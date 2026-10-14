package com.villagers.mod.network;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.menu.SquadBoxMenu;

public final class SquadBoxNetworking {
    private SquadBoxNetworking() {
    }

    public static void handleAction(ServerPlayer player, SquadBoxActionPayload payload) {
        ServerLevel level = player.serverLevel();
        if (!(level.getBlockEntity(payload.boxPos()) instanceof RecruiterBlockEntity box)) {
            return;
        }
        if (player.distanceToSqr(payload.boxPos().getX() + 0.5, payload.boxPos().getY() + 0.5, payload.boxPos().getZ() + 0.5) > 64) {
            return;
        }
        if (!(player.containerMenu instanceof SquadBoxMenu menu) || !menu.ownsBox(box)) {
            return;
        }
        menu.handleButton(player, payload.actionId());
    }
}
