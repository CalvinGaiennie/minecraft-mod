package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PostBedBlockEntity extends BlockEntity {
    public PostBedBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.POST_BED.get(), pos, state);
    }
}
