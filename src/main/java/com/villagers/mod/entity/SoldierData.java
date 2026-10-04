package com.villagers.mod.entity;

import java.util.UUID;

public class SoldierData {
    private UUID soldierUUID;
    private String rank = "Soldier";
    private int kills = 0;
    private String displayName;

    public SoldierData(UUID soldierUUID) {
        this.soldierUUID = soldierUUID;
        this.displayName = "Soldier";
    }

    public UUID getSoldierUUID() { return soldierUUID; }
    public void setSoldierUUID(UUID uuid) { this.soldierUUID = uuid; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public int getKills() { return kills; }
    public void setKills(int kills) { this.kills = kills; }
    public void addKill() { this.kills++; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String name) { this.displayName = name; }
}
