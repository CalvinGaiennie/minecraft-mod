package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.bed.SoldierBedClaims;
import com.villagers.mod.bed.VanillaBedHelper;
import com.villagers.mod.combat.SoldierCombatHelper;
import com.villagers.mod.util.SoldierStructureHelper;

public final class SoldierBedSleep {
    private SoldierBedSleep() {
    }

    public static void syncHomeMemory(Villager villager, BlockPos footPos) {
        if (villager.level().isClientSide) {
            return;
        }
        villager.getBrain().setMemory(
                MemoryModuleType.HOME,
                GlobalPos.of(villager.level().dimension(), footPos));
    }

    public static void trySleepAtAssignedBed(Villager soldier, SoldierData data) {
        if (!(soldier.level() instanceof ServerLevel level) || !shouldUseBedSleep(soldier, data)) {
            return;
        }
        if (soldier instanceof net.minecraft.world.entity.Mob mob
                && mob.getTarget() != null
                && mob.getTarget().isAlive()) {
            wakeNow(soldier);
            return;
        }
        SoldierStructureHelper.ensureAssignedBedCached(level, soldier.getUUID(), data, soldier.blockPosition());
        BlockPos foot = data.getAssignedPostBed();
        if (foot == null || !level.isLoaded(foot)) {
            return;
        }
        BlockPos sleepPos = VanillaBedHelper.sleepPosition(level, foot);
        if (soldier.blockPosition().distSqr(sleepPos) > 6.0) {
            PostReturnPathing.returnToBed(soldier, foot);
            return;
        }
        if (!soldier.isSleeping()) {
            soldier.startSleeping(sleepPos);
        }
        long lastDamage = data.getLastDamageGameTime();
        if (SoldierCombatHelper.isOutOfCombat(soldier, lastDamage < 0 ? 0 : lastDamage)) {
            soldier.setHealth(soldier.getMaxHealth());
        }
    }

    public static void wakeIfNeeded(Villager soldier, boolean sleepTime) {
        if (!sleepTime && soldier.isSleeping()) {
            wakeNow(soldier);
        }
    }

    public static void wakeNow(LivingEntity entity) {
        if (!entity.isSleeping()) {
            return;
        }
        entity.setPose(Pose.STANDING);
        entity.clearSleepingPos();
    }

    private static boolean shouldUseBedSleep(Villager soldier, SoldierData data) {
        BlockPos foot = data.getAssignedPostBed();
        if (foot == null || !(soldier.level() instanceof ServerLevel level)) {
            return false;
        }
        foot = VanillaBedHelper.footPosition(level, foot);
        return VanillaBedHelper.isIntactBed(level, foot)
                && SoldierBedClaims.get(level).soldierOwnsBed(soldier.getUUID(), foot, level);
    }
}
