package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;

import com.villagers.mod.war.SoldierSignalType;

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
    @Nullable
    private BlockPos squadBoxPos;
    private boolean squadLeader;
    private boolean actingLeader;
    @Nullable
    private BlockPos assignedRampart;
    private SoldierSignalType activeSignal = SoldierSignalType.NONE;
    private long signalUntilGameTime;
    private double signalX;
    private double signalY;
    private double signalZ;
    private int signalRadius;
    @Nullable
    private BlockPos redstoneTargetPos;
    private long redstoneTargetUntil;

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

    @Nullable
    public BlockPos getSquadBoxPos() {
        return squadBoxPos;
    }

    public void setSquadBoxPos(@Nullable BlockPos squadBoxPos) {
        this.squadBoxPos = squadBoxPos == null ? null : squadBoxPos.immutable();
    }

    public boolean isSquadLeader() {
        return squadLeader;
    }

    public void setSquadLeader(boolean squadLeader) {
        this.squadLeader = squadLeader;
        if (squadLeader) {
            this.actingLeader = false;
        }
    }

    public boolean isActingLeader() {
        return actingLeader;
    }

    public void setActingLeader(boolean actingLeader) {
        this.actingLeader = actingLeader;
        if (actingLeader) {
            this.squadLeader = false;
        }
    }

    @Nullable
    public BlockPos getAssignedRampart() {
        return assignedRampart;
    }

    public void setAssignedRampart(@Nullable BlockPos assignedRampart) {
        this.assignedRampart = assignedRampart == null ? null : assignedRampart.immutable();
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

    public SoldierSignalType getActiveSignal() {
        return activeSignal;
    }

    public void setActiveSignal(SoldierSignalType activeSignal) {
        this.activeSignal = activeSignal == null ? SoldierSignalType.NONE : activeSignal;
    }

    public long getSignalUntilGameTime() {
        return signalUntilGameTime;
    }

    public void setSignalUntilGameTime(long signalUntilGameTime) {
        this.signalUntilGameTime = signalUntilGameTime;
    }

    public double getSignalX() {
        return signalX;
    }

    public double getSignalY() {
        return signalY;
    }

    public double getSignalZ() {
        return signalZ;
    }

    public int getSignalRadius() {
        return signalRadius;
    }

    public void setSignalX(double signalX) {
        this.signalX = signalX;
    }

    public void setSignalY(double signalY) {
        this.signalY = signalY;
    }

    public void setSignalZ(double signalZ) {
        this.signalZ = signalZ;
    }

    public void setSignalRadius(int signalRadius) {
        this.signalRadius = signalRadius;
    }

    public void clearSignalTarget() {
        this.signalRadius = 0;
    }

    @Nullable
    public BlockPos getRedstoneTargetPos() {
        return redstoneTargetPos;
    }

    public void setRedstoneTargetPos(@Nullable BlockPos redstoneTargetPos) {
        this.redstoneTargetPos = redstoneTargetPos == null ? null : redstoneTargetPos.immutable();
    }

    public long getRedstoneTargetUntil() {
        return redstoneTargetUntil;
    }

    public void setRedstoneTargetUntil(long redstoneTargetUntil) {
        this.redstoneTargetUntil = redstoneTargetUntil;
    }

    public void clearRedstoneTarget() {
        this.redstoneTargetPos = null;
        this.redstoneTargetUntil = 0L;
    }
}
