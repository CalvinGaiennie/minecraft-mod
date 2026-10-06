package com.villagers.mod.player;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class EnlistmentService {
    private EnlistmentService() {
    }

    public static boolean isEnlisted(Player player) {
        if (player.level().isClientSide || !(player.level() instanceof ServerLevel level)) {
            return true;
        }
        return EnlistmentSavedData.get(level).isEnlisted(player.getUUID());
    }

    public static void enlistOnFirstMess(net.minecraft.world.entity.player.Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            EnlistmentSavedData.get(serverPlayer.serverLevel()).enlist(serverPlayer.getUUID());
        }
    }

    public static void enlist(ServerPlayer player) {
        EnlistmentSavedData.get(player.serverLevel()).enlist(player.getUUID());
    }

    public static void optOut(ServerPlayer player) {
        EnlistmentSavedData.get(player.serverLevel()).optOut(player.getUUID());
    }
}
