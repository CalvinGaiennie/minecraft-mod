package com.villagers.mod.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.Villager;

public final class VillagerPersonalNames {
    private VillagerPersonalNames() {
    }

    /** Name tag text from a player-renamed villager; empty if they only had the default entity name. */
    public static String captureAtEnlist(Villager villager) {
        Component custom = villager.getCustomName();
        if (custom == null) {
            return "";
        }
        String trimmed = custom.getString().trim();
        return trimmed.isEmpty() ? "" : trimmed;
    }

    public static String soldierLabel(String rank, String storedPersonalName, int kills) {
        return labelWithKills(rank, storedPersonalName, kills);
    }

    public static String militiaLabel(String rank, String storedPersonalName, int kills) {
        return labelWithKills(rank, storedPersonalName, kills);
    }

    private static String labelWithKills(String rank, String storedPersonalName, int kills) {
        if (kills <= 0) {
            return rank;
        }
        String personal = sanitizeStored(storedPersonalName);
        if (personal.isEmpty()) {
            return rank + " (" + kills + " kills)";
        }
        return rank + " " + personal + " (" + kills + " kills)";
    }

    /** Drops blank names and values saved before we stopped using {@link Villager#getName()}. */
    private static String sanitizeStored(String stored) {
        if (stored == null || stored.isBlank()) {
            return "";
        }
        String trimmed = stored.trim();
        if ("Villager".equalsIgnoreCase(trimmed)) {
            return "";
        }
        if ("Soldier".equalsIgnoreCase(trimmed) || "Militia".equalsIgnoreCase(trimmed)) {
            return "";
        }
        return trimmed;
    }
}
