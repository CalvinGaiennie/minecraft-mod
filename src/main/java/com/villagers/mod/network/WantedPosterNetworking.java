package com.villagers.mod.network;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import com.villagers.mod.block.entity.WantedPosterBlockEntity;
import com.villagers.mod.player.VillageOpsPermission;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;
import com.villagers.mod.war.WantedPosterNames;

import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public final class WantedPosterNetworking {
    private WantedPosterNetworking() {
    }

    public static void openEditor(ServerPlayer player, net.minecraft.core.BlockPos pos, List<String> lines) {
        PacketDistributor.sendToPlayer(player, new WantedPosterOpenPayload(pos, lines));
    }

    public static void handleSave(ServerPlayer player, WantedPosterSavePayload payload) {
        ServerLevel level = player.serverLevel();
        if (!(level.getBlockEntity(payload.pos()) instanceof WantedPosterBlockEntity poster)) {
            return;
        }
        VillageData village = VillageManager.get(level).findVillageAt(
                payload.pos().getX(), payload.pos().getY(), payload.pos().getZ());
        if (village == null || !VillageOpsPermission.mayManageVillage(level, player.getUUID(), village)) {
            return;
        }
        var resolved = WantedPosterNames.resolveNames(level, payload.lines());
        poster.setWantedFromResolved(resolved);
    }
}
