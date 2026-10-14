package com.villagers.mod.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import com.villagers.mod.client.AllyBookOpener;
import com.villagers.mod.client.AttackPlanOpener;
import com.villagers.mod.client.MusterRollBookOpener;
import com.villagers.mod.client.VillageAtlasScreenOpener;
import com.villagers.mod.client.WantedPosterOpener;
import com.villagers.mod.network.ExplorationTerrainBatchPayload;

public final class VillagersModPayloadRegistration {
    private VillagersModPayloadRegistration() {
    }

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToClient(MusterRollBookPayload.TYPE, MusterRollBookPayload.STREAM_CODEC, (payload, context) ->
                context.enqueueWork(() -> MusterRollBookOpener.open(payload)));
        registrar.playToClient(VillageAtlasPayload.TYPE, VillageAtlasPayload.STREAM_CODEC, (payload, context) ->
                context.enqueueWork(() -> VillageAtlasScreenOpener.open(payload)));
        registrar.playToClient(
                ExplorationTerrainBatchPayload.TYPE,
                ExplorationTerrainBatchPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() ->
                        com.villagers.mod.client.AtlasExplorationClientCache.applyBatch(payload)));
        registrar.playToClient(WantedPosterOpenPayload.TYPE, WantedPosterOpenPayload.STREAM_CODEC, (payload, context) ->
                context.enqueueWork(() -> WantedPosterOpener.open(payload)));
        registrar.playToServer(WantedPosterSavePayload.TYPE, WantedPosterSavePayload.STREAM_CODEC, (payload, context) ->
                context.enqueueWork(() -> {
                    if (context.player() instanceof net.minecraft.server.level.ServerPlayer player) {
                        WantedPosterNetworking.handleSave(player, payload);
                    }
                }));
        registrar.playToClient(AllyBookOpenPayload.TYPE, AllyBookOpenPayload.STREAM_CODEC, (payload, context) ->
                context.enqueueWork(() -> AllyBookOpener.open(payload)));
        registrar.playToServer(AllyBookActionPayload.TYPE, AllyBookActionPayload.STREAM_CODEC, (payload, context) ->
                context.enqueueWork(() -> {
                    if (context.player() instanceof net.minecraft.server.level.ServerPlayer player) {
                        AllyBookNetworking.handleAction(player, payload);
                    }
                }));
        registrar.playToClient(AttackPlanOpenPayload.TYPE, AttackPlanOpenPayload.STREAM_CODEC, (payload, context) ->
                context.enqueueWork(() -> AttackPlanOpener.open(payload)));
        registrar.playToServer(AttackPlanSavePayload.TYPE, AttackPlanSavePayload.STREAM_CODEC, (payload, context) ->
                context.enqueueWork(() -> {
                    if (context.player() instanceof net.minecraft.server.level.ServerPlayer player) {
                        AttackPlanNetworking.handleSave(player, payload);
                    }
                }));
        registrar.playToServer(SquadBoxActionPayload.TYPE, SquadBoxActionPayload.STREAM_CODEC, (payload, context) ->
                context.enqueueWork(() -> {
                    if (context.player() instanceof net.minecraft.server.level.ServerPlayer player) {
                        SquadBoxNetworking.handleAction(player, payload);
                    }
                }));
    }
}
