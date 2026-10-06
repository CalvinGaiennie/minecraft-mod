package com.villagers.mod.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import com.villagers.mod.util.MusterRollHelper;

public final class VillagersModNetwork {
    private VillagersModNetwork() {
    }

    public static void sendMusterRoll(ServerPlayer player) {
        MusterRollPayload payload = MusterRollHelper.buildPayload(player.serverLevel(), player);
        PacketDistributor.sendToPlayer(player, payload);
    }
}
