package com.villagers.mod.entity;

import net.minecraft.world.entity.npc.Villager;

public class SoldierDesertion {
    private static final int DAYS_UNTIL_HUNGRY_DESERTION = 7;
    private static final int NIGHTS_UNTIL_HOMELESS_DESERTION = 3;
    private static final long TICKS_PER_DAY = 24000;

    public static boolean shouldDeserve(Villager soldier) {
        SoldierData data = soldier.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null || soldier.getCommandSenderWorld() == null) {
            return false;
        }

        if (!hasPostBed(soldier)) {
            data.setHomelessNights(data.getHomelessNights() + 1);
            if (data.getHomelessNights() >= NIGHTS_UNTIL_HOMELESS_DESERTION) {
                return true;
            }
        } else {
            data.setHomelessNights(0);
        }

        if (!hasStockedMessStation(soldier)) {
            long currentDay = soldier.getCommandSenderWorld().getDayTime() / TICKS_PER_DAY;
            if (currentDay - data.getLastFoodDay() >= DAYS_UNTIL_HUNGRY_DESERTION) {
                return true;
            }
        } else {
            data.setLastFoodDay(soldier.getCommandSenderWorld().getDayTime() / TICKS_PER_DAY);
        }

        return false;
    }

    private static boolean hasPostBed(Villager soldier) {
        var level = soldier.getCommandSenderWorld();
        if (level == null) return false;

        int x = soldier.getBlockX();
        int y = soldier.getBlockY();
        int z = soldier.getBlockZ();

        for (int dx = -16; dx <= 16; dx++) {
            for (int dy = -5; dy <= 5; dy++) {
                for (int dz = -16; dz <= 16; dz++) {
                    var block = level.getBlockState(new net.minecraft.core.BlockPos(x + dx, y + dy, z + dz)).getBlock();
                    if (block instanceof com.villagers.mod.block.PostBedBlock) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean hasStockedMessStation(Villager soldier) {
        var level = soldier.getCommandSenderWorld();
        if (level == null) return false;

        int x = soldier.getBlockX();
        int y = soldier.getBlockY();
        int z = soldier.getBlockZ();

        for (int dx = -16; dx <= 16; dx++) {
            for (int dy = -5; dy <= 5; dy++) {
                for (int dz = -16; dz <= 16; dz++) {
                    var block = level.getBlockState(new net.minecraft.core.BlockPos(x + dx, y + dy, z + dz)).getBlock();
                    if (block instanceof com.villagers.mod.block.MessStationBlock) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void processDischarged(Villager soldier) {
        soldier.removeData(VillagerAttachments.SOLDIER_DATA.get());
    }
}
