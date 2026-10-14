package com.villagers.mod.client;

import net.minecraft.client.Minecraft;

import com.villagers.mod.network.AllyBookOpenPayload;

public final class AllyBookOpener {
    private AllyBookOpener() {
    }

    public static void open(AllyBookOpenPayload payload) {
        Minecraft.getInstance().setScreen(new AllyBookScreen(payload));
    }
}
