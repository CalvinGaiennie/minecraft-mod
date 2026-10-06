package com.villagers.mod.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.Villager;

import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/** Keeps distance and triggers {@link SoldierCombatArms} bow shots (villagers are not {@code RangedAttackMob}). */
public class SoldierRangedAttackGoal extends Goal {
    private final PathfinderMob mob;

    public SoldierRangedAttackGoal(PathfinderMob mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!(mob instanceof Villager villager)) {
            return false;
        }
        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        if (!SoldierCombatArms.hasRangedReady(villager)) {
            return false;
        }
        return SoldierCombatArms.inRangedAttackBand(villager, target);
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null || !(mob instanceof Villager villager)) {
            return;
        }
        mob.getLookControl().setLookAt(target, 30.0f, 30.0f);
        SoldierCombatArms.syncHandsForTarget(villager, target);

        if (SoldierRampartShooting.shouldUseRampartArchery(villager, target)) {
            tickRampartArchery(villager, target);
            return;
        }

        tickGroundRanged(villager, target);
    }

    private void tickRampartArchery(Villager villager, LivingEntity target) {
        boolean onWall = SoldierRampartShooting.isOnRampart(villager);
        boolean clearShot = onWall && SoldierRampartShooting.hasRampartLineOfFire(villager, target);

        if (!onWall || !clearShot) {
            SoldierRampartShooting.tryPathToRampartForArchery(mob, villager, target);
            if (onWall && SoldierRampartShooting.hasRampartLineOfFire(villager, target)) {
                SoldierCombatArms.tickBowShot(villager, target);
            }
            return;
        }

        mob.getNavigation().stop();
        SoldierCombatArms.tickBowShot(villager, target);

        double dist = combatDistanceSqr(villager, target);
        double max = SoldierCombatConstants.RANGED_ATTACK_MAX_DISTANCE;
        double min = SoldierCombatArms.rangedMinDistance(villager, target);
        if (dist > max * max && mob.getNavigation().isDone()) {
            BlockPos anchor = resolveAnchor(villager);
            SoldierRampartShooting.pathToShootPosition(mob, villager, target, anchor);
        } else if (dist < min * min) {
            mob.getNavigation().stop();
        }
    }

    private void tickGroundRanged(Villager villager, LivingEntity target) {
        SoldierCombatArms.tickBowShot(villager, target);

        BlockPos anchor = resolveAnchor(villager);
        boolean wallArcher = villager.level() instanceof ServerLevel level
                && SoldierRampartShooting.prefersWallArchery(villager, level, anchor);

        if (wallArcher && mob.getY() > target.getY() + 2.0) {
            if (!mob.hasLineOfSight(target)) {
                if (anchor != null && SoldierRampartShooting.pathToShootPosition(mob, villager, target, anchor)) {
                    return;
                }
            }
            mob.getNavigation().stop();
            return;
        }

        if (wallArcher && !mob.hasLineOfSight(target) && anchor != null) {
            if (SoldierRampartShooting.pathToShootPosition(mob, villager, target, anchor)) {
                return;
            }
        }

        double dist = combatDistanceSqr(villager, target);
        double max = SoldierCombatConstants.RANGED_ATTACK_MAX_DISTANCE;
        double min = SoldierCombatArms.rangedMinDistance(villager, target);
        if (dist > max * max) {
            if (!wallArcher || mob.getY() <= target.getY() + 2.0) {
                mob.getNavigation().moveTo(target, 0.9);
            }
        } else if (dist < min * min) {
            mob.getNavigation().stop();
        } else if (mob.getNavigation().isDone() && (!wallArcher || mob.hasLineOfSight(target))) {
            mob.getNavigation().moveTo(target, 0.65);
        }
    }

    private static double combatDistanceSqr(Villager villager, LivingEntity target) {
        if (villager.level() instanceof ServerLevel level) {
            BlockPos anchor = resolveAnchor(villager);
            if (SoldierRampartShooting.prefersWallArchery(villager, level, anchor)) {
                return SoldierRampartShooting.horizontalDistanceSqr(villager, target);
            }
        }
        return villager.distanceToSqr(target);
    }

    @Nullable
    private static BlockPos resolveAnchor(Villager villager) {
        return SoldierCombatArms.resolveCombatAnchor(villager);
    }
}
