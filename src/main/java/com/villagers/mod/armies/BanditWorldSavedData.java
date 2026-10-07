package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanditWorldSavedData extends SavedData {
    private static final String DATA_ID = "villagers_bandit_sites";

    public enum SiteKind {
        CAMP,
        HIDEOUT,
        CORVIN,
        GARLAND
    }

    public record SiteRecord(UUID id, SiteKind kind, BlockPos origin, boolean lootFilled, long nextRespawnGameTime) {
    }

    private final List<SiteRecord> sites = new ArrayList<>();
    private boolean generated;
    private boolean corvinBossDefeated;
    private boolean garlandBossDefeated;

    public static BanditWorldSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(BanditWorldSavedData::new, BanditWorldSavedData::load, null),
                DATA_ID);
    }

    private static BanditWorldSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        BanditWorldSavedData data = new BanditWorldSavedData();
        data.generated = tag.getBoolean("Generated");
        data.corvinBossDefeated = tag.getBoolean("CorvinDefeated");
        data.garlandBossDefeated = tag.getBoolean("GarlandDefeated");
        ListTag list = tag.getList("Sites", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            data.sites.add(new SiteRecord(
                    entry.getUUID("Id"),
                    SiteKind.valueOf(entry.getString("Kind")),
                    new BlockPos(entry.getInt("X"), entry.getInt("Y"), entry.getInt("Z")),
                    entry.getBoolean("LootFilled"),
                    entry.getLong("NextRespawn")));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("Generated", generated);
        tag.putBoolean("CorvinDefeated", corvinBossDefeated);
        tag.putBoolean("GarlandDefeated", garlandBossDefeated);
        ListTag list = new ListTag();
        for (SiteRecord site : sites) {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("Id", site.id());
            entry.putString("Kind", site.kind().name());
            entry.putInt("X", site.origin().getX());
            entry.putInt("Y", site.origin().getY());
            entry.putInt("Z", site.origin().getZ());
            entry.putBoolean("LootFilled", site.lootFilled());
            entry.putLong("NextRespawn", site.nextRespawnGameTime());
            list.add(entry);
        }
        tag.put("Sites", list);
        return tag;
    }

    public List<SiteRecord> sites() {
        return sites;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated() {
        generated = true;
        setDirty();
    }

    public void addSite(SiteRecord site) {
        sites.add(site);
        setDirty();
    }

    public void updateSite(SiteRecord updated) {
        for (int i = 0; i < sites.size(); i++) {
            if (sites.get(i).id().equals(updated.id())) {
                sites.set(i, updated);
                setDirty();
                return;
            }
        }
    }

    public SiteRecord findById(UUID id) {
        for (SiteRecord site : sites) {
            if (site.id().equals(id)) {
                return site;
            }
        }
        return null;
    }

    public SiteRecord findKind(SiteKind kind) {
        for (SiteRecord site : sites) {
            if (site.kind() == kind) {
                return site;
            }
        }
        return null;
    }

    public boolean isCorvinBossDefeated() {
        return corvinBossDefeated;
    }

    public void setCorvinBossDefeated() {
        corvinBossDefeated = true;
        setDirty();
    }

    public boolean isGarlandBossDefeated() {
        return garlandBossDefeated;
    }

    public void setGarlandBossDefeated() {
        garlandBossDefeated = true;
        setDirty();
    }
}
