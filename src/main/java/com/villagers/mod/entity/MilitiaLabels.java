package com.villagers.mod.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.Villager;

public final class MilitiaLabels {
    private MilitiaLabels() {
    }

    public static void apply(Villager villager, MilitiaData data) {
        String rank = data.isSeasoned() ? "Seasoned Militia" : "Militia";
        villager.setCustomName(Component.literal(
                VillagerPersonalNames.militiaLabel(rank, data.getDisplayName(), data.getKills())));
        villager.setCustomNameVisible(true);
    }

    public static void clear(Villager villager) {
        villager.setCustomName(null);
        villager.setCustomNameVisible(false);
    }
}
