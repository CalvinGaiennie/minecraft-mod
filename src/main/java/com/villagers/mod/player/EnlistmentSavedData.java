package com.villagers.mod.player;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Players who interact with the mod (threats apply); others are left alone. */
public class EnlistmentSavedData extends SavedData {
    private static final String DATA_ID = "villagers_enlistment";
    private final Set<UUID> enlisted = new HashSet<>();
    private final Set<UUID> optedOut = new HashSet<>();

    public static EnlistmentSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(EnlistmentSavedData::new, EnlistmentSavedData::load, null),
                DATA_ID);
    }

    private static EnlistmentSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        EnlistmentSavedData data = new EnlistmentSavedData();
        readUuidList(tag, "Enlisted", data.enlisted);
        readUuidList(tag, "OptedOut", data.optedOut);
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        writeUuidList(tag, "Enlisted", enlisted);
        writeUuidList(tag, "OptedOut", optedOut);
        return tag;
    }

    public boolean isEnlisted(UUID playerId) {
        if (optedOut.contains(playerId)) {
            return false;
        }
        return enlisted.contains(playerId);
    }

    public void enlist(UUID playerId) {
        optedOut.remove(playerId);
        if (enlisted.add(playerId)) {
            setDirty();
        }
    }

    public void optOut(UUID playerId) {
        enlisted.remove(playerId);
        if (optedOut.add(playerId)) {
            setDirty();
        }
    }

    public boolean isOptedOut(UUID playerId) {
        return optedOut.contains(playerId);
    }

    private static void readUuidList(CompoundTag tag, String key, Set<UUID> target) {
        if (!tag.contains(key, Tag.TAG_LIST)) {
            return;
        }
        ListTag list = tag.getList(key, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            target.add(NbtUtils.loadUUID(list.getCompound(i)));
        }
    }

    private static void writeUuidList(CompoundTag tag, String key, Set<UUID> source) {
        ListTag list = new ListTag();
        for (UUID id : source) {
            list.add(net.minecraft.nbt.NbtUtils.createUUID(id));
        }
        tag.put(key, list);
    }
}
