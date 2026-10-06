package com.villagers.mod.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

/** Villagers need melee attributes when soldiers use {@link net.minecraft.world.entity.ai.goal.MeleeAttackGoal}. */
public final class VillagerAttributeSetup {
    private VillagerAttributeSetup() {
    }

    public static void onModifyAttributes(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.VILLAGER, Attributes.ATTACK_DAMAGE)) {
            event.add(EntityType.VILLAGER, Attributes.ATTACK_DAMAGE, 1.0);
        }
        if (!event.has(EntityType.VILLAGER, Attributes.ATTACK_SPEED)) {
            event.add(EntityType.VILLAGER, Attributes.ATTACK_SPEED, 4.0);
        }
    }
}
