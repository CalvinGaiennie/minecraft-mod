package com.villagers.mod.village;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;

/** Bells that were processed for natural-village militia (armed, skipped, or claimed). */
public final class NaturalVillageSavedData extends SavedData {
    private static final String DATA_ID = "villagers_natural_villages";
    private final Set<Long> processedBells = new HashSet<>();

    public static NaturalVillageSavedData get(ServerLevel level) {
        return level.getDataStorage()
                .computeIfAbsent(
                        new SavedData.Factory<>(NaturalVillageSavedData::new, NaturalVillageSavedData::load, null),
                        DATA_ID);
    }

    private static NaturalVillageSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        NaturalVillageSavedData data = new NaturalVillageSavedData();
        ListTag list = tag.getList("ProcessedBells", ListTag.TAG_LONG);
        for (int i = 0; i < list.size(); i++) {
            data.processedBells.add(((LongTag) list.get(i)).getAsLong());
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (long encoded : processedBells) {
            list.add(LongTag.valueOf(encoded));
        }
        tag.put("ProcessedBells", list);
        return tag;
    }

    public boolean isProcessed(BlockPos bell) {
        return processedBells.contains(bell.asLong());
    }

    public void markProcessed(BlockPos bell) {
        if (processedBells.add(bell.asLong())) {
            setDirty();
        }
    }

    public void markDirtySelf() {
        setDirty();
    }
}
