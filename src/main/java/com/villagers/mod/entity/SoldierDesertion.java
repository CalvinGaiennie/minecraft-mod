package com.villagers.mod.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.combat.SoldierCombat;
import com.villagers.mod.util.SoldierStructureHelper;

public class SoldierDesertion {
    public static final int DAYS_UNTIL_HUNGRY_DESERTION = 7;
    public static final int NIGHTS_UNTIL_HOMELESS_DESERTION = 3;
    public static final long TICKS_PER_DAY = 24000;

    public static boolean wouldDesertFromHomelessness(SoldierData data) {
        return data.getHomelessNights() >= NIGHTS_UNTIL_HOMELESS_DESERTION;
    }

    public static boolean wouldDesertFromHunger(SoldierData data, long currentDay, boolean messStocked) {
        if (messStocked) {
            return false;
        }
        if (data.getLastFoodDay() < 0) {
            return false;
        }
        return currentDay - data.getLastFoodDay() >= DAYS_UNTIL_HUNGRY_DESERTION;
    }

    public static void recordNightWithoutBed(Villager soldier, SoldierData data) {
        if (!(soldier.level() instanceof ServerLevel level)) {
            return;
        }
        SoldierStructureHelper.ensureAssignedBedCached(level, soldier.getUUID(), data, soldier.blockPosition());
        if (SoldierStructureHelper.soldierOwnsAssignedBed(level, soldier.getUUID(), data.getAssignedPostBed())) {
            data.setHomelessNights(0);
        } else {
            data.setHomelessNights(data.getHomelessNights() + 1);
        }
    }

    public static boolean shouldDesertNow(Villager soldier) {
        SoldierData data = soldier.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null || soldier.level().isClientSide) {
            return false;
        }
        long currentDay = soldier.level().getDayTime() / TICKS_PER_DAY;
        boolean messStocked = SoldierStructureHelper.hasStockedMessStation(soldier.level(), soldier.blockPosition());
        if (messStocked) {
            data.setLastFoodDay(currentDay);
        }
        if (wouldDesertFromHomelessness(data)) {
            return true;
        }
        return wouldDesertFromHunger(data, currentDay, messStocked);
    }

    public static void desert(Villager soldier) {
        SoldierData data = soldier.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (soldier.level() instanceof ServerLevel level) {
            if (data != null) {
                SoldierStructureHelper.releaseSoldierBed(level, soldier.getUUID(), data.getAssignedPostBed());
            }
            com.villagers.mod.combat.RampartClaims.get(level).releaseDefender(soldier.getUUID());
        }
        SoldierCombat.disableCombatGoals(soldier);
        soldier.removeData(VillagerAttachments.SOLDIER_DATA.get());
        SoldierLabels.clear(soldier);
        com.villagers.mod.threat.BanditService.tryConvertDeserter(soldier);
    }
}
