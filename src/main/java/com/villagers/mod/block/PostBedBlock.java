package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;
import com.villagers.mod.block.entity.PostBedBlockEntity;
import com.villagers.mod.block.entity.VillagerBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class PostBedBlock extends BaseEntityBlock {
    public static final MapCodec<PostBedBlock> CODEC = simpleCodec(PostBedBlock::new);

    public PostBedBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public PostBedBlock() {
        this(BlockBehaviour.Properties.of()
                .strength(0.1f)
                .sound(SoundType.WOOD)
                .noOcclusion());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PostBedBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
