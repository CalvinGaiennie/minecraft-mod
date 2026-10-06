package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.combat.RampartPathing;
import com.villagers.mod.util.SoldierStructureHelper;

/** Soldiers/militia: fast return to rampart, mess, or bed. Haven blocks are for civilians only. */
public final class PostReturnPathing {
    public static final double RETURN_SPEED = 1.05;

    private PostReturnPathing() {
    }

    public static boolean hasCombatTarget(Villager villager) {
        return villager instanceof Mob mob && mob.getTarget() != null && mob.getTarget().isAlive();
    }

    /** Rampart post when idle; skips if already in combat. */
    public static void returnToRampart(Villager villager) {
        if (hasCombatTarget(villager)) {
            return;
        }
        RampartPathing.tickSeekNearestRampart(villager, RETURN_SPEED);
    }

    public static void returnToMess(Villager villager, ServerLevel level) {
        if (hasCombatTarget(villager)) {
            return;
        }
        SoldierStructureHelper.findStockedMessStation(level, villager.blockPosition()).ifPresent(pos -> {
            if (villager.blockPosition().distSqr(pos) <= 4) {
                return;
            }
            navigateFast(villager, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        });
    }

    public static void returnToBed(Villager villager, BlockPos foot) {
        if (hasCombatTarget(villager) || foot == null || !villager.level().isLoaded(foot)) {
            return;
        }
        var sleepPos = com.villagers.mod.bed.VanillaBedHelper.sleepPosition(villager.level(), foot);
        if (villager.blockPosition().distSqr(sleepPos) <= 6) {
            return;
        }
        navigateFast(villager, sleepPos.getX() + 0.5, sleepPos.getY(), sleepPos.getZ() + 0.5);
    }

    public static void navigateFast(Villager villager, double x, double y, double z) {
        if (villager.position().distanceToSqr(x, y, z) < 2.25) {
            villager.getNavigation().stop();
            return;
        }
        PathNavigation nav = villager.getNavigation();
        if (nav.isDone() || villager.tickCount % 8 == 0) {
            nav.moveTo(x, y, z, RETURN_SPEED);
        }
    }

}
