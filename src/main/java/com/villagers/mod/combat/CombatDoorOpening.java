package com.villagers.mod.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.entity.VillagerAttachments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Opens wooden doors and fence gates while pursuing; closes them after passing through. */
public final class CombatDoorOpening {
    /** Squared distance past the door before we close it. */
    private static final double CLOSE_AFTER_DIST_SQR = 6.25;
    private static final Map<UUID, List<GlobalPos>> doorsToClose = new HashMap<>();

    private CombatDoorOpening() {
    }

    public static void clearFor(UUID villagerId) {
        doorsToClose.remove(villagerId);
    }

    public static void tick(Villager villager) {
        if (villager.level().isClientSide || !(villager instanceof Mob mob)) {
            return;
        }
        if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())
                && !villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            return;
        }
        if (mob.getTarget() != null && mob.getTarget().isAlive()) {
            openDoorsToward(villager, mob.getTarget().position());
        }
        closeDoorsBehind(villager, mob.getTarget() == null || !mob.getTarget().isAlive());
    }

    private static void openDoorsToward(Villager villager, Vec3 toward) {
        Level level = villager.level();
        Vec3 from = villager.position();
        Vec3 step = toward.subtract(from);
        if (step.horizontalDistanceSqr() < 0.01) {
            return;
        }
        step = new Vec3(step.x, 0, step.z).normalize().scale(0.75);
        for (int i = 0; i <= 2; i++) {
            Vec3 sample = from.add(step.scale(i));
            BlockPos base = BlockPos.containing(sample);
            tryOpenAt(villager, level, base);
            tryOpenAt(villager, level, base.above());
        }
        BlockPos feet = villager.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(
                feet.offset(-1, 0, -1), feet.offset(1, 2, 1))) {
            tryOpenAt(villager, level, pos);
        }
    }

    private static void tryOpenAt(Villager villager, Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof DoorBlock door) {
            if (state.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER) {
                return;
            }
            if (!state.getValue(DoorBlock.OPEN)) {
                door.setOpen(villager, level, state, pos, true);
                rememberToClose(villager, level, pos);
            }
            return;
        }
        if (block instanceof FenceGateBlock) {
            if (!state.getValue(FenceGateBlock.OPEN)) {
                level.setBlock(pos, state.setValue(FenceGateBlock.OPEN, true), 3);
                level.playSound(null, pos, SoundEvents.FENCE_GATE_OPEN, SoundSource.BLOCKS, 1.0f, 1.0f);
                rememberToClose(villager, level, pos);
            }
        }
    }

    private static void rememberToClose(Villager villager, Level level, BlockPos pos) {
        GlobalPos global = GlobalPos.of(level.dimension(), pos.immutable());
        doorsToClose.computeIfAbsent(villager.getUUID(), id -> new ArrayList<>()).add(global);
    }

    private static void closeDoorsBehind(Villager villager, boolean chaseEnded) {
        List<GlobalPos> pending = doorsToClose.get(villager.getUUID());
        if (pending == null || pending.isEmpty()) {
            return;
        }
        Level level = villager.level();
        Iterator<GlobalPos> it = pending.iterator();
        while (it.hasNext()) {
            GlobalPos global = it.next();
            if (!global.dimension().equals(level.dimension())) {
                continue;
            }
            BlockPos pos = global.pos();
            if (!level.isLoaded(pos)) {
                continue;
            }
            boolean passed = villager.blockPosition().distSqr(pos) > CLOSE_AFTER_DIST_SQR;
            if (passed || chaseEnded) {
                tryCloseAt(villager, level, pos);
                it.remove();
            }
        }
        if (pending.isEmpty()) {
            doorsToClose.remove(villager.getUUID());
        }
    }

    private static void tryCloseAt(Villager villager, Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof DoorBlock door) {
            if (state.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER) {
                return;
            }
            if (state.getValue(DoorBlock.OPEN)) {
                door.setOpen(villager, level, state, pos, false);
            }
            return;
        }
        if (block instanceof FenceGateBlock && state.getValue(FenceGateBlock.OPEN)) {
            level.setBlock(pos, state.setValue(FenceGateBlock.OPEN, false), 3);
            level.playSound(null, pos, SoundEvents.FENCE_GATE_CLOSE, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }
}
