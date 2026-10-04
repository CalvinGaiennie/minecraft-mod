package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class PostBedBlockEntity extends BlockEntity {
    private UUID occupant;

    public PostBedBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.POST_BED.get(), pos, state);
    }

    public boolean isFree() {
        return occupant == null;
    }

    public UUID getOccupant() {
        return occupant;
    }

    public void setOccupant(UUID occupant) {
        this.occupant = occupant;
        setChanged();
    }

    public void clearOccupant() {
        this.occupant = null;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (occupant != null) {
            tag.putUUID("Occupant", occupant);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        occupant = tag.hasUUID("Occupant") ? tag.getUUID("Occupant") : null;
    }
}
