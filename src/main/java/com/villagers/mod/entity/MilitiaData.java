package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MilitiaData {
    private UUID militiaId;
    private int kills;
    private long lastFoodDay = -1;
    private String displayName = "";
    private boolean traderAtEnlist;
    @Nullable
    private BlockPos homeBed;

    public MilitiaData() {
    }

    public MilitiaData(UUID militiaId) {
        this.militiaId = militiaId;
    }

    public UUID getMilitiaId() {
        return militiaId;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
    }

    public boolean isSeasoned() {
        return kills >= 10;
    }

    public long getLastFoodDay() {
        return lastFoodDay;
    }

    public void setLastFoodDay(long lastFoodDay) {
        this.lastFoodDay = lastFoodDay;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean wasTraderAtEnlist() {
        return traderAtEnlist;
    }

    public void setTraderAtEnlist(boolean traderAtEnlist) {
        this.traderAtEnlist = traderAtEnlist;
    }

    @Nullable
    public BlockPos getHomeBed() {
        return homeBed;
    }

    public void setHomeBed(@Nullable BlockPos homeBed) {
        this.homeBed = homeBed == null ? null : homeBed.immutable();
    }
}
