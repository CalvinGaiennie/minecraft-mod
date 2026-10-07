package com.villagers.mod.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import com.villagers.mod.client.MusterRollScreenOpener;

/** Single registration for muster roll (server sends, client opens GUI). */
public final class VillagersModPayloadRegistration {
    private VillagersModPayloadRegistration() {
    }

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToClient(MusterRollPayload.TYPE, MusterRollPayload.STREAM_CODEC, (payload, context) ->
                        context.enqueueWork(() -> MusterRollScreenOpener.open(payload)));
    }
}
