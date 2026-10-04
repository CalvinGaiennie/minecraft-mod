package com.villagers.mod.village;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class VillageData {
    private String villageName;
    private UUID villageId;
    private UUID ownerId;
    private int markerX;
    private int markerY;
    private int markerZ;
    private int radius;
    private boolean active;
    private long createdTime;

    public VillageData(String villageName, UUID ownerId, int x, int y, int z) {
        this.villageName = villageName;
        this.villageId = UUID.randomUUID();
        this.ownerId = ownerId;
        this.markerX = x;
        this.markerY = y;
        this.markerZ = z;
        this.radius = 64;
        this.active = true;
        this.createdTime = System.currentTimeMillis();
    }

    public void writeToTag(CompoundTag tag) {
        tag.putString("VillageName", villageName);
        tag.putUUID("VillageId", villageId);
        tag.putUUID("OwnerId", ownerId);
        tag.putInt("MarkerX", markerX);
        tag.putInt("MarkerY", markerY);
        tag.putInt("MarkerZ", markerZ);
        tag.putInt("Radius", radius);
        tag.putBoolean("Active", active);
        tag.putLong("CreatedTime", createdTime);
    }

    public static VillageData readFromTag(CompoundTag tag) {
        VillageData data = new VillageData(
            tag.getString("VillageName"),
            tag.getUUID("OwnerId"),
            tag.getInt("MarkerX"),
            tag.getInt("MarkerY"),
            tag.getInt("MarkerZ")
        );
        data.villageId = tag.getUUID("VillageId");
        data.radius = tag.getInt("Radius");
        data.active = tag.getBoolean("Active");
        data.createdTime = tag.getLong("CreatedTime");
        return data;
    }

    public String getVillageName() { return villageName; }
    public UUID getVillageId() { return villageId; }
    public UUID getOwnerId() { return ownerId; }
    public int getMarkerX() { return markerX; }
    public int getMarkerY() { return markerY; }
    public int getMarkerZ() { return markerZ; }
    public int getRadius() { return radius; }
    public boolean isActive() { return active; }
    public long getCreatedTime() { return createdTime; }

    public void setRadius(int radius) { this.radius = radius; }
    public void setActive(boolean active) { this.active = active; }
}
