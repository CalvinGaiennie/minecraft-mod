package com.villagers.mod.player;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** One-way ally lists per player (design: ally book persists on player, not item). */
public final class AllySavedData extends SavedData {
    private static final String DATA_ID = "villagers_allies";
    private final Map<UUID, Map<UUID, AllyRank>> alliesByOwner = new HashMap<>();

    public static AllySavedData get(ServerLevel level) {
        return level.getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(AllySavedData::new, AllySavedData::load, null), DATA_ID);
    }

    private static AllySavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        AllySavedData data = new AllySavedData();
        CompoundTag owners = tag.getCompound("Owners");
        for (String key : owners.getAllKeys()) {
            UUID owner = UUID.fromString(key);
            Map<UUID, AllyRank> allies = new HashMap<>();
            CompoundTag ownerTag = owners.getCompound(key);
            if (ownerTag.contains("RankedAllies", Tag.TAG_LIST)) {
                ListTag list = ownerTag.getList("RankedAllies", Tag.TAG_COMPOUND);
                for (int i = 0; i < list.size(); i++) {
                    CompoundTag one = list.getCompound(i);
                    allies.put(one.getUUID("Id"), AllyRank.fromString(one.getString("Rank")));
                }
            } else {
                ListTag list = ownerTag.getList("Allies", Tag.TAG_COMPOUND);
                for (int i = 0; i < list.size(); i++) {
                    allies.put(NbtUtils.loadUUID(list.getCompound(i)), AllyRank.FRIEND);
                }
            }
            data.alliesByOwner.put(owner, allies);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag owners = new CompoundTag();
        for (var entry : alliesByOwner.entrySet()) {
            ListTag list = new ListTag();
            for (var allyEntry : entry.getValue().entrySet()) {
                CompoundTag one = new CompoundTag();
                one.putUUID("Id", allyEntry.getKey());
                one.putString("Rank", allyEntry.getValue().name());
                list.add(one);
            }
            CompoundTag one = new CompoundTag();
            one.put("RankedAllies", list);
            owners.put(entry.getKey().toString(), one);
        }
        tag.put("Owners", owners);
        return tag;
    }

    public Set<UUID> getAllies(UUID owner) {
        return new HashSet<>(alliesByOwner.computeIfAbsent(owner, k -> new HashMap<>()).keySet());
    }

    public AllyRank getRank(UUID owner, UUID ally) {
        if (owner.equals(ally)) {
            return AllyRank.CO_OWNER;
        }
        Map<UUID, AllyRank> map = alliesByOwner.get(owner);
        if (map == null) {
            return null;
        }
        return map.get(ally);
    }

    public boolean addAlly(UUID owner, UUID ally, AllyRank rank) {
        if (owner.equals(ally)) {
            return false;
        }
        Map<UUID, AllyRank> map = alliesByOwner.computeIfAbsent(owner, k -> new HashMap<>());
        AllyRank previous = map.put(ally, rank);
        if (previous == null || previous != rank) {
            setDirty();
            return true;
        }
        return false;
    }

    public boolean addAlly(UUID owner, UUID ally) {
        return addAlly(owner, ally, AllyRank.FRIEND);
    }

    public boolean setRank(UUID owner, UUID ally, AllyRank rank) {
        Map<UUID, AllyRank> map = alliesByOwner.get(owner);
        if (map == null || !map.containsKey(ally)) {
            return false;
        }
        map.put(ally, rank);
        setDirty();
        return true;
    }

    public boolean removeAlly(UUID owner, UUID ally) {
        Map<UUID, AllyRank> map = alliesByOwner.get(owner);
        if (map != null && map.remove(ally) != null) {
            setDirty();
            return true;
        }
        return false;
    }

    public boolean isAlly(UUID owner, UUID other) {
        return owner.equals(other) || getAllies(owner).contains(other);
    }

    public Map<UUID, AllyRank> rankedAllies(UUID owner) {
        return Map.copyOf(alliesByOwner.computeIfAbsent(owner, k -> new HashMap<>()));
    }

    public void markDirtySelf() {
        setDirty();
    }
}
