package com.villagers.mod.entity;

import java.util.UUID;

public class BanditData {
    private final UUID villagerId;

    public BanditData(UUID villagerId) {
        this.villagerId = villagerId;
    }

    public UUID getVillagerId() {
        return villagerId;
    }
}
