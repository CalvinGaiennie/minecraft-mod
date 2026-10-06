package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.villagers.mod.block.entity.MobSpawnerCapBlockEntity;
import com.villagers.mod.block.entity.VillagerBlockEntities;

import org.jetbrains.annotations.Nullable;

public class MobSpawnerCapBlock extends BaseEntityBlock {
    public static final MapCodec<MobSpawnerCapBlock> CODEC = simpleCodec(MobSpawnerCapBlock::new);

    public MobSpawnerCapBlock() {
        this(net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy(Blocks.SPAWNER));
    }

    public MobSpawnerCapBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MobSpawnerCapBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            net.minecraft.world.level.Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }
        return (level1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof MobSpawnerCapBlockEntity cap && level1 instanceof ServerLevel server) {
                MobSpawnerCapBlockEntity.serverTick(server, pos, state1, cap);
            }
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
