package com.villagers.mod.village;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

import com.villagers.mod.Config;
import com.villagers.mod.block.MessStationBlock;
import com.villagers.mod.entity.MilitiaConversion;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;
import com.villagers.mod.integration.CitadelAnchorSavedData;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class NaturalVillageMilitiaService {
    private static final Map<ResourceKey<Level>, Deque<net.minecraft.world.level.ChunkPos>> PENDING_SCANS = new HashMap<>();
    private static final Map<ResourceKey<Level>, Set<Long>> QUEUED_CHUNK_KEYS = new HashMap<>();

    private NaturalVillageMilitiaService() {
    }

    /** Queue a chunk for bell scan (never runs synchronously on chunk load / login). */
    public static void requestChunkScan(ServerLevel level, net.minecraft.world.level.ChunkPos chunkPos) {
        if (!level.dimension().location().getPath().equals("overworld")) {
            return;
        }
        ResourceKey<Level> dim = level.dimension();
        Set<Long> queued = QUEUED_CHUNK_KEYS.computeIfAbsent(dim, k -> new HashSet<>());
        long key = chunkPos.toLong();
        if (!queued.add(key)) {
            return;
        }
        PENDING_SCANS.computeIfAbsent(dim, k -> new ArrayDeque<>()).addLast(chunkPos);
    }

    public static int pendingScanCount(ServerLevel level) {
        Deque<net.minecraft.world.level.ChunkPos> q = PENDING_SCANS.get(level.dimension());
        return q == null ? 0 : q.size();
    }

    /** Called once per overworld tick from {@link com.villagers.mod.event.ThreatTickHandler}. */
    public static void tick(ServerLevel level) {
        if (!level.dimension().location().getPath().equals("overworld")) {
            return;
        }
        Deque<net.minecraft.world.level.ChunkPos> queue = PENDING_SCANS.get(level.dimension());
        if (queue == null || queue.isEmpty()) {
            return;
        }
        Set<Long> queued = QUEUED_CHUNK_KEYS.getOrDefault(level.dimension(), Set.of());
        int budget = Config.NATURAL_MILITIA_CHUNKS_SCANNED_PER_TICK.get();
        while (budget-- > 0 && !queue.isEmpty()) {
            net.minecraft.world.level.ChunkPos pos = queue.pollFirst();
            queued.remove(pos.toLong());
            scanChunkForBells(level, pos);
        }
    }

    /** Scan a chunk for village bells and arm natural militia once per bell. */
    public static void scanChunkForBells(ServerLevel level, net.minecraft.world.level.ChunkPos chunkPos) {
        if (!level.getChunkSource().hasChunk(chunkPos.x, chunkPos.z)) {
            return;
        }
        int minX = chunkPos.getMinBlockX();
        int minZ = chunkPos.getMinBlockZ();
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int wx = minX + dx;
                int wz = minZ + dz;
                int top = level.getHeight(Heightmap.Types.WORLD_SURFACE, wx, wz);
                int minY = Math.max(level.getMinBuildHeight(), top - 32);
                int maxY = Math.min(level.getMaxBuildHeight() - 1, top + 16);
                for (int y = minY; y <= maxY; y++) {
                    BlockPos pos = new BlockPos(wx, y, wz);
                    if (level.getBlockState(pos).is(Blocks.BELL)) {
                        tryArmAtBell(level, pos);
                    }
                }
            }
        }
    }

    public static void tryArmAtBell(ServerLevel level, BlockPos bell) {
        NaturalVillageSavedData saved = NaturalVillageSavedData.get(level);
        if (saved.isProcessed(bell)) {
            return;
        }
        int radius = Config.NATURAL_VILLAGE_BELL_RADIUS.get();
        if (hasMessStation(level, bell, radius)) {
            saved.markProcessed(bell);
            return;
        }
        List<Villager> villagers = level.getEntitiesOfClass(
                Villager.class,
                new AABB(bell).inflate(radius, radius, radius),
                v -> !v.isBaby()
                        && !v.hasData(VillagerAttachments.SOLDIER_DATA.get())
                        && !v.hasData(VillagerAttachments.MILITIA_DATA.get())
                        && !v.hasData(VillagerAttachments.BANDIT_DATA.get()));
        if (villagers.size() < 3) {
            return;
        }
        int target = militiaCountForBell(level, bell);
        villagers.sort(Comparator.comparingInt(NaturalVillageMilitiaService::militiaPickPriority));
        RandomSource random = level.getRandom();
        int armed = 0;
        for (Villager villager : villagers) {
            if (armed >= target) {
                break;
            }
            if (equipNaturalMilitiaGear(villager, random)) {
                MilitiaConversion.onGearUpdated(villager);
                if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
                    armed++;
                }
            }
        }
        saved.markProcessed(bell);
    }

    private static int militiaPickPriority(Villager villager) {
        VillagerProfession profession = villager.getVillagerData().getProfession();
        if (profession != VillagerProfession.NONE && profession != VillagerProfession.NITWIT) {
            return 0;
        }
        if (profession == VillagerProfession.NITWIT) {
            return 2;
        }
        return 1;
    }

    private static int militiaCountForBell(ServerLevel level, BlockPos bell) {
        int base = Config.NATURAL_MILITIA_BASE_COUNT.get();
        if (!Config.NATURAL_MILITIA_USE_CITADEL_TIER_COUNTS.get()
                || CitadelAnchorSavedData.citadelCenter(level).isEmpty()) {
            return base;
        }
        BlockPos spawn = level.getSharedSpawnPos();
        double dist = Math.sqrt(bell.distSqr(spawn));
        int near = Config.NATURAL_MILITIA_NEAR_SPAWN_DISTANCE.get();
        int mid = Config.NATURAL_MILITIA_MID_DISTANCE.get();
        if (dist < near) {
            return Config.NATURAL_MILITIA_NEAR_SPAWN_COUNT.get();
        }
        if (dist < mid) {
            return Config.NATURAL_MILITIA_MID_COUNT.get();
        }
        return base;
    }

    private static boolean hasMessStation(ServerLevel level, BlockPos center, int radius) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -16; dy <= 16; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    pos.setWithOffset(center, dx, dy, dz);
                    if (level.getBlockState(pos).getBlock() instanceof MessStationBlock) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean equipNaturalMilitiaGear(Villager villager, RandomSource random) {
        ItemStack[] armorPool = {
                new ItemStack(Items.LEATHER_HELMET),
                new ItemStack(Items.LEATHER_CHESTPLATE),
                new ItemStack(Items.LEATHER_BOOTS),
                new ItemStack(Items.CHAINMAIL_HELMET),
                new ItemStack(Items.CHAINMAIL_CHESTPLATE),
                new ItemStack(Items.CHAINMAIL_BOOTS),
                new ItemStack(Items.IRON_HELMET),
                new ItemStack(Items.IRON_CHESTPLATE),
                new ItemStack(Items.IRON_BOOTS)
        };
        VillagerGearSlot armorSlot = switch (random.nextInt(3)) {
            case 0 -> VillagerGearSlot.HELMET;
            case 1 -> VillagerGearSlot.CHESTPLATE;
            default -> VillagerGearSlot.BOOTS;
        };
        VillagerGearRules.set(villager, armorSlot, armorPool[random.nextInt(armorPool.length)].copy());

        if (random.nextFloat() < 0.25f) {
            VillagerGearRules.set(villager, VillagerGearSlot.RANGED, new ItemStack(Items.BOW));
            VillagerGearRules.set(villager, VillagerGearSlot.ARROWS, new ItemStack(Items.ARROW, 16));
            VillagerGearRules.set(villager, VillagerGearSlot.MELEE, ItemStack.EMPTY);
        } else {
            ItemStack melee = random.nextBoolean() ? new ItemStack(Items.IRON_SWORD) : new ItemStack(Items.IRON_AXE);
            VillagerGearRules.set(villager, VillagerGearSlot.MELEE, melee);
            VillagerGearRules.set(villager, VillagerGearSlot.RANGED, ItemStack.EMPTY);
            VillagerGearRules.set(villager, VillagerGearSlot.ARROWS, ItemStack.EMPTY);
        }
        return MilitiaConversion.canBecomeMilitia(villager);
    }
}
