package com.villagers.mod.village;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.player.AllyService;

import java.util.UUID;

public final class VillageProtection {
    private VillageProtection() {
    }

    public static int soldiersInsideVillage(ServerLevel level, VillageData village) {
        long radiusSq = (long) village.getRadius() * village.getRadius();
        int centerX = village.getMarkerX();
        int centerZ = village.getMarkerZ();
        int count = 0;
        for (Villager villager : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(
                        centerX - village.getRadius(), village.getMarkerY() - 64, centerZ - village.getRadius(),
                        centerX + village.getRadius(), village.getMarkerY() + 64, centerZ + village.getRadius()))) {
            if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                continue;
            }
            long dx = villager.getBlockX() - centerX;
            long dz = villager.getBlockZ() - centerZ;
            if (dx * dx + dz * dz > radiusSq) {
                continue;
            }
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null && !data.isOnCampaign()) {
                count++;
            }
        }
        return count;
    }

    /** Mining slowdown divisor for non-owners (1 = none). */
    public static float breakSlowdownFactor(int homeSoldiers) {
        if (homeSoldiers >= 25) {
            return 10.0f;
        }
        if (homeSoldiers >= 10) {
            return 5.0f;
        }
        if (homeSoldiers >= 1) {
            return 3.0f;
        }
        return 1.0f;
    }

    public static boolean isProtectedFor(Player player, ServerLevel level, BlockPos pos) {
        VillageData village = VillageManager.get(level).findVillageAt(pos.getX(), pos.getY(), pos.getZ());
        if (village == null || !village.isActive()) {
            return false;
        }
        return !mayBypassProtection(level, player.getUUID(), village);
    }

    public static boolean mayBypassProtection(ServerLevel level, UUID playerId, VillageData village) {
        if (playerId.equals(village.getOwnerId())) {
            return true;
        }
        return AllyService.isAlly(level, village.getOwnerId(), playerId);
    }
}
