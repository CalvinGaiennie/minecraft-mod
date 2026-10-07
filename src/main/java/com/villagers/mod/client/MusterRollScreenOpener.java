package com.villagers.mod.client;

import net.minecraft.client.Minecraft;

import com.villagers.mod.network.MusterRollPayload;

public final class MusterRollScreenOpener {
    private MusterRollScreenOpener() {
    }

    public static void open(MusterRollPayload payload) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen(new MusterRollScreen(payload));
    }
}
