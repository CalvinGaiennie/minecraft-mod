package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;

import org.jetbrains.annotations.Nullable;

public class VeteranData {
    private int kills;
    private String rank = "Soldier";
    @Nullable
    private BlockPos homeBed;

    public VeteranData() {
    }

    public VeteranData(int kills, String rank) {
        this.kills = kills;
        this.rank = rank;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Nullable
    public BlockPos getHomeBed() {
        return homeBed;
    }

    public void setHomeBed(@Nullable BlockPos homeBed) {
        this.homeBed = homeBed == null ? null : homeBed.immutable();
    }
}
