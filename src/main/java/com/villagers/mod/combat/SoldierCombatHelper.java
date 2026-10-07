package com.villagers.mod.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;

public final class SoldierCombatHelper {
    private static final long COMBAT_COOLDOWN_TICKS = 600L;

    private SoldierCombatHelper() {
    }

    public static boolean isOutOfCombat(Villager villager, long lastDamageTick) {
        long now = villager.level().getGameTime();
        if (now - lastDamageTick < COMBAT_COOLDOWN_TICKS) {
            return false;
        }
        AABB box = villager.getBoundingBox().inflate(SoldierCombatConstants.OUT_OF_COMBAT_ENEMY_RANGE);
        for (LivingEntity entity : villager.level().getEntitiesOfClass(LivingEntity.class, box)) {
            if (entity instanceof Monster && entity.isAlive() && villager.hasLineOfSight(entity)) {
                return false;
            }
        }
        return true;
    }
}
