package com.villagers.mod.entity;

import net.minecraft.util.RandomSource;

public enum ZombieSoldierCure {
    RETURN_SOLDIER(0.60f),
    REFUSE_SERVICE(0.25f),
    DESERT(0.15f);

    private final float weight;

    ZombieSoldierCure(float weight) {
        this.weight = weight;
    }

    public static ZombieSoldierCure roll(RandomSource random) {
        float roll = random.nextFloat();
        if (roll < RETURN_SOLDIER.weight) {
            return RETURN_SOLDIER;
        }
        if (roll < RETURN_SOLDIER.weight + REFUSE_SERVICE.weight) {
            return REFUSE_SERVICE;
        }
        return DESERT;
    }
}
