package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;
import com.villagers.mod.block.entity.MessStationBlockEntity;
import com.villagers.mod.entity.SoldierConversion;
import com.villagers.mod.util.SoldierStructureHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.Containers;

import org.jetbrains.annotations.Nullable;

public class MessStationBlock extends BaseEntityBlock {
    public static final MapCodec<MessStationBlock> CODEC = simpleCodec(MessStationBlock::new);

    public MessStationBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public MessStationBlock() {
        this(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).strength(2.5f, 6f));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MessStationBlockEntity(pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        ensureBlockEntity(level, pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult) {
        if (!player.isShiftKeyDown() && MessStationBlockEntity.isSoldierFood(stack)) {
            ensureBlockEntity(level, pos, state);
            if (!level.isClientSide) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof MessStationBlockEntity mess && mess.tryInsertOne(stack)) {
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    stack.consume(1, player);
                    SoldierConversion.tryConvertNear(level, pos, SoldierStructureHelper.SCAN_RADIUS);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        ensureBlockEntity(level, pos, state);
        return ContainerBlockInteraction.openMenu(level, pos, player).consumesAction()
                ? ItemInteractionResult.sidedSuccess(level.isClientSide)
                : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        ensureBlockEntity(level, pos, state);
        return ContainerBlockInteraction.openMenu(level, pos, player);
    }

    private static void ensureBlockEntity(Level level, BlockPos pos, BlockState state) {
        ContainerBlockInteraction.ensureBlockEntity(level, pos, state, MessStationBlockEntity::new);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MessStationBlockEntity mess) {
                Containers.dropContents(level, pos, mess);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
