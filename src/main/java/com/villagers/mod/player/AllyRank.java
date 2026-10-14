package com.villagers.mod.player;

public enum AllyRank {
    FRIEND,
    OFFICER,
    CO_OWNER;

    public boolean canUseHornsAndFlares() {
        return ordinal() >= OFFICER.ordinal();
    }

    public boolean canManageVillageOps() {
        return this == CO_OWNER;
    }

    public static AllyRank fromString(String raw) {
        if (raw == null) {
            return FRIEND;
        }
        return switch (raw.toLowerCase()) {
            case "officer" -> OFFICER;
            case "co-owner", "coowner", "co_owner" -> CO_OWNER;
            default -> FRIEND;
        };
    }
}
