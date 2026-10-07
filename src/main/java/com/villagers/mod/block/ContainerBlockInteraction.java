package com.villagers.mod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

/** Opens {@link MenuProvider} block entities (same pattern as vanilla barrel / tax box). */
public final class ContainerBlockInteraction {
    private ContainerBlockInteraction() {
    }

    public static InteractionResult openMenu(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MenuProvider menu) {
            player.openMenu(menu);
        }
        return InteractionResult.CONSUME;
    }

    public static void ensureBlockEntity(
            Level level,
            BlockPos pos,
            BlockState state,
            BlockEntityFactory factory) {
        if (level.isClientSide || !(state.getBlock() instanceof BaseEntityBlock)) {
            return;
        }
        BlockEntity existing = level.getBlockEntity(pos);
        if (existing != null) {
            return;
        }
        BlockEntity created = factory.create(pos, state);
        if (created != null) {
            level.setBlockEntity(created);
        }
    }

    @FunctionalInterface
    public interface BlockEntityFactory {
        @Nullable
        BlockEntity create(BlockPos pos, BlockState state);
    }
}
