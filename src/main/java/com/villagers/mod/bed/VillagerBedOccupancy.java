package com.villagers.mod.bed;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

import java.util.UUID;

public final class VillagerBedOccupancy {
    private VillagerBedOccupancy() {
    }

    /** Another villager (not {@code excludeId}) uses this bed foot as HOME. */
    public static boolean isHomeOfOtherVillager(Level level, BlockPos bedFoot, UUID excludeId) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return false;
        }
        BlockPos foot = VanillaBedHelper.footPosition(level, bedFoot);
        for (Villager villager : serverLevel.getEntitiesOfClass(Villager.class, net.minecraft.world.phys.AABB.ofSize(
                foot.getCenter(), 128, 128, 128))) {
            if (villager.getUUID().equals(excludeId)) {
                continue;
            }
            GlobalPos home = villager.getBrain().getMemory(MemoryModuleType.HOME).orElse(null);
            if (home == null || home.dimension() != level.dimension()) {
                continue;
            }
            BlockPos theirFoot = VanillaBedHelper.footPosition(level, home.pos());
            if (theirFoot.equals(foot)) {
                return true;
            }
        }
        return false;
    }
}
