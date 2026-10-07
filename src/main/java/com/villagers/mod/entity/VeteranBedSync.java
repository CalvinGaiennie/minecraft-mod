package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

/** Veterans use a normal villager bed (not post beds). Home is synced from villager brain memory. */
public final class VeteranBedSync {
    private VeteranBedSync() {
    }

    public static void syncHomeBedFromVillager(Villager villager) {
        if (!villager.hasData(VillagerAttachments.VETERAN_DATA.get())
                || villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return;
        }
        VeteranData veteran = villager.getData(VillagerAttachments.VETERAN_DATA.get());
        if (veteran == null) {
            return;
        }
        BlockPos bed = resolveHomeBed(villager);
        if (bed != null) {
            veteran.setHomeBed(bed);
        }
    }

    @Nullable
    private static BlockPos resolveHomeBed(Villager villager) {
        var brain = villager.getBrain();
        GlobalPos home = brain.getMemory(MemoryModuleType.HOME).orElse(null);
        if (home != null && home.dimension() == villager.level().dimension()) {
            BlockPos pos = home.pos();
            if (isVillagerBed(villager, pos)) {
                return pos.immutable();
            }
        }
        BlockPos nearest = brain.getMemory(MemoryModuleType.NEAREST_BED).orElse(null);
        if (nearest != null && isVillagerBed(villager, nearest)) {
            return nearest.immutable();
        }
        return null;
    }

    private static boolean isVillagerBed(Villager villager, BlockPos pos) {
        if (!villager.level().isLoaded(pos)) {
            return false;
        }
        BlockState state = villager.level().getBlockState(pos);
        if (!(state.getBlock() instanceof BedBlock)) {
            return false;
        }
        // Post beds are mod blocks, not vanilla BedBlock — soldiers only use those.
        return true;
    }
}
