package com.villagers.mod.threat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class SwarmSavedData extends SavedData {
    private static final String DATA_ID = "villagers_swarms";
    private final List<SwarmRecord> swarms = new ArrayList<>();

    public record SwarmRecord(UUID id, String mobType, BlockPos target, List<UUID> members) {
    }

    public static SwarmSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(SwarmSavedData::new, SwarmSavedData::load, null),
                DATA_ID);
    }

    private static SwarmSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        SwarmSavedData data = new SwarmSavedData();
        ListTag list = tag.getList("Swarms", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            UUID id = entry.getUUID("Id");
            String type = entry.getString("Type");
            BlockPos target = new BlockPos(entry.getInt("X"), entry.getInt("Y"), entry.getInt("Z"));
            List<UUID> members = new ArrayList<>();
            ListTag mobList = entry.getList("Members", Tag.TAG_COMPOUND);
            for (int j = 0; j < mobList.size(); j++) {
                members.add(net.minecraft.nbt.NbtUtils.loadUUID(mobList.getCompound(j)));
            }
            data.swarms.add(new SwarmRecord(id, type, target, members));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (SwarmRecord swarm : swarms) {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("Id", swarm.id());
            entry.putString("Type", swarm.mobType());
            entry.putInt("X", swarm.target().getX());
            entry.putInt("Y", swarm.target().getY());
            entry.putInt("Z", swarm.target().getZ());
            ListTag mobList = new ListTag();
            for (UUID member : swarm.members()) {
                mobList.add(net.minecraft.nbt.NbtUtils.createUUID(member));
            }
            entry.put("Members", mobList);
            list.add(entry);
        }
        tag.put("Swarms", list);
        return tag;
    }

    public List<SwarmRecord> swarms() {
        return swarms;
    }

    public void add(SwarmRecord record) {
        swarms.add(record);
        setDirty();
    }

    public void remove(UUID id) {
        swarms.removeIf(s -> s.id().equals(id));
        setDirty();
    }

    public void purgeDead(ServerLevel level) {
        Iterator<SwarmRecord> it = swarms.iterator();
        while (it.hasNext()) {
            SwarmRecord swarm = it.next();
            swarm.members().removeIf(uuid -> level.getEntity(uuid) == null);
            if (swarm.members().isEmpty()) {
                it.remove();
            }
        }
        setDirty();
    }
}
