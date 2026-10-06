package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;
import com.villagers.mod.block.entity.SupplyDepotBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class SupplyDepotBlock extends OpenableContainerBlock {
    public static final MapCodec<SupplyDepotBlock> CODEC = simpleCodec(SupplyDepotBlock::new);

    public SupplyDepotBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public SupplyDepotBlock() {
        this(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE).strength(2.5f, 6f));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SupplyDepotBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
