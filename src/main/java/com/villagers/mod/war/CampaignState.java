package com.villagers.mod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CampaignState extends SavedData {
    private static final String DATA_ID = "villagers_campaigns";
    private final Map<UUID, CampRecord> camps = new HashMap<>();

    public record CampRecord(BlockPos pos, ResourceKey<Level> dimension) {
    }

    public static CampaignState get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(CampaignState::new, CampaignState::load, null),
                DATA_ID);
    }

    private static CampaignState load(CompoundTag tag, HolderLookup.Provider registries) {
        CampaignState state = new CampaignState();
        ListTag list = tag.getList("Camps", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            UUID owner = entry.getUUID("Owner");
            BlockPos pos = new BlockPos(entry.getInt("X"), entry.getInt("Y"), entry.getInt("Z"));
            ResourceKey<Level> dim = ResourceKey.create(
                    Registries.DIMENSION,
                    ResourceLocation.parse(entry.getString("Dimension")));
            state.camps.put(owner, new CampRecord(pos, dim));
        }
        return state;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (var e : camps.entrySet()) {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("Owner", e.getKey());
            entry.putInt("X", e.getValue().pos().getX());
            entry.putInt("Y", e.getValue().pos().getY());
            entry.putInt("Z", e.getValue().pos().getZ());
            entry.putString("Dimension", e.getValue().dimension().location().toString());
            list.add(entry);
        }
        tag.put("Camps", list);
        return tag;
    }

    public Map<UUID, CampRecord> camps() {
        return camps;
    }

    public void setCamp(UUID owner, BlockPos pos, ResourceKey<Level> dimension) {
        camps.put(owner, new CampRecord(pos.immutable(), dimension));
        setDirty();
    }

    public void clearCamp(UUID owner) {
        camps.remove(owner);
        setDirty();
    }
}
