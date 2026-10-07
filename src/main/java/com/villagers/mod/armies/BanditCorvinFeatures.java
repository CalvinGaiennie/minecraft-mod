package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** Corvin site dressing until `corvin_tower` structure NBT replaces this. */
public final class BanditCorvinFeatures {
    private BanditCorvinFeatures() {
    }

    public static void dressCorvinTower(ServerLevel level, BlockPos origin) {
        BlockPos base = origin.above();
        for (int dy = 0; dy < 6; dy++) {
            setIfAirOrReplaceable(level, base.above(dy), Blocks.COBBLESTONE.defaultBlockState());
        }
        setIfAirOrReplaceable(level, base.above(6), Blocks.OAK_FENCE.defaultBlockState());
        setIfAirOrReplaceable(level, base.above(7), Blocks.OAK_FENCE.defaultBlockState());
        setIfAirOrReplaceable(level, base.east().above(2), Blocks.COBBLESTONE_STAIRS.defaultBlockState());
        setIfAirOrReplaceable(level, base.west().above(2), Blocks.COBBLESTONE_STAIRS.defaultBlockState());
        setIfAirOrReplaceable(level, base.above(5), Blocks.LECTERN.defaultBlockState());
        setIfAirOrReplaceable(level, origin.south(2), Blocks.COBBLESTONE_WALL.defaultBlockState());
        setIfAirOrReplaceable(level, origin.south(2).west(), Blocks.COBBLESTONE_WALL.defaultBlockState());
        setIfAirOrReplaceable(level, origin.south(2).east(), Blocks.COBBLESTONE_WALL.defaultBlockState());
    }

    private static void setIfAirOrReplaceable(ServerLevel level, BlockPos pos, BlockState state) {
        BlockState existing = level.getBlockState(pos);
        if (existing.isAir() || existing.canBeReplaced()) {
            level.setBlock(pos, state, 3);
        }
    }
}
