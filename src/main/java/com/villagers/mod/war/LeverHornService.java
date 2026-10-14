package com.villagers.mod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class LeverHornService {
    public static final DyeColor[] HORN_COLORS = {
            DyeColor.WHITE,
            DyeColor.ORANGE,
            DyeColor.MAGENTA,
            DyeColor.LIGHT_BLUE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.PINK,
            DyeColor.CYAN
    };

    private LeverHornService() {
    }

    public static int blow(ServerPlayer player, DyeColor color, boolean randomOnly) {
        ServerLevel level = player.serverLevel();
        List<BlockPos> matches = new ArrayList<>();
        BlockPos origin = player.blockPosition();
        LeverColorSavedData colors = LeverColorSavedData.get(level);
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-HornCommandService.HORN_RADIUS, -16, -HornCommandService.HORN_RADIUS),
                origin.offset(HornCommandService.HORN_RADIUS, 16, HornCommandService.HORN_RADIUS))) {
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof LeverBlock) && !(state.getBlock() instanceof ButtonBlock)) {
                continue;
            }
            DyeColor at = colors.getColor(pos);
            if (at == color) {
                matches.add(pos.immutable());
            }
        }
        if (matches.isEmpty()) {
            return 0;
        }
        List<BlockPos> targets = matches;
        if (randomOnly) {
            targets = List.of(matches.get(ThreadLocalRandom.current().nextInt(matches.size())));
        }
        int dispatched = SoldierRedstoneInteract.dispatchForLeverHorn(player, color, false, targets);
        if (dispatched == 0) {
            int pulled = 0;
            for (BlockPos pos : targets) {
                if (activateTaggedBlock(level, pos)) {
                    pulled++;
                }
            }
            return pulled;
        }
        return dispatched;
    }

    private static boolean activateTaggedBlock(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof LeverBlock) {
            boolean powered = state.getValue(LeverBlock.POWERED);
            BlockState next = state.setValue(LeverBlock.POWERED, !powered);
            level.setBlock(pos, next, 3);
            level.updateNeighborsAt(pos, state.getBlock());
            level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3f, powered ? 0.5f : 0.6f);
            return true;
        }
        if (state.getBlock() instanceof ButtonBlock button) {
            if (!state.getValue(BlockStateProperties.POWERED)) {
                level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), 3);
                level.updateNeighborsAt(pos, button);
                level.scheduleTick(pos, button, 20);
                level.playSound(null, pos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.3f, 0.6f);
                return true;
            }
        }
        return false;
    }
}
