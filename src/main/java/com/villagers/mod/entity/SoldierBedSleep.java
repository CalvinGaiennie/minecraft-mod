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
import com.villagers.mod.defense.NightwatchGear;
import com.villagers.mod.threat.BanditRaidService;
import com.villagers.mod.util.SoldierStructureHelper;

import org.jetbrains.annotations.Nullable;

public final class SoldierBedSleep {
    private static final float REST_HEAL_PER_TICK = 2.0f;

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
        tryRestSleep(soldier, data);
    }

    public static void tryRestSleep(Villager soldier, SoldierData data) {
        if (!(soldier.level() instanceof ServerLevel level) || data == null) {
            return;
        }
        if (soldier instanceof net.minecraft.world.entity.Mob mob
                && mob.getTarget() != null
                && mob.getTarget().isAlive()) {
            if (restsUndisturbedByHostiles(soldier, data)) {
                mob.setTarget(null);
            } else {
                wakeNow(soldier);
                return;
            }
        }
        BlockPos foot = resolveRestBedFoot(level, soldier, data);
        if (foot == null) {
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
    }

    /** Heal while lying in any intact bed (post bed or villager bed). */
    public static void applyRestHeal(Villager soldier, SoldierData data) {
        if (data == null || !soldier.isSleeping() || soldier.getHealth() >= soldier.getMaxHealth()) {
            return;
        }
        if (!(soldier.level() instanceof ServerLevel level)) {
            return;
        }
        BlockPos sleepPos = soldier.getSleepingPos().orElse(null);
        if (sleepPos == null || !isRestBedPosition(level, sleepPos)) {
            return;
        }
        long lastDamage = data.getLastDamageGameTime();
        if (!SoldierCombatHelper.canHealWhileResting(soldier, data, lastDamage < 0 ? 0 : lastDamage)) {
            return;
        }
        float missing = soldier.getMaxHealth() - soldier.getHealth();
        soldier.heal(Math.min(REST_HEAL_PER_TICK, missing));
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

    /**
     * Scheduled rest: ignore nearby hostiles until damage, bell, or awaken horn sets {@link SoldierData#getAwakenUntilGameTime()}.
     */
    public static boolean restsUndisturbedByHostiles(Villager soldier, SoldierData data) {
        if (data == null || data.isOnCampaign()) {
            return false;
        }
        if (soldier.level() instanceof ServerLevel server
                && BanditRaidService.banditThreatInSoldierVillage(server, soldier)) {
            return false;
        }
        if (data.getAwakenUntilGameTime() > soldier.level().getGameTime()) {
            return false;
        }
        long dayTime = soldier.level().getDayTime() % 24000L;
        boolean night = dayTime >= 13000L && dayTime < 23000L;
        if (NightwatchGear.hasNightwatchHelmet(soldier)) {
            return !night;
        }
        return night;
    }

    @Nullable
    private static BlockPos resolveRestBedFoot(ServerLevel level, Villager soldier, SoldierData data) {
        SoldierStructureHelper.ensureAssignedBedCached(level, soldier.getUUID(), data, soldier.blockPosition());
        BlockPos assigned = data.getAssignedPostBed();
        if (assigned != null && canRestAtBed(level, soldier.getUUID(), assigned)) {
            return VanillaBedHelper.footPosition(level, assigned);
        }
        BlockPos fromMemory = bedFromVillagerMemory(soldier);
        if (fromMemory != null && canRestAtBed(level, soldier.getUUID(), fromMemory)) {
            return VanillaBedHelper.footPosition(level, fromMemory);
        }
        return null;
    }

    @Nullable
    private static BlockPos bedFromVillagerMemory(Villager villager) {
        var brain = villager.getBrain();
        GlobalPos home = brain.getMemory(MemoryModuleType.HOME).orElse(null);
        if (home != null && home.dimension() == villager.level().dimension()) {
            BlockPos pos = home.pos();
            if (isRestBedPosition(villager.level(), pos)) {
                return pos.immutable();
            }
        }
        BlockPos nearest = brain.getMemory(MemoryModuleType.NEAREST_BED).orElse(null);
        if (nearest != null && isRestBedPosition(villager.level(), nearest)) {
            return nearest.immutable();
        }
        return null;
    }

    private static boolean canRestAtBed(ServerLevel level, java.util.UUID soldierId, BlockPos footOrAny) {
        BlockPos foot = VanillaBedHelper.footPosition(level, footOrAny);
        if (!VanillaBedHelper.isIntactBed(level, foot)) {
            return false;
        }
        return !SoldierBedClaims.get(level).isClaimedByOther(foot, soldierId);
    }

    private static boolean isRestBedPosition(net.minecraft.world.level.Level level, BlockPos pos) {
        if (!level.isLoaded(pos)) {
            return false;
        }
        BlockPos foot = VanillaBedHelper.footPosition(level, pos);
        return VanillaBedHelper.isIntactBed(level, foot);
    }
}
