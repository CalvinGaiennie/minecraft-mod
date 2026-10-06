package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SoldierData {
    private UUID soldierUUID;
    private String rank = "Soldier";
    private int kills = 0;
    private String displayName = "";
    private int homelessNights = 0;
    private long lastFoodDay = -1;
    private long awakenUntilGameTime;
    private long lastDamageGameTime = -1L;
    private boolean onCampaign;
    @Nullable
    private BlockPos campaignCamp;
    @Nullable
    private UUID followPlayerId;
    private int grumblePoints;
    private int practiceKills;
    @Nullable
    private BlockPos assignedPostBed;

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

    @Nullable
    public BlockPos getAssignedPostBed() {
        return assignedPostBed;
    }

    public void setAssignedPostBed(@Nullable BlockPos assignedPostBed) {
        this.assignedPostBed = assignedPostBed == null ? null : assignedPostBed.immutable();
    }

    public long getLastDamageGameTime() {
        return lastDamageGameTime;
    }

    public void setLastDamageGameTime(long lastDamageGameTime) {
        this.lastDamageGameTime = lastDamageGameTime;
    }

    public boolean isOnCampaign() {
        return onCampaign;
    }

    public void setOnCampaign(boolean onCampaign) {
        this.onCampaign = onCampaign;
    }

    @Nullable
    public BlockPos getCampaignCamp() {
        return campaignCamp;
    }

    public void setCampaignCamp(@Nullable BlockPos campaignCamp) {
        this.campaignCamp = campaignCamp == null ? null : campaignCamp.immutable();
    }

    @Nullable
    public UUID getFollowPlayerId() {
        return followPlayerId;
    }

    public void setFollowPlayerId(@Nullable UUID followPlayerId) {
        this.followPlayerId = followPlayerId;
    }

    public int getGrumblePoints() {
        return grumblePoints;
    }

    public void setGrumblePoints(int grumblePoints) {
        this.grumblePoints = Math.max(0, grumblePoints);
    }

    public void addGrumblePoint() {
        this.grumblePoints++;
    }

    public int getPracticeKills() {
        return practiceKills;
    }

    public void addPracticeKill() {
        if (practiceKills < 5) {
            practiceKills++;
        }
    }
}
