package com.villagers.mod.village;

import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VillageManager {
    private static final Map<UUID, VillageData> villages = new HashMap<>();
    private static VillageManager instance;

    private VillageManager() {
    }

    public static VillageManager get(ServerLevel level) {
        if (instance == null) {
            instance = new VillageManager();
        }
        return instance;
    }

    public void registerVillage(VillageData village) {
        villages.put(village.getVillageId(), village);
    }

    public void unregisterVillage(UUID villageId) {
        villages.remove(villageId);
    }

    public VillageData getVillage(UUID villageId) {
        return villages.get(villageId);
    }

    public VillageData findVillageAt(int x, int y, int z) {
        for (VillageData village : villages.values()) {
            if (!village.isActive()) continue;

            int dx = x - village.getMarkerX();
            int dz = z - village.getMarkerZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance <= village.getRadius()) {
                return village;
            }
        }
        return null;
    }

    public Map<UUID, VillageData> getAllVillages() {
        return new HashMap<>(villages);
    }
}
