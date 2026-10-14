package com.villagers.mod.player;

import net.minecraft.server.level.ServerLevel;

import com.villagers.mod.village.VillageData;

import java.util.UUID;

public final class VillageOpsPermission {
    private VillageOpsPermission() {
    }

    public static boolean mayManageVillage(ServerLevel level, UUID playerId, VillageData village) {
        if (village.getOwnerId().equals(playerId)) {
            return true;
        }
        AllyRank rank = AllySavedData.get(level).getRank(village.getOwnerId(), playerId);
        return rank != null && rank.canManageVillageOps();
    }
}
