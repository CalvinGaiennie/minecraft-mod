package com.villagers.mod.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.Villager;

public final class SoldierLabels {
    private SoldierLabels() {
    }

    public static void apply(Villager villager, SoldierData data) {
        villager.setCustomName(Component.literal(
                VillagerPersonalNames.soldierLabel(data.getRank(), data.getDisplayName(), data.getKills())));
        villager.setCustomNameVisible(true);
    }

    public static void clear(Villager villager) {
        villager.setCustomName(null);
        villager.setCustomNameVisible(false);
    }
}
