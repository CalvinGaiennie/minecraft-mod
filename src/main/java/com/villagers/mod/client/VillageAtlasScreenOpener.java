package com.villagers.mod.client;

import net.minecraft.client.Minecraft;

import com.villagers.mod.network.VillageAtlasPayload;

public final class VillageAtlasScreenOpener {
    private VillageAtlasScreenOpener() {
    }

    public static void open(VillageAtlasPayload payload) {
        Minecraft.getInstance().setScreen(new VillageAtlasScreen(payload));
    }
}
