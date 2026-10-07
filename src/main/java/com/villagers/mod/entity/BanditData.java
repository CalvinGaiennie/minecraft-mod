package com.villagers.mod.entity;

import java.util.UUID;

public class BanditData {
    public enum BossRole {
        NONE,
        HALVEK,
        CORVIN,
        GARLAND
    }

    private final UUID banditId;
    private BossRole bossRole = BossRole.NONE;
    private UUID questOwnerId;
    private boolean leader;
    private boolean d2GarlandFleeImmune;
    private UUID homeCampId;

    public BanditData(UUID banditId) {
        this.banditId = banditId;
    }

    public UUID getBanditId() {
        return banditId;
    }

    public BossRole getBossRole() {
        return bossRole;
    }

    public void setBossRole(BossRole bossRole) {
        this.bossRole = bossRole;
    }

    public UUID getQuestOwnerId() {
        return questOwnerId;
    }

    public void setQuestOwnerId(UUID questOwnerId) {
        this.questOwnerId = questOwnerId;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public boolean isQuestBoss() {
        return bossRole != BossRole.NONE;
    }

    public boolean isD2GarlandFleeImmune() {
        return d2GarlandFleeImmune;
    }

    public void setD2GarlandFleeImmune(boolean d2GarlandFleeImmune) {
        this.d2GarlandFleeImmune = d2GarlandFleeImmune;
    }

    public UUID getHomeCampId() {
        return homeCampId;
    }

    public void setHomeCampId(UUID homeCampId) {
        this.homeCampId = homeCampId;
    }
}
