package com.villagers.mod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Soldiers path to tagged levers/buttons and use them on horn blasts ({@code war-and-defense.md}). */
public final class SoldierRedstoneInteract {
    private static final long TASK_TICKS = 600L;
    private static final double USE_DISTANCE_SQ = 2.5 * 2.5;

    private SoldierRedstoneInteract() {
    }

    public static int dispatchForLeverHorn(
            ServerPlayer player,
            @Nullable net.minecraft.world.item.DyeColor color,
            boolean randomOnly,
            List<BlockPos> targets) {
        if (targets.isEmpty()) {
            return 0;
        }
        if (randomOnly) {
            ServerLevel level = player.serverLevel();
            targets = List.of(targets.get(level.random.nextInt(targets.size())));
        }
        return assignSoldiers(player, targets);
    }

    public static void dispatchOnCommandHorn(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        List<BlockPos> targets = collectTargets(level, player.blockPosition(), null);
        assignSoldiers(player, targets);
    }

    private static int assignSoldiers(ServerPlayer player, List<BlockPos> targets) {
        ServerLevel level = player.serverLevel();
        Set<BlockPos> claimed = new HashSet<>();
        int count = 0;
        for (BlockPos target : targets) {
            Villager soldier = findNearestIdleSoldier(level, player, target, claimed);
            if (soldier == null) {
                continue;
            }
            claimed.add(target);
            SoldierData data = soldier.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data == null) {
                continue;
            }
            data.setRedstoneTargetPos(target);
            data.setRedstoneTargetUntil(level.getGameTime() + TASK_TICKS);
            if (soldier instanceof Mob mob) {
                mob.getNavigation().moveTo(
                        target.getX() + 0.5,
                        target.getY(),
                        target.getZ() + 0.5,
                        1.1);
            }
            count++;
        }
        return count;
    }

    public static void tick(ServerLevel level, Villager villager, SoldierData data) {
        BlockPos target = data.getRedstoneTargetPos();
        if (target == null || level.getGameTime() > data.getRedstoneTargetUntil()) {
            data.clearRedstoneTarget();
            return;
        }
        if (!(villager instanceof Mob mob)) {
            data.clearRedstoneTarget();
            return;
        }
        Vec3 center = Vec3.atCenterOf(target);
        if (villager.distanceToSqr(center) > USE_DISTANCE_SQ) {
            if (mob.getNavigation().isDone()) {
                mob.getNavigation().moveTo(center.x, center.y, center.z, 1.1);
            }
            return;
        }
        useBlock(level, target);
        data.clearRedstoneTarget();
        mob.getNavigation().stop();
    }

    private static void useBlock(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof LeverBlock) {
            boolean powered = state.getValue(LeverBlock.POWERED);
            BlockState next = state.setValue(LeverBlock.POWERED, !powered);
            level.setBlock(pos, next, 3);
            level.updateNeighborsAt(pos, state.getBlock());
            level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3f, powered ? 0.5f : 0.6f);
            return;
        }
        if (state.getBlock() instanceof ButtonBlock button) {
            if (!state.getValue(BlockStateProperties.POWERED)) {
                level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), 3);
                level.updateNeighborsAt(pos, button);
                level.scheduleTick(pos, button, 20);
                level.playSound(null, pos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.3f, 0.6f);
            }
        }
    }

    private static List<BlockPos> collectTargets(
            ServerLevel level, BlockPos origin, @Nullable net.minecraft.world.item.DyeColor color) {
        LeverColorSavedData colors = LeverColorSavedData.get(level);
        List<BlockPos> out = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-HornCommandService.HORN_RADIUS, -16, -HornCommandService.HORN_RADIUS),
                origin.offset(HornCommandService.HORN_RADIUS, 16, HornCommandService.HORN_RADIUS))) {
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof LeverBlock) && !(state.getBlock() instanceof ButtonBlock)) {
                continue;
            }
            net.minecraft.world.item.DyeColor at = colors.getColor(pos);
            if (at == null) {
                continue;
            }
            if (color != null && at != color) {
                continue;
            }
            out.add(pos.immutable());
        }
        return out;
    }

    @Nullable
    private static Villager findNearestIdleSoldier(
            ServerLevel level, ServerPlayer player, BlockPos target, Set<BlockPos> claimedTargets) {
        Villager best = null;
        double bestDist = Double.MAX_VALUE;
        for (Villager villager : level.getEntitiesOfClass(
                Villager.class, player.getBoundingBox().inflate(HornCommandService.HORN_RADIUS))) {
            if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                continue;
            }
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data == null || data.getRedstoneTargetPos() != null) {
                continue;
            }
            if (!ownsSoldier(level, player, villager)) {
                continue;
            }
            double dist = villager.distanceToSqr(Vec3.atCenterOf(target));
            if (dist < bestDist) {
                bestDist = dist;
                best = villager;
            }
        }
        return best;
    }

    private static boolean ownsSoldier(ServerLevel level, ServerPlayer player, Villager villager) {
        UUID ownerId = HornCommandService.resolveSoldierOwner(level, villager);
        return ownerId != null && HornCommandService.canCommandSoldiers(player, ownerId);
    }
}
