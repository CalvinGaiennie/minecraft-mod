package com.villagers.mod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.villagers.mod.block.MessStationBlock;
import com.villagers.mod.block.PostBedBlock;
import com.villagers.mod.block.RampartBlock;
import com.villagers.mod.block.entity.MessStationBlockEntity;
import com.villagers.mod.block.entity.PostBedBlockEntity;

import java.util.Optional;
import java.util.UUID;

public final class SoldierStructureHelper {
    private static final int SCAN_RADIUS = 16;

    private SoldierStructureHelper() {
    }

    public static Optional<BlockPos> findFreePostBed(Level level, BlockPos origin) {
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN_RADIUS, -5, -SCAN_RADIUS),
                origin.offset(SCAN_RADIUS, 5, SCAN_RADIUS))) {
            if (level.getBlockState(pos).getBlock() instanceof PostBedBlock) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof PostBedBlockEntity bed && bed.isFree()) {
                    return Optional.of(pos.immutable());
                }
            }
        }
        return Optional.empty();
    }

    public static boolean hasStockedMessStation(Level level, BlockPos origin) {
        return findStockedMessStation(level, origin).isPresent();
    }

    public static Optional<BlockPos> findStockedMessStation(Level level, BlockPos origin) {
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN_RADIUS, -5, -SCAN_RADIUS),
                origin.offset(SCAN_RADIUS, 5, SCAN_RADIUS))) {
            if (level.getBlockState(pos).getBlock() instanceof MessStationBlock) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof MessStationBlockEntity mess && mess.hasSoldierFood()) {
                    return Optional.of(pos.immutable());
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<BlockPos> findNearestRampart(Level level, BlockPos origin) {
        BlockPos nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN_RADIUS, -5, -SCAN_RADIUS),
                origin.offset(SCAN_RADIUS, 5, SCAN_RADIUS))) {
            if (level.getBlockState(pos).getBlock() instanceof RampartBlock) {
                double dist = origin.distSqr(pos);
                if (dist < nearestDist) {
                    nearestDist = dist;
                    nearest = pos.immutable();
                }
            }
        }
        return Optional.ofNullable(nearest);
    }

    public static void claimPostBed(Level level, BlockPos bedPos, UUID soldierId) {
        BlockEntity be = level.getBlockEntity(bedPos);
        if (be instanceof PostBedBlockEntity bed) {
            bed.setOccupant(soldierId);
            bed.setChanged();
        }
    }

    public static void releasePostBedForSoldier(Level level, Villager soldier) {
        BlockPos origin = soldier.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN_RADIUS, -5, -SCAN_RADIUS),
                origin.offset(SCAN_RADIUS, 5, SCAN_RADIUS))) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PostBedBlockEntity bed && soldier.getUUID().equals(bed.getOccupant())) {
                bed.clearOccupant();
                bed.setChanged();
                return;
            }
        }
    }

    public static boolean soldierHasAssignedBed(Level level, UUID soldierId, BlockPos origin) {
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN_RADIUS, -5, -SCAN_RADIUS),
                origin.offset(SCAN_RADIUS, 5, SCAN_RADIUS))) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PostBedBlockEntity bed && soldierId.equals(bed.getOccupant())) {
                return true;
            }
        }
        return false;
    }
}
