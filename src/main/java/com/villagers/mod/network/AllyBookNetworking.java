package com.villagers.mod.network;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import com.villagers.mod.player.AllyRank;
import com.villagers.mod.player.AllySavedData;
import com.villagers.mod.war.WantedPosterNames;

import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public final class AllyBookNetworking {
    private AllyBookNetworking() {
    }

    public static void open(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        Map<UUID, AllyRank> ranked = AllySavedData.get(level).rankedAllies(player.getUUID());
        var entries = new ArrayList<AllyBookOpenPayload.Entry>();
        for (var entry : ranked.entrySet()) {
            String name = WantedPosterNames.displayName(level.getServer(), entry.getKey());
            entries.add(new AllyBookOpenPayload.Entry(
                    entry.getKey().toString(), name, entry.getValue().name()));
        }
        PacketDistributor.sendToPlayer(player, new AllyBookOpenPayload(entries));
    }

    public static void handleAction(ServerPlayer player, AllyBookActionPayload payload) {
        ServerLevel level = player.serverLevel();
        AllySavedData data = AllySavedData.get(level);
        UUID self = player.getUUID();
        UUID target = resolveTarget(level, payload.target());
        if (target == null || target.equals(self)) {
            return;
        }
        switch (payload.action()) {
            case ADD -> {
                AllyRank rank = AllyRank.fromString(payload.rank());
                if (data.addAlly(self, target, rank)) {
                    player.displayClientMessage(
                            Component.translatable("message.villagers.ally_added", payload.target()), true);
                }
            }
            case REMOVE -> {
                if (data.removeAlly(self, target)) {
                    player.displayClientMessage(
                            Component.translatable("message.villagers.ally_removed", payload.target()), true);
                }
            }
            case SET_RANK -> {
                AllyRank rank = AllyRank.fromString(payload.rank());
                if (data.setRank(self, target, rank)) {
                    player.displayClientMessage(
                            Component.translatable("message.villagers.ally_rank_set", payload.target(), rank.name()),
                            true);
                }
            }
        }
        open(player);
    }

    private static UUID resolveTarget(ServerLevel level, String raw) {
        var list = WantedPosterNames.resolveNames(level, java.util.List.of(raw));
        return list.isEmpty() ? null : list.getFirst();
    }
}
