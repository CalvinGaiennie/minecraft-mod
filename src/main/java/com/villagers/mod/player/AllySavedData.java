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
    private final Map<UUID, Set<UUID>> alliesByOwner = new HashMap<>();

    public static AllySavedData get(ServerLevel level) {
        return level.getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(AllySavedData::new, AllySavedData::load, null), DATA_ID);
    }

    private static AllySavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        AllySavedData data = new AllySavedData();
        CompoundTag owners = tag.getCompound("Owners");
        for (String key : owners.getAllKeys()) {
            UUID owner = UUID.fromString(key);
            Set<UUID> allies = new HashSet<>();
            ListTag list = owners.getCompound(key).getList("Allies", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                allies.add(NbtUtils.loadUUID(list.getCompound(i)));
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
            for (UUID ally : entry.getValue()) {
                list.add(NbtUtils.createUUID(ally));
            }
            CompoundTag one = new CompoundTag();
            one.put("Allies", list);
            owners.put(entry.getKey().toString(), one);
        }
        tag.put("Owners", owners);
        return tag;
    }

    public Set<UUID> getAllies(UUID owner) {
        return alliesByOwner.computeIfAbsent(owner, k -> new HashSet<>());
    }

    public boolean addAlly(UUID owner, UUID ally) {
        if (owner.equals(ally)) {
            return false;
        }
        if (getAllies(owner).add(ally)) {
            setDirty();
            return true;
        }
        return false;
    }

    public boolean removeAlly(UUID owner, UUID ally) {
        if (getAllies(owner).remove(ally)) {
            setDirty();
            return true;
        }
        return false;
    }

    public boolean isAlly(UUID owner, UUID other) {
        return owner.equals(other) || getAllies(owner).contains(other);
    }

    public void markDirtySelf() {
        setDirty();
    }
}
