package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.combat.RampartPathing;
import com.villagers.mod.defense.NightwatchGear;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.util.SoldierStructureHelper;

/** Soldiers/militia: fast return to rampart, mess, or bed. Haven blocks are for civilians only. */
public final class PostReturnPathing {
    public static final double RETURN_SPEED = 1.05;
    /** Same window as {@link SoldierBehavior} soldier sleep (not nightwatch day-sleep). */
    private static final long SOLDIER_SLEEP_START = 13000L;
    private static final long SOLDIER_SLEEP_END = 23000L;

    private PostReturnPathing() {
    }

    public static boolean isSoldierNightSleepHours(Villager villager) {
        long dayTime = villager.level().getDayTime() % 24000L;
        return dayTime >= SOLDIER_SLEEP_START && dayTime < SOLDIER_SLEEP_END;
    }

    public static boolean hasCombatTarget(Villager villager) {
        return villager instanceof Mob mob && mob.getTarget() != null && mob.getTarget().isAlive();
    }

    /** Rampart post when idle; skips if already in combat. At night only nightwatch helmets seek rampart. */
    public static void returnToRampart(Villager villager) {
        if (hasCombatTarget(villager)) {
            return;
        }
        if (isSoldierNightSleepHours(villager) && !NightwatchGear.hasNightwatchHelmet(villager)) {
            return;
        }
        RampartPathing.tickSeekNearestRampart(villager, RETURN_SPEED);
    }

    public static void returnToMess(Villager villager, ServerLevel level) {
        if (hasCombatTarget(villager)) {
            return;
        }
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null && SoldierBedSleep.restsUndisturbedByHostiles(villager, data)) {
                return;
            }
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
        if (nav.isDone() || villager.tickCount % 4 == 0) {
            nav.moveTo(x, y, z, RETURN_SPEED);
        }
    }

}
