package com.villagers.mod.village;

import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VillageManager {
    public static final int HAMLET_RADIUS = 32;

    /** Claim radius tiers from village population (matches surveyor design doc). */
    public static int radiusForPopulation(int memberCount) {
        if (memberCount <= 5) {
            return 32;
        }
        if (memberCount <= 10) {
            return 48;
        }
        if (memberCount <= 20) {
            return 64;
        }
        if (memberCount <= 35) {
            return 96;
        }
        return 128;
    }

    private final Map<UUID, VillageData> villages;

    private VillageManager(Map<UUID, VillageData> villages) {
        this.villages = villages;
    }

    public static VillageManager get(ServerLevel level) {
        VillageSavedData saved = VillageSavedData.get(level);
        return new VillageManager(saved.villages());
    }

    public static void clearAllForTests() {
        // Tests use in-memory only via direct map clear on a throwaway manager is not used;
        // GameTests call register on get() which mutates SavedData in level — tests use helper.
    }

    /** Clears claims in the given level (GameTests). */
    public static void clearForLevel(ServerLevel level) {
        VillageSavedData.get(level).villages().clear();
        VillageSavedData.get(level).markDirty();
    }

    public void registerVillage(VillageData village, ServerLevel level) {
        villages.put(village.getVillageId(), village);
        VillageSavedData.get(level).markDirty();
    }

    public void unregisterVillage(UUID villageId, ServerLevel level) {
        villages.remove(villageId);
        VillageSavedData.get(level).markDirty();
    }

    public VillageData getVillage(UUID villageId) {
        return villages.get(villageId);
    }

    public VillageData findVillageAt(int x, int y, int z) {
        for (VillageData village : villages.values()) {
            if (!village.isActive()) {
                continue;
            }
            int dx = x - village.getMarkerX();
            int dz = z - village.getMarkerZ();
            double distance = Math.sqrt(dx * dx + dz * dz);
            if (distance <= village.getRadius()) {
                return village;
            }
        }
        return null;
    }

    public boolean canClaim(UUID ownerId, int x, int z, int radius) {
        for (VillageData village : villages.values()) {
            if (!village.isActive()) {
                continue;
            }
            if (ownerId.equals(village.getOwnerId())) {
                continue;
            }
            double distance = Math.hypot(x - village.getMarkerX(), z - village.getMarkerZ());
            if (distance < village.getRadius() + radius) {
                return false;
            }
        }
        return true;
    }

    public Map<UUID, VillageData> getAllVillages() {
        return new HashMap<>(villages);
    }

    public List<VillageData> getVillagesOwnedBy(UUID ownerId) {
        List<VillageData> owned = new ArrayList<>();
        for (VillageData village : villages.values()) {
            if (village.isActive() && ownerId.equals(village.getOwnerId())) {
                owned.add(village);
            }
        }
        return owned;
    }

    /** Counts villagers (any role) inside the village's current horizontal claim. */
    public static int countMembersInVillage(ServerLevel level, VillageData village) {
        int countRadius = Math.max(village.getRadius(), VillageZone.DEFAULT_TAX_RADIUS);
        long radiusSq = (long) countRadius * countRadius;
        int centerX = village.getMarkerX();
        int centerZ = village.getMarkerZ();
        int count = 0;
        int y = village.getMarkerY();
        int vertical = 128;
        for (var entity : level.getEntitiesOfClass(
                net.minecraft.world.entity.npc.Villager.class,
                new net.minecraft.world.phys.AABB(
                        centerX - countRadius,
                        y - vertical,
                        centerZ - countRadius,
                        centerX + countRadius,
                        y + vertical,
                        centerZ + countRadius))) {
            long dx = entity.getBlockX() - centerX;
            long dz = entity.getBlockZ() - centerZ;
            if (dx * dx + dz * dz <= radiusSq) {
                count++;
            }
        }
        return count;
    }

    /** Grows (never shrinks) saved claim radius from current member count. */
    public static void refreshRadiusFromPopulation(ServerLevel level, VillageData village) {
        int members = countMembersInVillage(level, village);
        int tier = radiusForPopulation(members);
        if (tier > village.getRadius()) {
            village.setRadius(tier);
            VillageSavedData.get(level).markDirty();
        }
    }
}
