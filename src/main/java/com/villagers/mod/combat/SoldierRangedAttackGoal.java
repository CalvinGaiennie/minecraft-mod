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
    private static final int CONTINUE_RECHECK_INTERVAL = 5;

    private final PathfinderMob mob;
    private final CombatRangedMotion rangedMotion = new CombatRangedMotion();
    private int continueRecheckCooldown;

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
        return SoldierCombatArms.canShootTarget(villager, target);
    }

    @Override
    public boolean canContinueToUse() {
        if (continueRecheckCooldown > 0) {
            continueRecheckCooldown--;
            LivingEntity target = mob.getTarget();
            return target != null && target.isAlive();
        }
        continueRecheckCooldown = CONTINUE_RECHECK_INTERVAL;
        return canUse();
    }

    @Override
    public void start() {
        continueRecheckCooldown = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null || !(mob instanceof Villager villager)) {
            return;
        }
        mob.getLookControl().setLookAt(target, 30.0f, 30.0f);
        if (mob.tickCount % 3 == 0) {
            SoldierCombatArms.syncHandsForTarget(villager, target);
        }

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

        SoldierCombatArms.tickBowShot(villager, target);

        double distBlocks = Math.sqrt(combatDistanceSqr(villager, target));
        double max = SoldierCombatConstants.RANGED_ATTACK_MAX_DISTANCE;
        double min = SoldierCombatArms.rangedMinDistance(villager, target);
        if (distBlocks > max && mob.getNavigation().isDone()) {
            BlockPos anchor = resolveAnchor(villager);
            SoldierRampartShooting.pathToShootPosition(mob, villager, target, anchor);
        } else {
            rangedMotion.tickRampartHold(mob, target, distBlocks, min, max);
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
            if (mob.getNavigation().isInProgress()) {
                mob.getNavigation().stop();
            }
            return;
        }

        if (wallArcher && !mob.hasLineOfSight(target) && anchor != null) {
            if (SoldierRampartShooting.pathToShootPosition(mob, villager, target, anchor)) {
                return;
            }
        }

        double distBlocks = Math.sqrt(combatDistanceSqr(villager, target));
        double max = SoldierCombatConstants.RANGED_ATTACK_MAX_DISTANCE;
        double min = SoldierCombatArms.rangedMinDistance(villager, target);
        boolean allowChase = !wallArcher || mob.getY() <= target.getY() + 2.0;
        if (wallArcher && mob.hasLineOfSight(target)) {
            allowChase = false;
        }
        boolean kiteCreeper = SoldierHostileTargets.requiresRangedKiting(target);
        rangedMotion.tickGround(mob, target, distBlocks, min, max, allowChase, kiteCreeper);
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
