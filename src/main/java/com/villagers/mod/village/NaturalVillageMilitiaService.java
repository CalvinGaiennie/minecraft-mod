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

import java.util.Comparator;
import java.util.List;

public final class NaturalVillageMilitiaService {
    private NaturalVillageMilitiaService() {
    }

    /** Scan a newly loaded chunk for village bells and arm natural militia once per bell. */
    public static void scanChunkForBells(ServerLevel level, net.minecraft.world.level.ChunkPos chunkPos) {
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
