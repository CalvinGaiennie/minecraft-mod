package com.villagers.mod.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.RecruiterBlock;
import com.villagers.mod.block.entity.ArrowBinBlockEntity;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;
import com.villagers.mod.util.SoldierStructureHelper;

public final class ArrowResupply {
    private static final int SCAN = 32;

    private ArrowResupply() {
    }

    public static void tryResupply(Villager soldier) {
        if (soldier.level().isClientSide || !(soldier.level() instanceof ServerLevel level)) {
            return;
        }
        ItemStack ranged = VillagerGearRules.get(soldier, VillagerGearSlot.RANGED);
        if (ranged.isEmpty() || (!ranged.is(Items.BOW) && !ranged.is(Items.CROSSBOW))) {
            return;
        }
        ItemStack arrows = VillagerGearRules.get(soldier, VillagerGearSlot.ARROWS);
        if (!arrows.isEmpty() && arrows.getCount() >= VillagerGearRules.MAX_ARROWS) {
            return;
        }
        int need = VillagerGearRules.MAX_ARROWS - (arrows.isEmpty() ? 0 : arrows.getCount());
        BlockPos origin = soldier.blockPosition();
        BlockPos nearest = null;
        double best = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN, -5, -SCAN), origin.offset(SCAN, 5, SCAN))) {
            if (!canResupplyFrom(level, pos)) {
                continue;
            }
            double dist = origin.distSqr(pos);
            if (dist < best) {
                best = dist;
                nearest = pos.immutable();
            }
        }
        if (nearest == null) {
            return;
        }
        int pulled = pullArrows(level, nearest, need);
        if (pulled <= 0) {
            return;
        }
        ItemStack updated = arrows.isEmpty() ? new ItemStack(Items.ARROW, pulled) : arrows.copy();
        if (!arrows.isEmpty()) {
            updated.setCount(arrows.getCount() + pulled);
        }
        VillagerGearRules.set(soldier, VillagerGearSlot.ARROWS, updated);
    }

    private static boolean canResupplyFrom(ServerLevel level, BlockPos pos) {
        if (level.getBlockState(pos).is(VillagersMod.ARROW_BIN.get())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ArrowBinBlockEntity bin) {
                for (int i = 0; i < bin.getContainerSize(); i++) {
                    if (bin.getItem(i).is(Items.ARROW)) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (level.getBlockState(pos).getBlock() instanceof RecruiterBlock) {
            BlockEntity be = level.getBlockEntity(pos);
            return be instanceof RecruiterBlockEntity recruiter && recruiter.hasArrows();
        }
        return false;
    }

    private static int pullArrows(ServerLevel level, BlockPos pos, int max) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof RecruiterBlockEntity recruiter) {
            return recruiter.withdrawArrows(max);
        }
        if (be instanceof ArrowBinBlockEntity bin) {
            int taken = 0;
            for (int i = 0; i < bin.getContainerSize() && taken < max; i++) {
                ItemStack slot = bin.getItem(i);
                if (!slot.is(Items.ARROW)) {
                    continue;
                }
                int pull = Math.min(max - taken, slot.getCount());
                slot.shrink(pull);
                if (slot.isEmpty()) {
                    bin.setItem(i, ItemStack.EMPTY);
                }
                taken += pull;
            }
            if (taken > 0) {
                bin.setChanged();
            }
            return taken;
        }
        return 0;
    }

    /** Guard duty pathing: nearest bin or recruiter with arrows within structure scan range. */
    public static java.util.Optional<BlockPos> nearestResupplyPos(ServerLevel level, BlockPos origin) {
        return SoldierStructureHelper.findNearestArrowResupply(level, origin);
    }
}
