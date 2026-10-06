package com.villagers.mod.economy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.villagers.mod.block.RecruiterBlock;
import com.villagers.mod.block.entity.RecruiterBlockEntity;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/** Routes issued gear from supply depots into nearby recruiter boxes. */
public final class SupplyRouting {
    /** Same horizontal reach as {@link com.villagers.mod.event.EconomyTickHandler} economy ticks. */
    public static final int VILLAGE_SCAN = 48;

    private SupplyRouting() {
    }

    public static Optional<RecruiterBlockEntity> findNearestRecruiterBox(Level level, BlockPos origin) {
        RecruiterBlockEntity nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-VILLAGE_SCAN, -8, -VILLAGE_SCAN),
                origin.offset(VILLAGE_SCAN, 8, VILLAGE_SCAN))) {
            if (!(level.getBlockState(pos).getBlock() instanceof RecruiterBlock)) {
                continue;
            }
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof RecruiterBlockEntity recruiter)) {
                continue;
            }
            double dist = origin.distSqr(pos);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = recruiter;
            }
        }
        return Optional.ofNullable(nearest);
    }

    /** Inserts into the nearest recruiter box, or the given fallback depot slots if none exists. */
    public static void depositWeeklyGear(Level level, BlockPos depotPos, IssuedGearSink fallback) {
        RecruiterBlockEntity recruiter = findNearestRecruiterBox(level, depotPos).orElse(null);
        for (var stack : weeklyGearStacks()) {
            insertOne(stack, recruiter, fallback);
        }
    }

    private static net.minecraft.world.item.ItemStack[] weeklyGearStacks() {
        return new net.minecraft.world.item.ItemStack[] {
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_SWORD),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.BOW),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_CHESTPLATE),
        };
    }

    private static boolean insertOne(
            net.minecraft.world.item.ItemStack stack,
            @Nullable RecruiterBlockEntity recruiter,
            IssuedGearSink fallback) {
        if (recruiter != null && recruiter.tryInsertIssuedGear(stack)) {
            recruiter.setChanged();
            return true;
        }
        return fallback.tryInsert(stack);
    }

    @FunctionalInterface
    public interface IssuedGearSink {
        boolean tryInsert(net.minecraft.world.item.ItemStack stack);
    }
}
