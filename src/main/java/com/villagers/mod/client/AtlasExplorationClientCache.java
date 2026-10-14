package com.villagers.mod.client;

import net.minecraft.world.level.ChunkPos;

import com.villagers.mod.atlas.MapPalette;
import com.villagers.mod.network.ExplorationTerrainBatchPayload;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public final class AtlasExplorationClientCache {
    private static final Long2ObjectMap<byte[]> CHUNK_COLORS = new Long2ObjectOpenHashMap<>();

    private AtlasExplorationClientCache() {
    }

    public static void applyBatch(ExplorationTerrainBatchPayload payload) {
        for (ExplorationTerrainBatchPayload.ChunkSlice slice : payload.chunks()) {
            long key = ChunkPos.asLong(slice.chunkX(), slice.chunkZ());
            CHUNK_COLORS.put(key, slice.colors());
        }
    }

    public static void clear() {
        CHUNK_COLORS.clear();
    }

    public static int argbAtBlock(int worldX, int worldZ) {
        int chunkX = Math.floorDiv(worldX, 16);
        int chunkZ = Math.floorDiv(worldZ, 16);
        byte[] colors = CHUNK_COLORS.get(ChunkPos.asLong(chunkX, chunkZ));
        if (colors == null) {
            return MapPalette.UNEXPLORED_ARGB;
        }
        int localX = Math.floorMod(worldX, 16);
        int localZ = Math.floorMod(worldZ, 16);
        return MapPalette.argbFromMapColorId(colors[localX + localZ * 16]);
    }

    public static boolean isExploredBlock(int worldX, int worldZ) {
        int chunkX = Math.floorDiv(worldX, 16);
        int chunkZ = Math.floorDiv(worldZ, 16);
        return CHUNK_COLORS.containsKey(ChunkPos.asLong(chunkX, chunkZ));
    }
}
