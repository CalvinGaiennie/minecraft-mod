package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;
import com.villagers.mod.village.VillageZone;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class VillageSoldierCounts {
    private VillageSoldierCounts() {
    }

    public static int countSoldiersInVillage(ServerLevel level, VillageData village) {
        int radius = Math.max(village.getRadius(), VillageZone.DEFAULT_TAX_RADIUS);
        long radiusSq = (long) radius * radius;
        int cx = village.getMarkerX();
        int cz = village.getMarkerZ();
        int count = 0;
        for (Villager entity : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(cx - radius, -64, cz - radius, cx + radius, 320, cz + radius))) {
            if (!entity.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                continue;
            }
            SoldierData data = entity.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data == null || data.isOnCampaign()) {
                continue;
            }
            long dx = entity.getBlockX() - cx;
            long dz = entity.getBlockZ() - cz;
            if (dx * dx + dz * dz <= radiusSq) {
                count++;
            }
        }
        return count;
    }

    public static Optional<VillageData> findVillageAt(ServerLevel level, BlockPos pos) {
        VillageManager manager = VillageManager.get(level);
        return Optional.ofNullable(manager.findVillageAt(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static Optional<VillageData> largestOwnedVillage(ServerLevel level, UUID ownerId) {
        List<VillageData> owned = VillageManager.get(level).getVillagesOwnedBy(ownerId);
        if (owned.isEmpty()) {
            return Optional.empty();
        }
        return owned.stream().max(Comparator
                .comparingInt((VillageData v) -> countSoldiersInVillage(level, v))
                .thenComparingInt(v -> VillageManager.countMembersInVillage(level, v))
                .thenComparing(v -> v.getVillageId().toString()));
    }

    public static boolean playerInVillage(ServerLevel level, UUID playerId, VillageData village) {
        var player = level.getServer().getPlayerList().getPlayer(playerId);
        if (player == null) {
            return false;
        }
        int radius = Math.max(village.getRadius(), VillageZone.DEFAULT_TAX_RADIUS);
        long dx = player.getBlockX() - village.getMarkerX();
        long dz = player.getBlockZ() - village.getMarkerZ();
        return dx * dx + dz * dz <= (long) radius * radius;
    }
}
