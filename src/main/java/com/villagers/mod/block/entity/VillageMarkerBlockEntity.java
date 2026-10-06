package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class VillageMarkerBlockEntity extends BlockEntity {
    private String villageName = "Unnamed Village";
    private UUID villageId = UUID.randomUUID();
    private UUID ownerId = new UUID(0L, 0L);

    public VillageMarkerBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.VILLAGE_MARKER.get(), pos, state);
    }

    public String getVillageName() { return villageName; }
    public void setVillageName(String name) { this.villageName = name; }

    public UUID getVillageId() { return villageId; }
    public void setVillageId(UUID id) { this.villageId = id; }

    public UUID getOwnerId() { return ownerId; }
    public void setOwnerId(UUID id) { this.ownerId = id; }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("VillageName", villageName);
        tag.putUUID("VillageId", villageId);
        tag.putUUID("OwnerId", ownerId);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("VillageName")) {
            villageName = tag.getString("VillageName");
        }
        if (tag.contains("VillageId")) {
            villageId = tag.getUUID("VillageId");
        }
        if (tag.contains("OwnerId")) {
            ownerId = tag.getUUID("OwnerId");
        }
    }
}
