package com.villagers.mod.atlas;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;

import com.villagers.mod.network.ExplorationTerrainBatchPayload;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class AtlasExplorationService {
    private static final int EXPLORE_RADIUS_CHUNKS = 2;
    private static final int CHUNKS_PER_SYNC_TICK = 6;

    private static final Map<UUID, Deque<Long>> pendingTerrainSync = new ConcurrentHashMap<>();

    private AtlasExplorationService() {
    }

    public static void queueTerrainSync(ServerPlayer player) {
        if (!isOverworld(player.serverLevel())) {
            return;
        }
        AtlasExplorationSavedData data = AtlasExplorationSavedData.get(player.serverLevel());
        long[] keys = data.chunkKeysForPlayer(player.getUUID());
        Deque<Long> queue = new ArrayDeque<>(keys.length);
        for (long key : keys) {
            queue.addLast(key);
        }
        pendingTerrainSync.put(player.getUUID(), queue);
    }

    public static void tickPlayer(ServerPlayer player) {
        if (!isOverworld(player.serverLevel())) {
            return;
        }
        if (player.tickCount % 10 == 0) {
            revealNearbyChunks(player);
        }
        drainSyncQueue(player);
    }

    private static void revealNearbyChunks(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        ChunkPos center = player.chunkPosition();
        AtlasExplorationSavedData data = AtlasExplorationSavedData.get(level);
        UUID id = player.getUUID();

        for (int dx = -EXPLORE_RADIUS_CHUNKS; dx <= EXPLORE_RADIUS_CHUNKS; dx++) {
            for (int dz = -EXPLORE_RADIUS_CHUNKS; dz <= EXPLORE_RADIUS_CHUNKS; dz++) {
                ChunkPos pos = new ChunkPos(center.x + dx, center.z + dz);
                long key = pos.toLong();
                if (data.hasChunk(id, key)) {
                    continue;
                }
                if (!level.hasChunk(pos.x, pos.z)) {
                    continue;
                }
                byte[] colors = ChunkSurfaceSampler.sample(level, level.getChunk(pos.x, pos.z));
                data.putChunk(id, pos, colors);
                sendBatch(player, List.of(new ExplorationTerrainBatchPayload.ChunkSlice(pos.x, pos.z, colors)));
            }
        }
    }

    private static void drainSyncQueue(ServerPlayer player) {
        Deque<Long> queue = pendingTerrainSync.get(player.getUUID());
        if (queue == null || queue.isEmpty()) {
            pendingTerrainSync.remove(player.getUUID());
            return;
        }
        ServerLevel level = player.serverLevel();
        AtlasExplorationSavedData data = AtlasExplorationSavedData.get(level);
        List<ExplorationTerrainBatchPayload.ChunkSlice> batch = new ArrayList<>(CHUNKS_PER_SYNC_TICK);
        for (int i = 0; i < CHUNKS_PER_SYNC_TICK && !queue.isEmpty(); i++) {
            long key = queue.removeFirst();
            byte[] colors = data.getColors(player.getUUID(), key);
            if (colors == null) {
                continue;
            }
            ChunkPos pos = new ChunkPos(key);
            batch.add(new ExplorationTerrainBatchPayload.ChunkSlice(pos.x, pos.z, colors));
        }
        if (!batch.isEmpty()) {
            sendBatch(player, batch);
        }
        if (queue.isEmpty()) {
            pendingTerrainSync.remove(player.getUUID());
        }
    }

    private static void sendBatch(ServerPlayer player, List<ExplorationTerrainBatchPayload.ChunkSlice> batch) {
        PacketDistributor.sendToPlayer(player, new ExplorationTerrainBatchPayload(batch));
    }

    private static boolean isOverworld(ServerLevel level) {
        return level.dimension().location().getPath().equals("overworld");
    }
}
