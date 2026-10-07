package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;
import com.villagers.mod.block.entity.RecruiterBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class RecruiterBlock extends OpenableContainerBlock {
    public static final MapCodec<RecruiterBlock> CODEC = simpleCodec(RecruiterBlock::new);

    public RecruiterBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public RecruiterBlock() {
        this(BlockBehaviour.Properties.ofFullCopy(Blocks.SMITHING_TABLE).strength(2.5f, 6f));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RecruiterBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
