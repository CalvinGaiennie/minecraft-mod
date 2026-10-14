package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import com.villagers.mod.war.WantedPosterSavedData;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WantedPosterBlockEntity extends BlockEntity {
    public static final int MAX_NAMES = 4;
    private UUID ownerId = new UUID(0L, 0L);
    @Nullable
    private UUID villageId;
    private final List<UUID> wantedIds = new ArrayList<>();

    public WantedPosterBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.WANTED_POSTER.get(), pos, state);
    }

    public void setOwnerAndVillage(UUID ownerId, UUID villageId) {
        this.ownerId = ownerId;
        this.villageId = villageId;
        setChanged();
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    @Nullable
    public UUID getVillageId() {
        return villageId;
    }

    public List<UUID> getWantedIds() {
        return List.copyOf(wantedIds);
    }

    public void setWantedFromResolved(List<UUID> ids) {
        wantedIds.clear();
        for (UUID id : ids) {
            if (wantedIds.size() >= MAX_NAMES) {
                break;
            }
            if (id != null && !ownerId.equals(id)) {
                wantedIds.add(id);
            }
        }
        syncIndex();
        setChanged();
    }

    private void syncIndex() {
        if (!(level instanceof ServerLevel server) || villageId == null) {
            return;
        }
        WantedPosterSavedData.get(server).setPoster(villageId, worldPosition, new HashSet<>(wantedIds));
    }

    public void clearFromIndex() {
        if (level instanceof ServerLevel server && villageId != null) {
            WantedPosterSavedData.get(server).removePoster(villageId, worldPosition);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putUUID("Owner", ownerId);
        if (villageId != null) {
            tag.putUUID("Village", villageId);
        }
        ListTag list = new ListTag();
        for (UUID id : wantedIds) {
            list.add(net.minecraft.nbt.NbtUtils.createUUID(id));
        }
        tag.put("Wanted", list);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ownerId = tag.hasUUID("Owner") ? tag.getUUID("Owner") : new UUID(0L, 0L);
        villageId = tag.hasUUID("Village") ? tag.getUUID("Village") : null;
        wantedIds.clear();
        ListTag list = tag.getList("Wanted", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size() && wantedIds.size() < MAX_NAMES; i++) {
            wantedIds.add(net.minecraft.nbt.NbtUtils.loadUUID(list.getCompound(i)));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            syncIndex();
        }
    }
}
