package com.villagers.mod.entity;

import java.util.UUID;

public class SoldierData {
    private UUID soldierUUID;
    private String rank = "Soldier";
    private int kills = 0;
    private String displayName = "Soldier";
    private int homelessNights = 0;
    private long lastFoodDay = -1;
    private long awakenUntilGameTime;

    public SoldierData() {
    }

    public SoldierData(UUID soldierUUID) {
        this.soldierUUID = soldierUUID;
    }

    public UUID getSoldierUUID() {
        return soldierUUID;
    }

    public void setSoldierUUID(UUID uuid) {
        this.soldierUUID = uuid;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill() {
        this.kills++;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public int getHomelessNights() {
        return homelessNights;
    }

    public void setHomelessNights(int nights) {
        this.homelessNights = nights;
    }

    public long getLastFoodDay() {
        return lastFoodDay;
    }

    public void setLastFoodDay(long day) {
        this.lastFoodDay = day;
    }

    public long getAwakenUntilGameTime() {
        return awakenUntilGameTime;
    }

    public void setAwakenUntilGameTime(long awakenUntilGameTime) {
        this.awakenUntilGameTime = awakenUntilGameTime;
    }
}
