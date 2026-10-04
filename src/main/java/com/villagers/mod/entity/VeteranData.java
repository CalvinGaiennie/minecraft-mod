package com.villagers.mod.entity;

public class VeteranData {
    private int kills;
    private String rank = "Soldier";

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
}
