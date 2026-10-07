package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;

import com.villagers.mod.VillagersMod;

/** Minimal hideout dressing: caltrop lanes until full cave templates exist. */
public final class BanditHideoutFeatures {
    private BanditHideoutFeatures() {
    }

    public static void dressHideout(ServerLevel level, BlockPos origin, RandomSource random) {
        for (int i = 0; i < 8; i++) {
            BlockPos pos = origin.offset(-2 + random.nextInt(5), 0, -2 + random.nextInt(5));
            if (level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).isSolidRender(level, pos.below())) {
                level.setBlock(pos, VillagersMod.CALTROP_BLOCK.get().defaultBlockState(), 3);
            }
        }
        BlockPos cell = origin.south(2);
        if (level.getBlockState(cell).isAir()) {
            level.setBlock(cell, Blocks.IRON_BARS.defaultBlockState(), 3);
        }
    }
}
