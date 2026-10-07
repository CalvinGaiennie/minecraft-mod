package com.villagers.mod.entity;

import java.util.UUID;

public class MinionData {
    private final UUID ownerId;

    public MinionData(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }
}
