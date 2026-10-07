package com.villagers.mod.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.block.entity.MessStationBlockEntity;
import com.villagers.mod.util.SoldierStructureHelper;

public final class MilitiaBehavior {
    private static final long TICKS_PER_DAY = 24000L;
    private static final int DAYS_WITHOUT_FOOD = 2;

    private MilitiaBehavior() {
    }

    public static void tickDaily(Villager villager, MilitiaData data, boolean newDay) {
        if (!newDay || !(villager.level() instanceof ServerLevel level)) {
            return;
        }
        MilitiaHomeSync.syncHomeBedFromVillager(villager, data);
        long day = level.getDayTime() / TICKS_PER_DAY;
        boolean stocked = SoldierStructureHelper.hasStockedMessStation(level, villager.blockPosition());
        if (stocked) {
            tryEatOneMeal(villager, data, level);
            data.setLastFoodDay(day);
        } else if (data.getLastFoodDay() >= 0 && day - data.getLastFoodDay() >= DAYS_WITHOUT_FOOD) {
            MilitiaConversion.disarm(villager, true);
        }
    }

    public static void tickGuard(Villager villager) {
        if (villager.level().isClientSide) {
            return;
        }
        if (villager instanceof net.minecraft.world.entity.Mob mob && mob.getTarget() != null) {
            return;
        }
        GuardDutyPathing.tick(villager);
    }

    private static void tryEatOneMeal(Villager villager, MilitiaData data, ServerLevel level) {
        var messPos = SoldierStructureHelper.findStockedMessStation(level, villager.blockPosition());
        if (messPos.isEmpty()) {
            return;
        }
        var be = level.getBlockEntity(messPos.get());
        if (be instanceof MessStationBlockEntity mess && mess.consumeOneFood()) {
            villager.heal(2.0f);
        }
    }
}
