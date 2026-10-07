package com.villagers.mod.player;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class AllyService {
    private AllyService() {
    }

    public static boolean isAlly(ServerLevel level, UUID ownerId, UUID otherId) {
        if (ownerId == null || otherId == null) {
            return false;
        }
        return AllySavedData.get(level).isAlly(ownerId, otherId);
    }

    public static boolean isAlly(ServerPlayer owner, ServerPlayer other) {
        return isAlly(owner.serverLevel(), owner.getUUID(), other.getUUID());
    }
}
