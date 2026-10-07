package com.villagers.mod.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.entity.SoldierBedSleep;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VeteranData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.threat.BanditService;
import com.villagers.mod.item.VeteranSwordItem;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class SoldierCombat {
    private static final Set<UUID> COMBAT_GOALS = new HashSet<>();

    private SoldierCombat() {
    }

    public static void enableCombatGoals(Villager villager) {
        if (!(villager instanceof PathfinderMob mob) || !COMBAT_GOALS.add(villager.getUUID())) {
            return;
        }
        mob.goalSelector.addGoal(2, new SoldierRangedAttackGoal(mob));
        mob.goalSelector.addGoal(3, new SoldierMeleeAttackGoal(mob));
        mob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(mob, Monster.class, true));
    }

    public static void disableCombatGoals(Villager villager) {
        if (!(villager instanceof Mob mob)) {
            return;
        }
        COMBAT_GOALS.remove(villager.getUUID());
        mob.setTarget(null);
    }

    public static void tick(Villager villager) {
        if (villager.level().isClientSide || !(villager instanceof Mob mob)) {
            return;
        }
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            enableCombatGoals(villager);
            LivingEntity hostile = findVisibleHostile(villager, SoldierCombatConstants.WAKE_HOSTILE_RANGE);
            if (hostile != null) {
                mob.setTarget(hostile);
            } else {
                com.villagers.mod.entity.PostReturnPathing.returnToRampart(villager);
            }
            suppressPanic(villager);
            return;
        }
        if (!shouldEngageHostiles(villager)) {
            if (isVeteranCivilian(villager)) {
                mob.setTarget(null);
            }
            return;
        }
        enableCombatGoals(villager);
        suppressPanic(villager);
        SoldierData soldierData = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        long gameTime = villager.level().getGameTime();
        LivingEntity hostile = null;
        BlockPos anchor = null;
        if (soldierData != null
                && villager.level() instanceof ServerLevel serverLevel
                && SoldierHoldGround.isHoldGroundActive(soldierData, gameTime)) {
            anchor = SoldierHoldGround.resolveAnchor(serverLevel, villager, soldierData);
            if (anchor != null) {
                SoldierHoldGround.enforceHoldGround(mob, villager, soldierData, anchor);
                hostile = SoldierHoldGround.findHostileInEngageZone(villager, anchor);
            }
        }
        if (hostile == null) {
            hostile = findVisibleHostile(villager, SoldierCombatConstants.WAKE_HOSTILE_RANGE);
        }
        if (hostile != null) {
            SoldierBedSleep.wakeIfNeeded(villager, false);
            mob.setTarget(hostile);
        } else {
            mob.setTarget(null);
            if (soldierData != null && !soldierData.isOnCampaign()
                    && soldierData.getAwakenUntilGameTime() <= gameTime) {
                com.villagers.mod.entity.PostReturnPathing.returnToRampart(villager);
            }
        }
        SoldierCombatArms.syncHandsForTarget(villager, mob.getTarget());
    }

    public static boolean shouldEngageHostiles(Villager villager) {
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return true;
        }
        if (!isVeteranDefender(villager)) {
            return false;
        }
        VeteranData veteran = villager.getData(VillagerAttachments.VETERAN_DATA.get());
        if (veteran == null || veteran.getHomeBed() == null) {
            return false;
        }
        return distanceToBed(villager, veteran.getHomeBed()) <= SoldierCombatConstants.VETERAN_DEFEND_RADIUS;
    }

    public static boolean isWithinHomeBedRadius(Villager villager) {
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null || data.getAssignedPostBed() == null) {
            return false;
        }
        return distanceToBed(villager, data.getAssignedPostBed()) <= SoldierCombatConstants.HOME_BED_RADIUS;
    }

    @Nullable
    public static LivingEntity findVisibleHostile(Villager villager, double range) {
        Level level = villager.level();
        AABB box = villager.getBoundingBox().inflate(range);
        LivingEntity best = null;
        double bestDist = range * range;
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box, e -> e instanceof Monster && e.isAlive())) {
            if (entity == villager || !villager.hasLineOfSight(entity)) {
                continue;
            }
            double dist = villager.distanceToSqr(entity);
            if (dist < bestDist) {
                bestDist = dist;
                best = entity;
            }
        }
        for (Villager other : level.getEntitiesOfClass(Villager.class, box)) {
            if (other == villager || !BanditService.isBandit(other) || !other.isAlive()) {
                continue;
            }
            if (!villager.hasLineOfSight(other)) {
                continue;
            }
            double dist = villager.distanceToSqr(other);
            if (dist < bestDist) {
                bestDist = dist;
                best = other;
            }
        }
        return best;
    }

    public static void suppressPanic(Villager villager) {
        Brain<Villager> brain = villager.getBrain();
        brain.eraseMemory(MemoryModuleType.IS_PANICKING);
        brain.eraseMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
        brain.eraseMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED);
        if (brain.isActive(Activity.PANIC)) {
            brain.setActiveActivityIfPossible(Activity.IDLE);
        }
    }

    public static boolean isVeteranDefender(Villager villager) {
        if (!villager.hasData(VillagerAttachments.VETERAN_DATA.get())) {
            return false;
        }
        return VeteranSwordItem.isVeteranSword(villager.getMainHandItem())
                || VeteranSwordItem.isVeteranSword(villager.getOffhandItem());
    }

    private static boolean isVeteranCivilian(Villager villager) {
        return villager.hasData(VillagerAttachments.VETERAN_DATA.get())
                && !villager.hasData(VillagerAttachments.SOLDIER_DATA.get());
    }

    private static double distanceToBed(Villager villager, net.minecraft.core.BlockPos bed) {
        Vec3 bedCenter = Vec3.atCenterOf(bed);
        return villager.position().distanceTo(bedCenter);
    }
}
