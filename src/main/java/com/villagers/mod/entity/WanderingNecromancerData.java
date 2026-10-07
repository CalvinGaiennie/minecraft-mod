package com.villagers.mod.entity;

import java.util.UUID;

/** Deserter-turned wandering necromancer; not a player necromancer (no wand hearts). */
public class WanderingNecromancerData {
    private final UUID necromancerId;
    private UUID exOwnerId;
    private boolean exOwnerOnlineSinceDesert;

    public WanderingNecromancerData(UUID necromancerId) {
        this.necromancerId = necromancerId;
    }

    public UUID getNecromancerId() {
        return necromancerId;
    }

    public UUID getExOwnerId() {
        return exOwnerId;
    }

    public void setExOwnerId(UUID exOwnerId) {
        this.exOwnerId = exOwnerId;
    }

    public boolean isExOwnerOnlineSinceDesert() {
        return exOwnerOnlineSinceDesert;
    }

    public void setExOwnerOnlineSinceDesert(boolean exOwnerOnlineSinceDesert) {
        this.exOwnerOnlineSinceDesert = exOwnerOnlineSinceDesert;
    }
}
