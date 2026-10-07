package com.villagers.mod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.network.MusterRollPayload;
import com.villagers.mod.village.VillageClaimService;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MusterRollHelper {
    public static final int NEARBY_RADIUS = 64;

    private MusterRollHelper() {
    }

    public record Summary(int soldierCount, int totalKills) {
    }

    public static MusterRollPayload buildPayload(ServerLevel level, Player player) {
        VillageClaimService.backfillNearbyMessClaims(level, player);
        UUID ownerId = player.getUUID();
        BlockPos playerPos = player.blockPosition();
        Summary nearby = summarize(level, player, NEARBY_RADIUS);

        VillageManager manager = VillageManager.get(level);
        List<MusterRollPayload.TerritoryEntry> territories = new ArrayList<>();
        int ownedSoldiers = 0;
        int ownedKills = 0;

        for (VillageData village : manager.getVillagesOwnedBy(ownerId)) {
            Summary inVillage = summarizeInVillage(level, village);
            ownedSoldiers += inVillage.soldierCount();
            ownedKills += inVillage.totalKills();
            territories.add(new MusterRollPayload.TerritoryEntry(
                    village.getVillageName(),
                    village.getMarkerX(),
                    village.getMarkerZ(),
                    village.getRadius(),
                    inVillage.soldierCount(),
                    inVillage.totalKills()));
        }

        territories.sort((a, b) -> a.villageName().compareToIgnoreCase(b.villageName()));

        return new MusterRollPayload(
                playerPos.getX(),
                playerPos.getZ(),
                nearby.soldierCount(),
                nearby.totalKills(),
                ownedSoldiers,
                ownedKills,
                territories);
    }

    public static Summary summarizeInVillage(ServerLevel level, VillageData village) {
        int soldierCount = 0;
        int totalKills = 0;
        long radiusSq = (long) village.getRadius() * village.getRadius();
        int centerX = village.getMarkerX();
        int centerZ = village.getMarkerZ();

        for (var entity : level.getAllEntities()) {
            if (!(entity instanceof Villager villager)) {
                continue;
            }
            long dx = villager.getBlockX() - centerX;
            long dz = villager.getBlockZ() - centerZ;
            if (dx * dx + dz * dz > radiusSq) {
                continue;
            }
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null) {
                soldierCount++;
                totalKills += data.getKills();
            }
        }
        return new Summary(soldierCount, totalKills);
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
