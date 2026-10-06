package com.villagers.mod.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.npc.Villager;

/** Melee on ground; archers on the wall keep shooting instead of chasing down. */
public class SoldierMeleeAttackGoal extends MeleeAttackGoal {
    public SoldierMeleeAttackGoal(PathfinderMob mob) {
        super(mob, 1.0, false);
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            return false;
        }
        if (!(mob instanceof Villager villager)) {
            return true;
        }
        if (!SoldierCombatArms.hasRangedReady(villager)) {
            return true;
        }
        LivingEntity target = mob.getTarget();
        if (target == null) {
            return true;
        }
        if (mob.getY() > target.getY() + 2.0) {
            return false;
        }
        return SoldierRampartShooting.horizontalDistanceSqr(mob, target)
                <= SoldierCombatConstants.MELEE_PREFER_MAX_DISTANCE * SoldierCombatConstants.MELEE_PREFER_MAX_DISTANCE;
    }
}
