package com.villagers.mod.armies;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArmiesQuestSavedData extends SavedData {
    private static final String DATA_ID = "villagers_armies_quest";

    public static final class PlayerQuestState {
        public ArmiesQuestStage stage = ArmiesQuestStage.NOT_STARTED;
        public UUID d1VillageId;
        public boolean ledgerRead;
        public boolean garlandCompassUnlocked;
        public boolean rookbreakerTitle;
        public boolean corvinKilled;
        public boolean garlandKilled;
    }

    private final Map<UUID, PlayerQuestState> byOwner = new HashMap<>();

    public static ArmiesQuestSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(ArmiesQuestSavedData::new, ArmiesQuestSavedData::load, null),
                DATA_ID);
    }

    private static ArmiesQuestSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        ArmiesQuestSavedData data = new ArmiesQuestSavedData();
        ListTag list = tag.getList("Owners", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            UUID owner = entry.getUUID("Owner");
            PlayerQuestState state = new PlayerQuestState();
            state.stage = ArmiesQuestStage.valueOf(entry.getString("Stage"));
            if (entry.hasUUID("D1Village")) {
                state.d1VillageId = entry.getUUID("D1Village");
            }
            state.ledgerRead = entry.getBoolean("LedgerRead");
            state.garlandCompassUnlocked = entry.getBoolean("GarlandCompass");
            state.rookbreakerTitle = entry.getBoolean("Rookbreaker");
            state.corvinKilled = entry.getBoolean("CorvinKilled");
            state.garlandKilled = entry.getBoolean("GarlandKilled");
            data.byOwner.put(owner, state);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (var e : byOwner.entrySet()) {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("Owner", e.getKey());
            PlayerQuestState state = e.getValue();
            entry.putString("Stage", state.stage.name());
            if (state.d1VillageId != null) {
                entry.putUUID("D1Village", state.d1VillageId);
            }
            entry.putBoolean("LedgerRead", state.ledgerRead);
            entry.putBoolean("GarlandCompass", state.garlandCompassUnlocked);
            entry.putBoolean("Rookbreaker", state.rookbreakerTitle);
            entry.putBoolean("CorvinKilled", state.corvinKilled);
            entry.putBoolean("GarlandKilled", state.garlandKilled);
            list.add(entry);
        }
        tag.put("Owners", list);
        return tag;
    }

    public PlayerQuestState getOrCreate(UUID ownerId) {
        return byOwner.computeIfAbsent(ownerId, id -> new PlayerQuestState());
    }

    public boolean hasRookbreaker(UUID ownerId) {
        PlayerQuestState state = byOwner.get(ownerId);
        return state != null && state.rookbreakerTitle;
    }

    public void markDirtySelf() {
        setDirty();
    }
}
