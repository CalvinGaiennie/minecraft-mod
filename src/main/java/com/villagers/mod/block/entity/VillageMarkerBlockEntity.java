package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class VillageMarkerBlockEntity extends BlockEntity {
    private String villageName = "Unnamed Village";
    private UUID villageId = UUID.randomUUID();

    public VillageMarkerBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.VILLAGE_MARKER.get(), pos, state);
    }

    public String getVillageName() { return villageName; }
    public void setVillageName(String name) { this.villageName = name; }

    public UUID getVillageId() { return villageId; }
    public void setVillageId(UUID id) { this.villageId = id; }
}
