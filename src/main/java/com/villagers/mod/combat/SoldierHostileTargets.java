package com.villagers.mod.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;

/** Hostiles soldiers/militia should fight (slimes are {@link net.minecraft.world.entity.Mob}, not {@link Monster}). */
public final class SoldierHostileTargets {
    private SoldierHostileTargets() {
    }

    public static boolean isEngageable(LivingEntity entity) {
        return entity.isAlive() && (entity instanceof Monster || entity instanceof Slime);
    }

    /** Hostiles soldiers should never melee when they can shoot (creeper fuse). */
    public static boolean requiresRangedKiting(LivingEntity entity) {
        return entity instanceof Creeper;
    }
}
