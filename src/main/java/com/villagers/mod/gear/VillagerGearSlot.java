package com.villagers.mod.gear;

import net.minecraft.network.chat.Component;

public enum VillagerGearSlot {
    HELMET("slot.villagers.helmet", "slot.villagers.helmet.tip", 44, 32),
    CHESTPLATE("slot.villagers.chestplate", "slot.villagers.chestplate.tip", 44, 50),
    LEGGINGS("slot.villagers.leggings", "slot.villagers.leggings.tip", 44, 68),
    BOOTS("slot.villagers.boots", "slot.villagers.boots.tip", 44, 86),
    MELEE("slot.villagers.melee", "slot.villagers.melee.tip", 128, 50),
    RANGED("slot.villagers.ranged", "slot.villagers.ranged.tip", 128, 68),
    ARROWS("slot.villagers.arrows", "slot.villagers.arrows.tip", 128, 86);

    private final String labelKey;
    private final String tipKey;
    private final int x;
    private final int y;

    VillagerGearSlot(String labelKey, String tipKey, int x, int y) {
        this.labelKey = labelKey;
        this.tipKey = tipKey;
        this.x = x;
        this.y = y;
    }

    public Component label() {
        return Component.translatable(labelKey);
    }

    public Component tip() {
        return Component.translatable(tipKey);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
}
