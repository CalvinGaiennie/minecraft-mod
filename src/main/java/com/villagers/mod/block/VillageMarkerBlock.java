package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionHand;

import com.villagers.mod.block.entity.VillageMarkerBlockEntity;
import com.villagers.mod.block.entity.VillagerBlockEntities;

import org.jetbrains.annotations.Nullable;

public class VillageMarkerBlock extends BaseEntityBlock {
    public static final MapCodec<VillageMarkerBlock> CODEC = simpleCodec(VillageMarkerBlock::new);

    public VillageMarkerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public VillageMarkerBlock() {
        this(BlockBehaviour.Properties.of()
                .strength(2.0f)
                .sound(SoundType.STONE)
                .noOcclusion());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VillageMarkerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
