package com.villagers.mod.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import org.jetbrains.annotations.Nullable;

/** Avoids redundant {@link Mob#setTarget} calls that reset pathfinding every tick. */
public final class CombatTargetAssign {
    private CombatTargetAssign() {
    }

    public static void assign(Mob mob, @Nullable LivingEntity target) {
        if (target != null && target.isAlive()) {
            if (mob.getTarget() == target) {
                return;
            }
            mob.setTarget(target);
            return;
        }
        if (mob.getTarget() != null) {
            mob.setTarget(null);
        }
    }
}
