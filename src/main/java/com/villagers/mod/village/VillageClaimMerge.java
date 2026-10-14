package com.villagers.mod.village;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Same-owner claims that touch or overlap merge into one village ({@code soldiers-and-villages.md}).
 */
public final class VillageClaimMerge {
    private static final int MAX_RADIUS = 128;

    private VillageClaimMerge() {
    }

    public static boolean circlesTouchOrOverlap(VillageData a, VillageData b) {
        double dist = Math.hypot(a.getMarkerX() - b.getMarkerX(), a.getMarkerZ() - b.getMarkerZ());
        return dist <= (double) a.getRadius() + b.getRadius();
    }

    /** New claim at {@code x,z} with {@code newRadius} touching an owned village? */
    @org.jetbrains.annotations.Nullable
    public static VillageData findOwnedClaimTouching(ServerLevel level, UUID ownerId, int x, int z, int newRadius) {
        VillageManager manager = VillageManager.get(level);
        VillageData best = null;
        long bestCreated = Long.MAX_VALUE;
        for (VillageData village : manager.getVillagesOwnedBy(ownerId)) {
            double dist = Math.hypot(x - village.getMarkerX(), z - village.getMarkerZ());
            if (dist <= (double) village.getRadius() + newRadius
                    && village.getCreatedTime() < bestCreated) {
                best = village;
                bestCreated = village.getCreatedTime();
            }
        }
        return best;
    }

    public static void expandToIncludePoint(VillageData village, BlockPos pos) {
        double dist = Math.hypot(pos.getX() - village.getMarkerX(), pos.getZ() - village.getMarkerZ());
        int need = (int) Math.ceil(dist);
        if (need > village.getRadius()) {
            village.setRadius(Math.min(MAX_RADIUS, need));
        }
    }

    /** Merges overlapping/touching hamlets for one owner; keeps oldest {@link VillageData}. */
    public static void consolidateOwnedVillages(ServerLevel level, UUID ownerId) {
        VillageManager manager = VillageManager.get(level);
        boolean merged;
        do {
            merged = false;
            List<VillageData> owned = new ArrayList<>(manager.getVillagesOwnedBy(ownerId));
            owned.sort(Comparator.comparingLong(VillageData::getCreatedTime));
            outer:
            for (int i = 0; i < owned.size(); i++) {
                VillageData a = owned.get(i);
                if (!a.isActive() || manager.getVillage(a.getVillageId()) == null) {
                    continue;
                }
                for (int j = i + 1; j < owned.size(); j++) {
                    VillageData b = owned.get(j);
                    if (!b.isActive() || manager.getVillage(b.getVillageId()) == null) {
                        continue;
                    }
                    if (circlesTouchOrOverlap(a, b)) {
                        mergePair(level, manager, a, b);
                        merged = true;
                        break outer;
                    }
                }
            }
        } while (merged);
    }

    private static void mergePair(
            ServerLevel level, VillageManager manager, VillageData primary, VillageData absorbed) {
        if (primary.getCreatedTime() > absorbed.getCreatedTime()) {
            VillageData swap = primary;
            primary = absorbed;
            absorbed = swap;
        }
        int cover = radiusCoveringBoth(primary, absorbed);
        if (cover > primary.getRadius()) {
            primary.setRadius(Math.min(MAX_RADIUS, cover));
        }
        manager.unregisterVillage(absorbed.getVillageId(), level);
        VillageManager.refreshRadiusFromPopulation(level, primary);
        VillageSavedData.get(level).markDirty();
    }

    private static int radiusCoveringBoth(VillageData primary, VillageData other) {
        double dist = Math.hypot(primary.getMarkerX() - other.getMarkerX(), primary.getMarkerZ() - other.getMarkerZ());
        int fromPrimary = (int) Math.ceil(dist) + other.getRadius();
        return Math.max(primary.getRadius(), fromPrimary);
    }
}
