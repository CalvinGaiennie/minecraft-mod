package com.villagers.mod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;

public final class MusterRollHelper {
    private MusterRollHelper() {
    }

    public record Summary(int soldierCount, int totalKills) {
    }

    public static Summary summarize(ServerLevel level, Player player, int radius) {
        int soldierCount = 0;
        int totalKills = 0;
        double radiusSq = (double) radius * radius;

        for (var entity : level.getAllEntities()) {
            if (entity instanceof Villager villager && villager.distanceToSqr(player) <= radiusSq) {
                SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
                if (data != null) {
                    soldierCount++;
                    totalKills += data.getKills();
                }
            }
        }
        return new Summary(soldierCount, totalKills);
    }

    public static Summary summarizeNear(ServerLevel level, BlockPos center, int radius) {
        int soldierCount = 0;
        int totalKills = 0;
        double radiusSq = (double) radius * radius;

        for (var entity : level.getAllEntities()) {
            if (entity instanceof Villager villager && villager.blockPosition().distSqr(center) <= radiusSq) {
                SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
                if (data != null) {
                    soldierCount++;
                    totalKills += data.getKills();
                }
            }
        }
        return new Summary(soldierCount, totalKills);
    }
}
