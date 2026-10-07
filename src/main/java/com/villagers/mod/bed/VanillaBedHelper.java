package com.villagers.mod.bed;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public final class VanillaBedHelper {
    private VanillaBedHelper() {
    }

    public static boolean isBed(BlockState state) {
        return state.getBlock() instanceof BedBlock;
    }

    public static BlockPos footPosition(Level level, BlockPos anyPart) {
        BlockState state = level.getBlockState(anyPart);
        if (!(state.getBlock() instanceof BedBlock)) {
            return anyPart;
        }
        if (state.getValue(BedBlock.PART) == BedPart.FOOT) {
            return anyPart.immutable();
        }
        return anyPart.relative(BedBlock.getConnectedDirection(state).getOpposite()).immutable();
    }

    public static BlockPos sleepPosition(Level level, BlockPos footOrHead) {
        BlockState state = level.getBlockState(footOrHead);
        if (!(state.getBlock() instanceof BedBlock)) {
            return footOrHead;
        }
        if (state.getValue(BedBlock.PART) == BedPart.HEAD) {
            return footOrHead;
        }
        return footOrHead.relative(BedBlock.getConnectedDirection(state));
    }

    public static boolean isIntactBed(Level level, BlockPos foot) {
        BlockState footState = level.getBlockState(foot);
        if (!(footState.getBlock() instanceof BedBlock) || footState.getValue(BedBlock.PART) != BedPart.FOOT) {
            return false;
        }
        BlockPos head = foot.relative(BedBlock.getConnectedDirection(footState));
        BlockState headState = level.getBlockState(head);
        return headState.getBlock() instanceof BedBlock && headState.getValue(BedBlock.PART) == BedPart.HEAD;
    }
}
