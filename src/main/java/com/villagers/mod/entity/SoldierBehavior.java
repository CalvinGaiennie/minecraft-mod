package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.entity.VillagerAttachments;

import com.villagers.mod.block.entity.MessStationBlockEntity;
import com.villagers.mod.defense.NightwatchGear;
import com.villagers.mod.combat.RampartPathing;
import com.villagers.mod.util.SoldierStructureHelper;
import com.villagers.mod.war.CampaignService;
import com.villagers.mod.war.HornCommandService;
import com.villagers.mod.war.SoldierSignalType;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class SoldierBehavior {
    private static final int MEALS_PER_DAY = 2;
    private static final int TICKS_PER_DAY = 24000;
    private static final long MEAL_INTERVAL = TICKS_PER_DAY / MEALS_PER_DAY;

    public static void signalAwaken(Villager soldier, long gameTime) {
        SoldierData data = soldier.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data != null) {
            data.setAwakenUntilGameTime(gameTime + 200);
        }
    }

    public static void updateDailyRoutine(Villager soldier) {
        if (soldier.level().isClientSide) {
            return;
        }

        SoldierData data = soldier.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null) {
            return;
        }

        if (soldier.isSleeping()) {
            SoldierBedSleep.applyRestHeal(soldier, data);
            if (SoldierBedSleep.restsUndisturbedByHostiles(soldier, data)) {
                return;
            }
            SoldierBedSleep.wakeNow(soldier);
        }

        long gameTime = soldier.level().getGameTime();
        if (HornCommandService.isRallyActive(soldier, gameTime)) {
            Vec3 rally = HornCommandService.getRallyDestination(soldier, gameTime);
            if (rally != null) {
                soldier.getNavigation().moveTo(rally.x, rally.y, rally.z, 1.0);
            }
            return;
        }

        if (data.isOnCampaign()) {
            if (shouldEat(soldier.level().getDayTime() % TICKS_PER_DAY)) {
                eatMealCampaign(soldier, data);
            } else {
                CampaignService.tickSoldier(soldier, data);
            }
            return;
        }
        if (data.getActiveSignal() == SoldierSignalType.GUARD_CHIEF && data.getSignalUntilGameTime() > gameTime) {
            return;
        }
        if (data.getAwakenUntilGameTime() > gameTime) {
            SoldierBedSleep.wakeIfNeeded(soldier, false);
            GuardDutyPathing.tick(soldier);
            return;
        }

        long dayTime = soldier.level().getDayTime() % TICKS_PER_DAY;
        boolean night = dayTime >= 13000 && dayTime < 23000;
        boolean nightwatch = NightwatchGear.hasSquadNightwatch(soldier);

        if (night && nightwatch) {
            patrolAtNight(soldier);
            return;
        }

        if (SoldierBedSleep.restsUndisturbedByHostiles(soldier, data)) {
            SoldierBedSleep.tryRestSleep(soldier, data);
            if (soldier.isSleeping()) {
                SoldierBedSleep.applyRestHeal(soldier, data);
            }
            return;
        }

        if (shouldEat(dayTime)) {
            eatMeal(soldier, data);
            return;
        }

        SoldierBedSleep.wakeIfNeeded(soldier, shouldSleep(dayTime));

        if (shouldSleep(dayTime)) {
            SoldierBedSleep.tryRestSleep(soldier, data);
            if (soldier.isSleeping()) {
                SoldierBedSleep.applyRestHeal(soldier, data);
            }
            return;
        }

        GuardDutyPathing.tick(soldier);
    }

    private static boolean shouldEat(long dayTime) {
        long meal1Start = MEAL_INTERVAL / 2;
        long meal2Start = MEAL_INTERVAL + (MEAL_INTERVAL / 2);
        int mealDuration = 2000;

        return (dayTime >= meal1Start && dayTime < meal1Start + mealDuration)
                || (dayTime >= meal2Start && dayTime < meal2Start + mealDuration);
    }

    private static boolean shouldSleep(long dayTime) {
        long sleepStart = 13000;
        long sleepEnd = 23000;
        return dayTime >= sleepStart && dayTime < sleepEnd;
    }

    private static void eatMeal(Villager soldier, SoldierData data) {
        if (!(soldier.level() instanceof ServerLevel level)) {
            return;
        }
        var messPos = SoldierStructureHelper.findStockedMessStation(level, soldier.blockPosition());
        if (messPos.isPresent()) {
            BlockPos pos = messPos.get();
            if (soldier.blockPosition().distSqr(pos) > 4) {
                PostReturnPathing.returnToMess(soldier, level);
                return;
            }
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MessStationBlockEntity mess && mess.consumeOneFood()) {
                long day = level.getDayTime() / TICKS_PER_DAY;
                data.setLastFoodDay(day);
                soldier.heal(4.0f);
            }
        } else if (soldier.getHealth() < soldier.getMaxHealth()) {
            soldier.heal(1.0f);
        }
    }

    private static void eatMealCampaign(Villager soldier, SoldierData data) {
        if (CampaignService.tryEatAtCamp(soldier, data)) {
            long day = soldier.level().getDayTime() / TICKS_PER_DAY;
            data.setLastFoodDay(day);
            soldier.heal(4.0f);
        }
        CampaignService.tickSoldier(soldier, data);
    }

    private static void patrolAtNight(Villager soldier) {
        if (NightwatchGear.hasRoyalGuardChest(soldier)) {
            SoldierStructureHelper.findNearestRoyalGuardPost(soldier.level(), soldier.blockPosition())
                    .ifPresent(post -> moveTo(soldier, post));
            return;
        }
        RampartPathing.tickSeekNearestRampart(soldier, 0.65);
    }

    private static void moveTo(Villager soldier, BlockPos pos) {
        if (soldier.getNavigation().isDone()) {
            soldier.getNavigation().moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.65);
        }
    }

}
