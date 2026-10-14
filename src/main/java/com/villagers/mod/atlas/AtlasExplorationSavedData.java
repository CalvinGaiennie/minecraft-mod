package com.villagers.mod.atlas;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AtlasExplorationSavedData extends SavedData {
    private static final String DATA_ID = "villagers_atlas_exploration";

    private final Map<UUID, Long2ObjectMap<byte[]>> byPlayer = new HashMap<>();

    public static AtlasExplorationSavedData get(ServerLevel level) {
        return level.getDataStorage()
                .computeIfAbsent(
                        new SavedData.Factory<>(AtlasExplorationSavedData::new, AtlasExplorationSavedData::load, null),
                        DATA_ID);
    }

    private static AtlasExplorationSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        AtlasExplorationSavedData data = new AtlasExplorationSavedData();
        ListTag players = tag.getList("Players", Tag.TAG_COMPOUND);
        for (int p = 0; p < players.size(); p++) {
            CompoundTag playerTag = players.getCompound(p);
            UUID playerId = playerTag.getUUID("Id");
            Long2ObjectMap<byte[]> chunks = new Long2ObjectOpenHashMap<>();
            ListTag list = playerTag.getList("Chunks", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag one = list.getCompound(i);
                long key = one.getLong("Pos");
                byte[] colors = one.getByteArray("Colors");
                if (colors.length == ChunkSurfaceSampler.BLOCKS_PER_CHUNK) {
                    chunks.put(key, colors);
                }
            }
            data.byPlayer.put(playerId, chunks);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag players = new ListTag();
        for (var entry : byPlayer.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("Id", entry.getKey());
            ListTag list = new ListTag();
            for (var chunkEntry : entry.getValue().long2ObjectEntrySet()) {
                CompoundTag one = new CompoundTag();
                one.putLong("Pos", chunkEntry.getLongKey());
                one.putByteArray("Colors", chunkEntry.getValue());
                list.add(one);
            }
            playerTag.put("Chunks", list);
            players.add(playerTag);
        }
        tag.put("Players", players);
        return tag;
    }

    private Long2ObjectMap<byte[]> chunks(UUID playerId) {
        return byPlayer.computeIfAbsent(playerId, id -> new Long2ObjectOpenHashMap<>());
    }

    public boolean hasChunk(UUID playerId, long chunkKey) {
        Long2ObjectMap<byte[]> map = byPlayer.get(playerId);
        return map != null && map.containsKey(chunkKey);
    }

    public byte[] getColors(UUID playerId, long chunkKey) {
        Long2ObjectMap<byte[]> map = byPlayer.get(playerId);
        return map == null ? null : map.get(chunkKey);
    }

    public long[] chunkKeysForPlayer(UUID playerId) {
        Long2ObjectMap<byte[]> map = byPlayer.get(playerId);
        if (map == null || map.isEmpty()) {
            return new long[0];
        }
        return map.keySet().toLongArray();
    }

    public void putChunk(UUID playerId, ChunkPos pos, byte[] colors) {
        chunks(playerId).put(pos.toLong(), colors);
        setDirty();
    }
}
