package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.util.SoldierStructureHelper;

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

        long gameTime = soldier.level().getGameTime();
        if (data.getAwakenUntilGameTime() > gameTime) {
            pathToRampart(soldier);
            return;
        }

        long dayTime = soldier.level().getDayTime() % TICKS_PER_DAY;

        if (shouldEat(dayTime)) {
            eatMeal(soldier);
            return;
        }

        if (shouldSleep(dayTime)) {
            sleepInBed(soldier);
            return;
        }

        pathToRampart(soldier);
    }

    private static void pathToRampart(Villager soldier) {
        SoldierStructureHelper.findNearestRampart(soldier.level(), soldier.blockPosition()).ifPresent(rampart -> {
            PathNavigation navigation = soldier.getNavigation();
            if (navigation.isDone()) {
                navigation.moveTo(rampart.getX() + 0.5, rampart.getY(), rampart.getZ() + 0.5, 0.6);
            }
        });
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

    private static void eatMeal(Villager soldier) {
        if (soldier.getHealth() < soldier.getMaxHealth()) {
            soldier.heal(3.0f);
        }
    }

    private static void sleepInBed(Villager soldier) {
        for (BlockPos pos : BlockPos.betweenClosed(
                soldier.blockPosition().offset(-16, -5, -16),
                soldier.blockPosition().offset(16, 5, 16))) {
            if (soldier.level().getBlockEntity(pos) instanceof com.villagers.mod.block.entity.PostBedBlockEntity bed
                    && soldier.getUUID().equals(bed.getOccupant())) {
                soldier.getNavigation().moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.6);
                return;
            }
        }
    }
}
