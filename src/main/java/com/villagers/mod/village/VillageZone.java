package com.villagers.mod.village;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.entity.VillageMarkerBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Horizontal claim circles used for tax and other village-wide checks. */
public final class VillageZone {
    /** Minimum tax reach when no saved village claim exists at the box. */
    public static final int DEFAULT_TAX_RADIUS = 128;
    /** Extra margin when scanning for surveyor markers beyond the primary claim radius. */
    private static final int MARKER_SCAN_PADDING = 32;

    private VillageZone() {
    }

    public record Circle(int centerX, int centerZ, int radius) {
        public boolean contains(Villager villager) {
            return contains(villager.getBlockX(), villager.getBlockZ());
        }

        public boolean contains(int x, int z) {
            long dx = (long) x - centerX;
            long dz = (long) z - centerZ;
            long r = radius;
            return dx * dx + dz * dz <= r * r;
        }
    }

    /** Refreshes stored claim radius from population, then returns all circles that count for tax at {@code taxBox}. */
    public static List<Circle> taxCircles(ServerLevel level, BlockPos taxBox) {
        VillageManager manager = VillageManager.get(level);
        VillageData village = manager.findVillageAt(taxBox.getX(), taxBox.getY(), taxBox.getZ());
        if (village == null) {
            return List.of(new Circle(taxBox.getX(), taxBox.getZ(), DEFAULT_TAX_RADIUS));
        }

        VillageManager.refreshRadiusFromPopulation(level, village);
        int taxRadius = Math.max(DEFAULT_TAX_RADIUS, village.getRadius());

        List<Circle> circles = new ArrayList<>();
        circles.add(new Circle(village.getMarkerX(), village.getMarkerZ(), taxRadius));
        appendSurveyorMarkerCircles(level, taxBox, village.getOwnerId(), taxRadius, circles);
        return circles;
    }

    public static int primaryTaxRadius(ServerLevel level, BlockPos taxBox) {
        List<Circle> circles = taxCircles(level, taxBox);
        int max = DEFAULT_TAX_RADIUS;
        for (Circle circle : circles) {
            max = Math.max(max, circle.radius());
        }
        return max;
    }

    public static boolean villagerInTaxZone(Villager villager, List<Circle> circles) {
        for (Circle circle : circles) {
            if (circle.contains(villager)) {
                return true;
            }
        }
        return false;
    }

    public static boolean positionInTaxZone(int x, int z, List<Circle> circles) {
        for (Circle circle : circles) {
            if (circle.contains(x, z)) {
                return true;
            }
        }
        return false;
    }

    /** Bounding AABB half-extent for entity queries covering all tax circles. */
    public static int queryHalfExtent(List<Circle> circles) {
        int max = DEFAULT_TAX_RADIUS;
        for (Circle circle : circles) {
            max = Math.max(max, circle.radius());
        }
        return max + MARKER_SCAN_PADDING;
    }

    private static void appendSurveyorMarkerCircles(
            ServerLevel level,
            BlockPos taxBox,
            UUID ownerId,
            int taxRadius,
            List<Circle> circles) {
        int scan = taxRadius + MARKER_SCAN_PADDING;
        VillageManager manager = VillageManager.get(level);
        BlockPos min = taxBox.offset(-scan, -32, -scan);
        BlockPos max = taxBox.offset(scan, 32, scan);
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            if (!level.isLoaded(pos)) {
                continue;
            }
            if (!level.getBlockState(pos).is(VillagersMod.VILLAGE_MARKER.get())) {
                continue;
            }
            if (!markerOwnedBy(level, pos, ownerId, manager)) {
                continue;
            }
            if (alreadyHasCenter(circles, pos.getX(), pos.getZ())) {
                continue;
            }
            circles.add(new Circle(pos.getX(), pos.getZ(), taxRadius));
        }
    }

    private static boolean markerOwnedBy(
            ServerLevel level, BlockPos pos, UUID ownerId, VillageManager manager) {
        var be = level.getBlockEntity(pos);
        if (be instanceof VillageMarkerBlockEntity marker) {
            UUID markerOwner = marker.getOwnerId();
            boolean hasOwner = markerOwner.getLeastSignificantBits() != 0L
                    || markerOwner.getMostSignificantBits() != 0L;
            if (hasOwner) {
                return markerOwner.equals(ownerId);
            }
        }
        VillageData atMarker = manager.findVillageAt(pos.getX(), pos.getY(), pos.getZ());
        if (atMarker != null) {
            return atMarker.getOwnerId().equals(ownerId);
        }
        return false;
    }

    private static boolean alreadyHasCenter(List<Circle> circles, int x, int z) {
        for (Circle circle : circles) {
            if (circle.centerX() == x && circle.centerZ() == z) {
                return true;
            }
        }
        return false;
    }
}
