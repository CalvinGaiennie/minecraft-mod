package com.villagers.mod.village;

import java.util.UUID;

/** Stable short ids shared by the muster book and village map. */
public final class VillageShortId {
    public static final String PREFIX = "V-";

    private VillageShortId() {
    }

    public static String format(VillageData village) {
        return format(village.getVillageId());
    }

    public static String format(UUID villageId) {
        String hex = villageId.toString().replace("-", "");
        return PREFIX + hex.substring(0, 8).toUpperCase();
    }
}
