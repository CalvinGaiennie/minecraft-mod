package com.villagers.mod.atlas;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;

public final class ChunkSurfaceSampler {
    public static final int BLOCKS_PER_CHUNK = 16 * 16;

    private ChunkSurfaceSampler() {
    }

    /** One map-color id per block column in the chunk (x + z*16). */
    public static byte[] sample(Level level, ChunkAccess chunk) {
        byte[] colors = new byte[BLOCKS_PER_CHUNK];
        int baseX = chunk.getPos().getMinBlockX();
        int baseZ = chunk.getPos().getMinBlockZ();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int index = 0;
        for (int lz = 0; lz < 16; lz++) {
            for (int lx = 0; lx < 16; lx++) {
                int wx = baseX + lx;
                int wz = baseZ + lz;
                colors[index++] = sampleColumn(level, pos, wx, wz);
            }
        }
        return colors;
    }

    private static byte sampleColumn(Level level, BlockPos.MutableBlockPos pos, int wx, int wz) {
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, wx, wz);
        pos.set(wx, y, wz);
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) {
            while (y > level.getMinBuildHeight() && state.isAir()) {
                y--;
                pos.setY(y);
                state = level.getBlockState(pos);
            }
        } else {
            while (y < level.getMaxBuildHeight() - 1) {
                BlockState above = level.getBlockState(pos.above());
                if (above.isAir() || !above.getFluidState().isEmpty()) {
                    break;
                }
                y++;
                pos.setY(y);
                state = above;
            }
        }
        MapColor mapColor = state.getMapColor(level, pos);
        if (mapColor == null) {
            mapColor = MapColor.NONE;
        }
        return (byte) mapColor.id;
    }
}
