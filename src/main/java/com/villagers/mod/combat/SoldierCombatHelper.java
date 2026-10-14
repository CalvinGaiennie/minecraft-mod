package com.villagers.mod.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;

import com.villagers.mod.entity.SoldierBedSleep;
import com.villagers.mod.entity.SoldierData;

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
            if (SoldierHostileTargets.isEngageable(entity) && villager.hasLineOfSight(entity)) {
                return false;
            }
        }
        return true;
    }

    /** Bed heal: scheduled rest ignores nearby hostiles; otherwise same as {@link #isOutOfCombat}. */
    public static boolean canHealWhileResting(Villager villager, SoldierData data, long lastDamageTick) {
        long now = villager.level().getGameTime();
        if (lastDamageTick >= 0 && now - lastDamageTick < COMBAT_COOLDOWN_TICKS) {
            return false;
        }
        if (SoldierBedSleep.restsUndisturbedByHostiles(villager, data)) {
            return true;
        }
        return isOutOfCombat(villager, lastDamageTick < 0 ? 0 : lastDamageTick);
    }
}
