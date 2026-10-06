package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CaltropBlockEntity extends BlockEntity {
    private static final int MAX_HITS = 8;
    private int hits;

    public CaltropBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.CALTROP.get(), pos, state);
    }

    /** @return true if the caltrop should break */
    public boolean registerHit() {
        hits++;
        setChanged();
        return hits >= MAX_HITS;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Hits", hits);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        hits = tag.getInt("Hits");
    }
}
