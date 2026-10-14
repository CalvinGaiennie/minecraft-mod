package com.villagers.mod.relic;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Per-player once-per-day relic saves (Last Mantle, Crown fallback). */
public class RelicCooldownSavedData extends SavedData {
    private static final String DATA_ID = "villagers_relic_cooldowns";

    private final Map<UUID, Integer> lastMantleDay = new HashMap<>();
    private final Map<UUID, Integer> crownSaveDay = new HashMap<>();

    public static RelicCooldownSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(RelicCooldownSavedData::new, RelicCooldownSavedData::load, null),
                DATA_ID);
    }

    private static RelicCooldownSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        RelicCooldownSavedData data = new RelicCooldownSavedData();
        readMap(tag.getList("LastMantle", Tag.TAG_COMPOUND), data.lastMantleDay);
        readMap(tag.getList("CrownSave", Tag.TAG_COMPOUND), data.crownSaveDay);
        return data;
    }

    private static void readMap(ListTag list, Map<UUID, Integer> target) {
        for (Tag entry : list) {
            CompoundTag c = (CompoundTag) entry;
            target.put(c.getUUID("Id"), c.getInt("Day"));
        }
    }

    private static ListTag writeMap(Map<UUID, Integer> source) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, Integer> e : source.entrySet()) {
            CompoundTag c = new CompoundTag();
            c.putUUID("Id", e.getKey());
            c.putInt("Day", e.getValue());
            list.add(c);
        }
        return list;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("LastMantle", writeMap(lastMantleDay));
        tag.put("CrownSave", writeMap(crownSaveDay));
        return tag;
    }

    public boolean canUseLastMantle(UUID player, int day) {
        return lastMantleDay.getOrDefault(player, -1) != day;
    }

    public void markLastMantle(UUID player, int day) {
        lastMantleDay.put(player, day);
        setDirty();
    }

    public boolean canUseCrownSave(UUID player, int day) {
        return crownSaveDay.getOrDefault(player, -1) != day;
    }

    public void markCrownSave(UUID player, int day) {
        crownSaveDay.put(player, day);
        setDirty();
    }

    public static int currentDay(ServerLevel level) {
        return (int) (level.getDayTime() / 24000L);
    }
}
