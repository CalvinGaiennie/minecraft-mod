package com.villagers.mod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.server.level.ServerLevel;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.CampBlock;
import com.villagers.mod.block.FallbackBlock;
import com.villagers.mod.block.HavenBlock;
import com.villagers.mod.block.MessStationBlock;
import com.villagers.mod.block.PostBlock;
import com.villagers.mod.combat.RampartPathing;
import com.villagers.mod.block.RecruiterBlock;
import com.villagers.mod.block.RampartBlock;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.block.RoyalGuardPostBlock;
import com.villagers.mod.block.entity.MessStationBlockEntity;
import com.villagers.mod.bed.SoldierBedClaims;
import com.villagers.mod.bed.VanillaBedHelper;
import com.villagers.mod.bed.VillagerBedOccupancy;
import com.villagers.mod.entity.SoldierData;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class SoldierStructureHelper {
    /** Must stay below GameTest structure spacing (~30) to avoid cross-test bleed in CI. */
    public static final int SCAN_RADIUS = 24;

    private SoldierStructureHelper() {
    }

    public static Optional<BlockPos> findFreeSoldierBed(Level level, BlockPos origin, Villager enlistee) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return Optional.empty();
        }
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        UUID enlistId = enlistee.getUUID();
        SoldierBedClaims claims = SoldierBedClaims.get(serverLevel);

        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN_RADIUS, -5, -SCAN_RADIUS),
                origin.offset(SCAN_RADIUS, 5, SCAN_RADIUS))) {
            if (!VanillaBedHelper.isBed(level.getBlockState(pos))) {
                continue;
            }
            BlockPos foot = VanillaBedHelper.footPosition(level, pos);
            if (!VanillaBedHelper.isIntactBed(level, foot)) {
                continue;
            }
            if (claims.isClaimedByOther(foot, enlistId)) {
                continue;
            }
            if (VillagerBedOccupancy.isHomeOfOtherVillager(level, foot, enlistId)) {
                continue;
            }
            double dist = origin.distSqr(foot);
            if (dist < bestDist) {
                bestDist = dist;
                best = foot.immutable();
            }
        }
        return Optional.ofNullable(best);
    }

    /** Post blocks mark where recruiters should place beds — not beds themselves. */
    public static boolean hasPostBlockNear(Level level, BlockPos origin) {
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN_RADIUS, -5, -SCAN_RADIUS),
                origin.offset(SCAN_RADIUS, 5, SCAN_RADIUS))) {
            if (level.getBlockState(pos).getBlock() instanceof PostBlock) {
                return true;
            }
        }
        return false;
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

    public static Optional<BlockPos> findNearestArrowBin(Level level, BlockPos origin) {
        return findNearestArrowResupply(level, origin);
    }

    /** Nearest arrow bin or recruiter box that currently holds arrows. */
    public static Optional<BlockPos> findNearestArrowResupply(Level level, BlockPos origin) {
        BlockPos nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN_RADIUS, -5, -SCAN_RADIUS),
                origin.offset(SCAN_RADIUS, 5, SCAN_RADIUS))) {
            if (!hasArrowResupplyAt(level, pos)) {
                continue;
            }
            double dist = origin.distSqr(pos);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = pos.immutable();
            }
        }
        return Optional.ofNullable(nearest);
    }

    private static boolean hasArrowResupplyAt(Level level, BlockPos pos) {
        if (level.getBlockState(pos).is(VillagersMod.ARROW_BIN.get())) {
            return true;
        }
        if (level.getBlockState(pos).getBlock() instanceof RecruiterBlock) {
            BlockEntity be = level.getBlockEntity(pos);
            return be instanceof RecruiterBlockEntity recruiter && recruiter.hasArrows();
        }
        return false;
    }

    public static Optional<BlockPos> findNearestRampart(Level level, BlockPos origin) {
        return findNearestRampart(level, origin, SCAN_RADIUS);
    }

    public static Optional<BlockPos> findNearestRampart(Level level, BlockPos origin, int radius) {
        BlockPos nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-radius, -RampartPathing.Y_BELOW, -radius),
                origin.offset(radius, RampartPathing.Y_ABOVE, radius))) {
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

    public static void claimSoldierBed(ServerLevel level, BlockPos bedFoot, UUID soldierId) {
        BlockPos foot = VanillaBedHelper.footPosition(level, bedFoot);
        SoldierBedClaims.get(level).claim(foot, soldierId);
    }

    /** Backfill {@link SoldierData#getAssignedPostBed()} when only the claim map has the bed. */
    public static void ensureAssignedBedCached(ServerLevel level, UUID soldierId, SoldierData data, BlockPos searchOrigin) {
        if (data.getAssignedPostBed() != null) {
            return;
        }
        SoldierBedClaims.get(level).findBedForSoldier(soldierId).ifPresent(data::setAssignedPostBed);
    }

    public static boolean soldierOwnsAssignedBed(ServerLevel level, UUID soldierId, @Nullable BlockPos assignedBed) {
        return SoldierBedClaims.get(level).soldierOwnsBed(soldierId, assignedBed, level);
    }

    public static void releaseSoldierBed(ServerLevel level, UUID soldierId, @Nullable BlockPos assignedBed) {
        SoldierBedClaims claims = SoldierBedClaims.get(level);
        if (assignedBed != null) {
            claims.releaseAt(VanillaBedHelper.footPosition(level, assignedBed));
        }
        claims.releaseSoldier(soldierId);
    }

    /** Civilian raid shelter — not used for soldier/militia post return (see {@link com.villagers.mod.entity.PostReturnPathing}). */
    public static Optional<BlockPos> findNearestHaven(Level level, BlockPos origin) {
        return findNearestBlock(level, origin, HavenBlock.class);
    }

    public static Optional<BlockPos> findNearestCamp(Level level, BlockPos origin) {
        return findNearestBlock(level, origin, CampBlock.class);
    }

    public static Optional<BlockPos> findNearestRoyalGuardPost(Level level, BlockPos origin) {
        return findNearestBlock(level, origin, RoyalGuardPostBlock.class);
    }

    public static Optional<BlockPos> findNearestFallback(Level level, BlockPos origin) {
        return findNearestBlock(level, origin, FallbackBlock.class);
    }

    private static Optional<BlockPos> findNearestBlock(Level level, BlockPos origin, Class<? extends Block> type) {
        BlockPos nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN_RADIUS, -5, -SCAN_RADIUS),
                origin.offset(SCAN_RADIUS, 5, SCAN_RADIUS))) {
            if (!type.isInstance(level.getBlockState(pos).getBlock())) {
                continue;
            }
            double dist = origin.distSqr(pos);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = pos.immutable();
            }
        }
        return Optional.ofNullable(nearest);
    }

}
