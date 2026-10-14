package com.villagers.mod.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.RampartBlock;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.squad.SquadService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** Paths soldiers onto rampart tiles through doors, stairs, and turns. */
public final class RampartPathing {
    /** Wider than structure scan so they will cross doors to reach a wall walk. */
    public static final int RAMPART_SEEK_RADIUS = (int) SoldierCombatConstants.HOLD_GROUND_ENGAGE_RADIUS;
    public static final int Y_BELOW = 8;
    public static final int Y_ABOVE = 16;
    private static final int MAX_PATHFIND_TRIES = 8;
    private static final int CACHE_TICKS = 80;
    private static final int RAMPART_LIST_CACHE_TICKS = 50;
    /** How often idle soldiers re-issue rampart paths (was 20 = ~1 Hz, too stuttery on foot). */
    private static final int SEEK_INTERVAL_TICKS = 5;
    private static final Map<UUID, CachedRampartTarget> pathCache = new HashMap<>();
    private static final Map<RampartListKey, CachedRampartList> rampartListCache = new HashMap<>();

    private record CachedRampartTarget(BlockPos rampart, long gameTime) {
    }

    private record RampartListKey(ResourceKey<Level> dimension, ChunkPos chunk, int radius) {
    }

    private record CachedRampartList(List<BlockPos> ramparts, long gameTime) {
    }

    private RampartPathing() {
    }

    public static void tickSeekNearestRampart(Villager villager) {
        tickSeekNearestRampart(villager, 0.65);
    }

    public static void tickSeekNearestRampart(Villager villager, double speed) {
        if (!(villager.level() instanceof ServerLevel level)) {
            return;
        }
        if ((level.getGameTime() + villager.getId()) % SEEK_INTERVAL_TICKS != 0L
                && !SoldierRampartShooting.isOnRampart(villager)) {
            return;
        }
        if (SoldierRampartShooting.isOnRampart(villager)) {
            BlockPos on = RampartClaims.get(level).rampartBlockUnder(villager);
            if (on != null && level.getBlockState(on).is(VillagersMod.RAMPART.get())) {
                RampartClaims.get(level).claim(level, on, villager.getUUID());
            }
            villager.getNavigation().stop();
            return;
        }
        findBestRampartToStandOn(villager, RAMPART_SEEK_RADIUS).ifPresent(rampart -> {
            RampartClaims.get(level).claim(level, rampart, villager.getUUID());
            navigateToStand(villager, SoldierRampartShooting.standOnRampart(rampart), speed);
        });
    }

    public static void navigateToStand(Villager villager, Vec3 stand, double speed) {
        if (villager.position().distanceToSqr(stand) < 2.25) {
            villager.getNavigation().stop();
            return;
        }
        PathNavigation nav = villager.getNavigation();
        int repathInterval = speed >= 0.95 ? 4 : 8;
        if (nav.isDone() || villager.tickCount % repathInterval == 0) {
            nav.moveTo(stand.x, stand.y, stand.z, speed);
        }
    }

    public static Optional<BlockPos> findBestRampartToStandOn(Villager villager, int radius) {
        if (!(villager.level() instanceof ServerLevel server)) {
            return Optional.empty();
        }
        UUID self = villager.getUUID();
        long now = server.getGameTime();
        CachedRampartTarget cached = pathCache.get(self);
        if (cached != null && now - cached.gameTime() < CACHE_TICKS
                && RampartClaims.get(server).isAvailableFor(server, cached.rampart(), self)) {
            return Optional.of(cached.rampart());
        }

        List<BlockPos> ramparts = collectRampartsForSoldier(villager, radius);
        if (ramparts.isEmpty()) {
            return Optional.empty();
        }
        ramparts.sort(Comparator.comparingDouble(p -> villager.blockPosition().distSqr(p)));

        BlockPos bestPath = null;
        int bestNodes = Integer.MAX_VALUE;
        int tries = 0;
        for (BlockPos rampart : ramparts) {
            if (tries >= MAX_PATHFIND_TRIES) {
                break;
            }
            if (!canStandOn(villager.level(), rampart)) {
                continue;
            }
            if (!RampartClaims.get(server).isAvailableFor(server, rampart, self)) {
                continue;
            }
            tries++;
            Vec3 stand = SoldierRampartShooting.standOnRampart(rampart);
            Path path = villager.getNavigation().createPath(stand.x, stand.y, stand.z, 0);
            if (path != null && path.canReach()) {
                int nodes = path.getNodeCount();
                if (nodes < bestNodes) {
                    bestNodes = nodes;
                    bestPath = rampart;
                }
            }
        }
        if (bestPath != null) {
            pathCache.put(self, new CachedRampartTarget(bestPath, now));
            return Optional.of(bestPath);
        }
        for (BlockPos rampart : ramparts) {
            if (RampartClaims.get(server).isAvailableFor(server, rampart, self)) {
                pathCache.put(self, new CachedRampartTarget(rampart, now));
                return Optional.of(rampart);
            }
        }
        return Optional.empty();
    }

    private static List<BlockPos> collectRampartsForSoldier(Villager villager, int radius) {
        Level level = villager.level();
        BlockPos origin = villager.blockPosition();
        if (level instanceof ServerLevel server && villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null) {
                BlockPos personal = data.getAssignedRampart();
                if (personal != null && level.getBlockState(personal).getBlock() instanceof RampartBlock) {
                    return List.of(personal);
                }
                if (data.getSquadBoxPos() != null) {
                    RecruiterBlockEntity box = SquadService.squadBox(server, data.getSquadBoxPos());
                    if (box != null && !box.getSquadRamparts().isEmpty()) {
                        List<BlockPos> linked = new ArrayList<>();
                        for (BlockPos rampart : box.getSquadRamparts()) {
                            if (level.getBlockState(rampart).getBlock() instanceof RampartBlock) {
                                linked.add(rampart.immutable());
                            }
                        }
                        if (!linked.isEmpty()) {
                            return linked;
                        }
                    }
                }
            }
        }
        return collectRampartsInRadius(level, origin, radius);
    }

    private static List<BlockPos> collectRampartsInRadius(Level level, BlockPos origin, int radius) {
        if (level instanceof ServerLevel server) {
            long now = server.getGameTime();
            RampartListKey key = new RampartListKey(server.dimension(), new ChunkPos(origin), radius);
            CachedRampartList cached = rampartListCache.get(key);
            if (cached != null && now - cached.gameTime() < RAMPART_LIST_CACHE_TICKS) {
                return cached.ramparts();
            }
            List<BlockPos> list = scanRampartsInRadius(level, origin, radius);
            rampartListCache.put(key, new CachedRampartList(list, now));
            return list;
        }
        return scanRampartsInRadius(level, origin, radius);
    }

    private static List<BlockPos> scanRampartsInRadius(Level level, BlockPos origin, int radius) {
        List<BlockPos> list = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-radius, -Y_BELOW, -radius),
                origin.offset(radius, Y_ABOVE, radius))) {
            if (level.getBlockState(pos).is(VillagersMod.RAMPART.get())
                    || level.getBlockState(pos).getBlock() instanceof RampartBlock) {
                list.add(pos.immutable());
            }
        }
        return list;
    }

    private static boolean canStandOn(Level level, BlockPos rampartPos) {
        if (!(level instanceof ServerLevel server)) {
            return true;
        }
        BlockPos feet = rampartPos.above();
        if (!server.getBlockState(feet).getCollisionShape(server, feet).isEmpty()) {
            return false;
        }
        BlockPos head = feet.above();
        return server.getBlockState(head).getCollisionShape(server, head).isEmpty();
    }
}
