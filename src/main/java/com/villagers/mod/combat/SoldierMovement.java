package com.villagers.mod.combat;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Villager;

/** Walk speed for soldiers, militia, and bandits relative to vanilla zombies. */
public final class SoldierMovement {
    /** Vanilla zombie {@link Attributes#MOVEMENT_SPEED} base. */
    public static final double ZOMBIE_MOVEMENT_SPEED = 0.23;
    /** Default unemployed villager walk speed. */
    public static final double VILLAGER_MOVEMENT_SPEED = 0.5;
    /** Soldiers and militia: halfway between zombie and unemployed villager walk speed. */
    public static final double COMBAT_MOVEMENT_SPEED = (ZOMBIE_MOVEMENT_SPEED + VILLAGER_MOVEMENT_SPEED) * 0.5;

    private SoldierMovement() {
    }

    public static void applyCombatWalkSpeed(Villager villager) {
        AttributeInstance speed = villager.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(COMBAT_MOVEMENT_SPEED);
        }
    }

    public static void resetVillagerWalkSpeed(Villager villager) {
        AttributeInstance speed = villager.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(VILLAGER_MOVEMENT_SPEED);
        }
    }
}
