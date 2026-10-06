package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;
import com.villagers.mod.block.entity.TaxBoxBlockEntity;
import com.villagers.mod.economy.TaxCollection;

import net.minecraft.server.level.ServerLevel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class TaxBoxBlock extends BaseEntityBlock {
    public static final MapCodec<TaxBoxBlock> CODEC = simpleCodec(TaxBoxBlock::new);

    public TaxBoxBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public TaxBoxBlock() {
        this(BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST).strength(2.5f, 6f));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TaxBoxBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected net.minecraft.world.InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel
                && level.getBlockEntity(pos) instanceof TaxBoxBlockEntity taxBox) {
            TaxCollection.processOpen(serverLevel, pos, taxBox, player);
            player.openMenu(taxBox);
        }
        return level.isClientSide ? net.minecraft.world.InteractionResult.SUCCESS : net.minecraft.world.InteractionResult.CONSUME;
    }
}
