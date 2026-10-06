package com.villagers.mod.bed;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SoldierBedClaims extends SavedData {
    private static final String DATA_ID = "villagers_soldier_beds";
    private final Map<BlockPos, UUID> bedFootToSoldier = new HashMap<>();

    public static SoldierBedClaims get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(SoldierBedClaims::new, SoldierBedClaims::load, null),
                DATA_ID);
    }

    private static SoldierBedClaims load(CompoundTag tag, HolderLookup.Provider registries) {
        SoldierBedClaims data = new SoldierBedClaims();
        ListTag list = tag.getList("Claims", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            BlockPos pos = BlockPos.of(entry.getLong("Pos"));
            UUID soldier = entry.getUUID("Soldier");
            data.bedFootToSoldier.put(pos.immutable(), soldier);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (var entry : bedFootToSoldier.entrySet()) {
            CompoundTag bed = new CompoundTag();
            bed.putLong("Pos", entry.getKey().asLong());
            bed.putUUID("Soldier", entry.getValue());
            list.add(bed);
        }
        tag.put("Claims", list);
        return tag;
    }

    public boolean isClaimedByOther(BlockPos bedFoot, UUID soldierId) {
        UUID owner = bedFootToSoldier.get(bedFoot.immutable());
        return owner != null && !owner.equals(soldierId);
    }

    @Nullable
    public UUID getSoldierAt(BlockPos bedFoot) {
        return bedFootToSoldier.get(bedFoot.immutable());
    }

    public void claim(BlockPos bedFoot, UUID soldierId) {
        releaseSoldier(soldierId);
        bedFootToSoldier.put(bedFoot.immutable(), soldierId);
        setDirty();
    }

    public void releaseAt(BlockPos bedFoot) {
        if (bedFootToSoldier.remove(bedFoot.immutable()) != null) {
            setDirty();
        }
    }

    public void releaseSoldier(UUID soldierId) {
        if (bedFootToSoldier.entrySet().removeIf(e -> e.getValue().equals(soldierId))) {
            setDirty();
        }
    }

    public Optional<BlockPos> findBedForSoldier(UUID soldierId) {
        for (var entry : bedFootToSoldier.entrySet()) {
            if (entry.getValue().equals(soldierId)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    public boolean soldierOwnsBed(UUID soldierId, @Nullable BlockPos assignedFoot, ServerLevel level) {
        if (assignedFoot == null) {
            return false;
        }
        BlockPos foot = VanillaBedHelper.footPosition(level, assignedFoot);
        if (!level.isLoaded(foot)) {
            return true;
        }
        UUID owner = getSoldierAt(foot);
        return soldierId.equals(owner) && VanillaBedHelper.isIntactBed(level, foot);
    }
}
