package com.villagers.mod.threat;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class WanderingNecromancerSavedData extends SavedData {
    private static final String DATA_ID = "villagers_wandering_necromancers";

    public record GrudgeRecord(UUID necromancerEntityId, UUID exOwnerId, long nextStrikeDay) {
    }

    private final List<GrudgeRecord> grudges = new ArrayList<>();

    public static WanderingNecromancerSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(WanderingNecromancerSavedData::new, WanderingNecromancerSavedData::load, null),
                DATA_ID);
    }

    private static WanderingNecromancerSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        WanderingNecromancerSavedData data = new WanderingNecromancerSavedData();
        ListTag list = tag.getList("Grudges", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            data.grudges.add(new GrudgeRecord(
                    NbtUtils.loadUUID(entry.getCompound("Necromancer")),
                    NbtUtils.loadUUID(entry.getCompound("ExOwner")),
                    entry.getLong("NextStrikeDay")));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (GrudgeRecord record : grudges) {
            CompoundTag entry = new CompoundTag();
            entry.put("Necromancer", NbtUtils.createUUID(record.necromancerEntityId()));
            entry.put("ExOwner", NbtUtils.createUUID(record.exOwnerId()));
            entry.putLong("NextStrikeDay", record.nextStrikeDay());
            list.add(entry);
        }
        tag.put("Grudges", list);
        return tag;
    }

    public List<GrudgeRecord> grudges() {
        return grudges;
    }

    public void add(GrudgeRecord record) {
        grudges.add(record);
        setDirty();
    }

    public void removeNecromancer(UUID entityId) {
        if (grudges.removeIf(g -> g.necromancerEntityId().equals(entityId))) {
            setDirty();
        }
    }

    public void updateNextStrike(UUID entityId, long nextStrikeDay) {
        for (int i = 0; i < grudges.size(); i++) {
            GrudgeRecord g = grudges.get(i);
            if (g.necromancerEntityId().equals(entityId)) {
                grudges.set(i, new GrudgeRecord(g.necromancerEntityId(), g.exOwnerId(), nextStrikeDay));
                setDirty();
                return;
            }
        }
    }

    public void markOwnerOnline(UUID ownerId) {
        boolean changed = false;
        for (int i = 0; i < grudges.size(); i++) {
            if (grudges.get(i).exOwnerId().equals(ownerId)) {
                changed = true;
            }
        }
        if (changed) {
            setDirty();
        }
    }

    public void purgeDead(ServerLevel level) {
        Iterator<GrudgeRecord> it = grudges.iterator();
        boolean changed = false;
        while (it.hasNext()) {
            UUID id = it.next().necromancerEntityId();
            var entity = level.getEntity(id);
            if (entity == null || !entity.isAlive()) {
                it.remove();
                changed = true;
            }
        }
        if (changed) {
            setDirty();
        }
    }
}
