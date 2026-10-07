package com.villagers.mod.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.util.SoldierStructureHelper;

import org.jetbrains.annotations.Nullable;

/** Limits soldier pursuit to a zone around rampart (preferred) or assigned bed. */
public final class SoldierHoldGround {
    private SoldierHoldGround() {
    }

    public static boolean isHoldGroundActive(SoldierData data, long gameTime) {
        if (data.isOnCampaign()) {
            return false;
        }
        return data.getAwakenUntilGameTime() <= gameTime;
    }

    @Nullable
    public static BlockPos resolveAnchor(ServerLevel level, Villager soldier, SoldierData data) {
        return SoldierStructureHelper.findNearestRampart(
                        level, soldier.blockPosition(), RampartPathing.RAMPART_SEEK_RADIUS)
                .orElse(data.getAssignedPostBed());
    }

    public static double distanceToAnchor(Villager soldier, BlockPos anchor) {
        return soldier.position().distanceTo(Vec3.atCenterOf(anchor));
    }

    public static boolean hostileWithinEngageZone(BlockPos anchor, LivingEntity hostile) {
        return anchor.distSqr(hostile.blockPosition()) <= sq(SoldierCombatConstants.HOLD_GROUND_ENGAGE_RADIUS);
    }

    @Nullable
    public static LivingEntity findHostileInEngageZone(Villager soldier, BlockPos anchor) {
        Level level = soldier.level();
        double soldierRange = SoldierCombatConstants.WAKE_HOSTILE_RANGE;
        AABB box = soldier.getBoundingBox().inflate(soldierRange);
        LivingEntity bestVisible = null;
        double bestVisibleDist = soldierRange * soldierRange;
        LivingEntity bestHidden = null;
        double bestHiddenDist = soldierRange * soldierRange;
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box, e -> e instanceof Monster && e.isAlive())) {
            if (entity == soldier) {
                continue;
            }
            if (!hostileWithinEngageZone(anchor, entity)) {
                continue;
            }
            double dist = SoldierRampartShooting.horizontalDistanceSqr(soldier, entity);
            if (soldier.hasLineOfSight(entity)) {
                if (dist < bestVisibleDist) {
                    bestVisibleDist = dist;
                    bestVisible = entity;
                }
            } else if (dist < bestHiddenDist) {
                bestHiddenDist = dist;
                bestHidden = entity;
            }
        }
        return bestVisible != null ? bestVisible : bestHidden;
    }

    /** Drop invalid targets and path home when the soldier over-extends. */
    public static void enforceHoldGround(Mob mob, Villager soldier, SoldierData data, BlockPos anchor) {
        LivingEntity target = mob.getTarget();
        if (target != null && (!target.isAlive() || !hostileWithinEngageZone(anchor, target))) {
            mob.setTarget(null);
            target = null;
        }
        if (distanceToAnchor(soldier, anchor) > SoldierCombatConstants.HOLD_GROUND_MAX_CHASE) {
            mob.setTarget(null);
            pathToAnchor(mob, soldier.level(), anchor, 0.85);
            return;
        }
        if (target == null && mob instanceof Villager villager) {
            com.villagers.mod.entity.PostReturnPathing.returnToRampart(villager);
        }
    }

    private static void pathToAnchor(Mob mob, Level level, BlockPos anchor, double speed) {
        if (!mob.getNavigation().isDone()) {
            return;
        }
        if (SoldierRampartShooting.isRampartAnchor(level, anchor)) {
            if (mob instanceof Villager villager) {
                RampartPathing.navigateToStand(villager, SoldierRampartShooting.standOnRampart(anchor), speed);
            }
        } else {
            mob.getNavigation().moveTo(anchor.getX() + 0.5, anchor.getY(), anchor.getZ() + 0.5, speed);
        }
    }

    private static double sq(double radius) {
        return radius * radius;
    }
}
