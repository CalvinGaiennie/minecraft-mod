package com.villagers.mod.entity;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.Blocks;

public class SoldierBehavior {
    private static final int MEALS_PER_DAY = 2;
    private static final int TICKS_PER_DAY = 24000;
    private static final long MEAL_INTERVAL = TICKS_PER_DAY / MEALS_PER_DAY;

    public static void updateDailyRoutine(Villager soldier) {
        if (soldier.getCommandSenderWorld() == null || soldier.getCommandSenderWorld().isClientSide) {
            return;
        }

        SoldierData data = soldier.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null) return;

        long dayTime = soldier.getCommandSenderWorld().getDayTime() % TICKS_PER_DAY;

        if (shouldEat(dayTime)) {
            eatMeal(soldier);
        }

        if (shouldSleep(dayTime)) {
            sleepInBed(soldier);
        }
    }

    private static boolean shouldEat(long dayTime) {
        long meal1Start = MEAL_INTERVAL / 2;
        long meal2Start = MEAL_INTERVAL + (MEAL_INTERVAL / 2);
        int mealDuration = 2000;

        return (dayTime >= meal1Start && dayTime < meal1Start + mealDuration) ||
               (dayTime >= meal2Start && dayTime < meal2Start + mealDuration);
    }

    private static boolean shouldSleep(long dayTime) {
        long sleepStart = 13000;
        long sleepEnd = 23000;
        return dayTime >= sleepStart && dayTime < sleepEnd;
    }

    private static void eatMeal(Villager soldier) {
        if (soldier.getHealth() < soldier.getMaxHealth()) {
            soldier.heal(2.0f);
        }
    }

    private static void sleepInBed(Villager soldier) {
        if (soldier.getCommandSenderWorld() == null) return;

        int x = soldier.getBlockX();
        int y = soldier.getBlockY();
        int z = soldier.getBlockZ();

        for (int dx = -16; dx <= 16; dx++) {
            for (int dy = -5; dy <= 5; dy++) {
                for (int dz = -16; dz <= 16; dz++) {
                    var block = soldier.getCommandSenderWorld().getBlockState(
                            new net.minecraft.core.BlockPos(x + dx, y + dy, z + dz)).getBlock();
                    if (block instanceof com.villagers.mod.block.PostBedBlock) {
                        soldier.setPos(x + dx + 0.5, y + dy + 0.5, z + dz + 0.5);
                        return;
                    }
                }
            }
        }
    }
}
