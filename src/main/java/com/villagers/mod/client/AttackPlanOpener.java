package com.villagers.mod.client;

import net.minecraft.client.Minecraft;

import com.villagers.mod.network.AttackPlanOpenPayload;

public final class AttackPlanOpener {
    private AttackPlanOpener() {
    }

    public static void open(AttackPlanOpenPayload payload) {
        Minecraft.getInstance().setScreen(new AttackPlanScreen(payload));
    }
}
