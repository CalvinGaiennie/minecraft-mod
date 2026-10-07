package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class MilitiaHomeSync {
    private MilitiaHomeSync() {
    }

    public static void syncHomeBedFromVillager(Villager villager, MilitiaData data) {
        if (villager.level().isClientSide) {
            return;
        }
        GlobalPos home = villager.getBrain().getMemory(MemoryModuleType.HOME).orElse(null);
        if (home == null || home.dimension() != villager.level().dimension()) {
            data.setHomeBed(null);
            return;
        }
        BlockPos pos = home.pos();
        BlockState state = villager.level().getBlockState(pos);
        if (state.getBlock() instanceof BedBlock) {
            data.setHomeBed(pos);
        } else {
            data.setHomeBed(null);
        }
    }
}
