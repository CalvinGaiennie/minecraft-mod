package com.villagers.mod.village;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VillageSavedData extends SavedData {
    private static final String DATA_ID = "villagers_village_claims";
    private final Map<UUID, VillageData> villages = new HashMap<>();

    public static VillageSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(VillageSavedData::new, VillageSavedData::load, null),
                DATA_ID);
    }

    private static VillageSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        VillageSavedData data = new VillageSavedData();
        ListTag list = tag.getList("Villages", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            VillageData village = VillageData.readFromTag(list.getCompound(i));
            data.villages.put(village.getVillageId(), village);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (VillageData village : villages.values()) {
            CompoundTag entry = new CompoundTag();
            village.writeToTag(entry);
            list.add(entry);
        }
        tag.put("Villages", list);
        return tag;
    }

    public Map<UUID, VillageData> villages() {
        return villages;
    }

    public void markDirty() {
        setDirty();
    }
}
