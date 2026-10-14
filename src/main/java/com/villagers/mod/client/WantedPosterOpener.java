package com.villagers.mod.client;

import net.minecraft.client.Minecraft;

import com.villagers.mod.network.WantedPosterOpenPayload;

public final class WantedPosterOpener {
    private WantedPosterOpener() {
    }

    public static void open(WantedPosterOpenPayload payload) {
        Minecraft.getInstance().setScreen(new WantedPosterEditScreen(payload.pos(), payload.lines()));
    }
}
