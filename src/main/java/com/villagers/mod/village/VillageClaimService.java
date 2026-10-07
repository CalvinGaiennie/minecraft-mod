package com.villagers.mod.village;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import com.villagers.mod.block.MessStationBlock;

import java.util.UUID;

/** Creates hamlet claims at mess stations and backfills missing claims for older worlds. */
public final class VillageClaimService {
    /** Mess stations within this range of the player can be claimed when opening the muster roll. */
    public static final int BACKFILL_RADIUS = 128;

    private VillageClaimService() {
    }

    public static boolean tryCreateHamletAtMess(ServerLevel level, UUID ownerId, String ownerDisplayName, BlockPos messPos) {
        VillageManager manager = VillageManager.get(level);
        if (manager.findVillageAt(messPos.getX(), messPos.getY(), messPos.getZ()) != null) {
            return false;
        }
        if (!manager.canClaim(ownerId, messPos.getX(), messPos.getZ(), VillageManager.HAMLET_RADIUS)) {
            return false;
        }
        String villageName = "Village of " + ownerDisplayName;
        VillageData village = new VillageData(villageName, ownerId, messPos.getX(), messPos.getY(), messPos.getZ());
        village.setRadius(VillageManager.HAMLET_RADIUS);
        manager.registerVillage(village, level);
        return true;
    }

    /** Registers claims for nearby mess stations that never got a hamlet entry (e.g. pre-claim worlds). */
    public static void backfillNearbyMessClaims(ServerLevel level, Player player) {
        BlockPos center = player.blockPosition();
        int r = BACKFILL_RADIUS;
        UUID ownerId = player.getUUID();
        String name = player.getName().getString();
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-r, -32, -r), center.offset(r, 32, r))) {
            if (!(level.getBlockState(pos).getBlock() instanceof MessStationBlock)) {
                continue;
            }
            tryCreateHamletAtMess(level, ownerId, name, pos);
        }
    }
}
