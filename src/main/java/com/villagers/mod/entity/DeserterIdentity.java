package com.villagers.mod.entity;

/** Soldier name/kills captured before desert outcome rolls. */
public record DeserterIdentity(String personalName, int kills) {
    public static final DeserterIdentity EMPTY = new DeserterIdentity("", 0);

    public static DeserterIdentity fromSoldier(SoldierData data) {
        if (data == null) {
            return EMPTY;
        }
        return new DeserterIdentity(data.getDisplayName() != null ? data.getDisplayName() : "", data.getKills());
    }
}
