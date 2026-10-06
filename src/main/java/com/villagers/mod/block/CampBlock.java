package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;
import com.villagers.mod.block.entity.CampBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class CampBlock extends OpenableContainerBlock {
    public static final MapCodec<CampBlock> CODEC = simpleCodec(CampBlock::new);

    public CampBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public CampBlock() {
        this(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).strength(2.0f, 6.0f));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CampBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
