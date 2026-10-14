package com.villagers.mod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Aggregated wanted-player IDs per village (from wall posters). */
public final class WantedPosterSavedData extends SavedData {
    private static final String DATA_ID = "villagers_wanted_posters";
    private final Map<UUID, Map<BlockPos, Set<UUID>>> byVillage = new HashMap<>();

    public static WantedPosterSavedData get(ServerLevel level) {
        return level.getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(WantedPosterSavedData::new, WantedPosterSavedData::load, null), DATA_ID);
    }

    private static WantedPosterSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        WantedPosterSavedData data = new WantedPosterSavedData();
        CompoundTag villages = tag.getCompound("Villages");
        for (String villageKey : villages.getAllKeys()) {
            UUID villageId = UUID.fromString(villageKey);
            Map<BlockPos, Set<UUID>> posters = new HashMap<>();
            CompoundTag villageTag = villages.getCompound(villageKey);
            for (String posKey : villageTag.getAllKeys()) {
                BlockPos pos = BlockPos.of(Long.parseLong(posKey));
                ListTag list = villageTag.getList(posKey, Tag.TAG_COMPOUND);
                Set<UUID> wanted = new HashSet<>();
                for (int i = 0; i < list.size(); i++) {
                    wanted.add(net.minecraft.nbt.NbtUtils.loadUUID(list.getCompound(i)));
                }
                posters.put(pos, wanted);
            }
            data.byVillage.put(villageId, posters);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag villages = new CompoundTag();
        for (var villageEntry : byVillage.entrySet()) {
            CompoundTag villageTag = new CompoundTag();
            for (var posterEntry : villageEntry.getValue().entrySet()) {
                ListTag list = new ListTag();
                for (UUID id : posterEntry.getValue()) {
                    list.add(net.minecraft.nbt.NbtUtils.createUUID(id));
                }
                villageTag.put(Long.toString(posterEntry.getKey().asLong()), list);
            }
            villages.put(villageEntry.getKey().toString(), villageTag);
        }
        tag.put("Villages", villages);
        return tag;
    }

    public void setPoster(UUID villageId, BlockPos pos, Set<UUID> wantedIds) {
        Map<BlockPos, Set<UUID>> posters = byVillage.computeIfAbsent(villageId, k -> new HashMap<>());
        if (wantedIds.isEmpty()) {
            posters.remove(pos);
            if (posters.isEmpty()) {
                byVillage.remove(villageId);
            }
        } else {
            posters.put(pos.immutable(), Set.copyOf(wantedIds));
        }
        setDirty();
    }

    public void removePoster(UUID villageId, BlockPos pos) {
        Map<BlockPos, Set<UUID>> posters = byVillage.get(villageId);
        if (posters != null) {
            posters.remove(pos);
            if (posters.isEmpty()) {
                byVillage.remove(villageId);
            }
            setDirty();
        }
    }

    public boolean isWanted(UUID villageId, UUID playerId) {
        Map<BlockPos, Set<UUID>> posters = byVillage.get(villageId);
        if (posters == null) {
            return false;
        }
        for (Set<UUID> set : posters.values()) {
            if (set.contains(playerId)) {
                return true;
            }
        }
        return false;
    }

    public Set<UUID> wantedInVillage(UUID villageId) {
        Map<BlockPos, Set<UUID>> posters = byVillage.get(villageId);
        if (posters == null || posters.isEmpty()) {
            return Set.of();
        }
        Set<UUID> merged = new HashSet<>();
        for (Set<UUID> set : posters.values()) {
            merged.addAll(set);
        }
        return Collections.unmodifiableSet(merged);
    }

    @Nullable
    public Set<UUID> atPoster(UUID villageId, BlockPos pos) {
        Map<BlockPos, Set<UUID>> posters = byVillage.get(villageId);
        if (posters == null) {
            return null;
        }
        return posters.get(pos);
    }
}
