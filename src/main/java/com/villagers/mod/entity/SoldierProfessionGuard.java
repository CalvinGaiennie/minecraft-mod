package com.villagers.mod.entity;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.trading.MerchantOffers;

/** Soldiers and militia must stay unemployed — no villager trades while in service. */
public final class SoldierProfessionGuard {
    private SoldierProfessionGuard() {
    }

    public static boolean isCombatRole(Villager villager) {
        return villager.hasData(VillagerAttachments.SOLDIER_DATA.get())
                || villager.hasData(VillagerAttachments.MILITIA_DATA.get())
                || villager.hasData(VillagerAttachments.BANDIT_DATA.get());
    }

    public static void stripJobIfNeeded(Villager villager) {
        if (villager.getVillagerData().getProfession() == VillagerProfession.NONE) {
            return;
        }
        villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.NONE).setLevel(1));
    }

    public static void clearTradeOffers(Villager villager) {
        villager.setOffers(new MerchantOffers());
    }

    public static void stripCommerceForCombatRole(Villager villager) {
        stripJobIfNeeded(villager);
        clearTradeOffers(villager);
    }

    public static void enforceForCombatRole(Villager villager) {
        if (isCombatRole(villager)) {
            stripCommerceForCombatRole(villager);
        }
    }
}
