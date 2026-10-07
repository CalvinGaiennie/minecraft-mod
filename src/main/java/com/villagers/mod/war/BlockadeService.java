package com.villagers.mod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.village.VillageManager;

public final class BlockadeService {
    public static final int BLOCKADE_RADIUS = 64;
    public static final int MIN_SIEGE_SOLDIERS = 15;

    private BlockadeService() {
    }

    public static boolean isVillageBlockaded(ServerLevel level, BlockPos villageCenter) {
        var village = VillageManager.get(level).findVillageAt(villageCenter.getX(), villageCenter.getY(), villageCenter.getZ());
        if (village == null) {
            return false;
        }
        BlockPos marker = new BlockPos(village.getMarkerX(), village.getMarkerY(), village.getMarkerZ());
        for (var camp : CampaignState.get(level).camps().values()) {
            if (camp.dimension() != level.dimension()) {
                continue;
            }
            if (marker.distSqr(camp.pos()) > BLOCKADE_RADIUS * BLOCKADE_RADIUS) {
                continue;
            }
            if (countCampaignSoldiersNear(level, camp.pos()) >= MIN_SIEGE_SOLDIERS) {
                return true;
            }
        }
        return false;
    }

    private static int countCampaignSoldiersNear(ServerLevel level, BlockPos campPos) {
        int count = 0;
        for (Villager villager : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(campPos).inflate(32))) {
            if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                continue;
            }
            var data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null && data.isOnCampaign()) {
                count++;
            }
        }
        return count;
    }
}
