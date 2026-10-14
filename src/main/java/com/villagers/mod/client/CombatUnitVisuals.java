package com.villagers.mod.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.combat.SoldierRanks;
import com.villagers.mod.entity.VillagerAttachments;

/** Client-side identification of combat villagers for overlays. */
public final class CombatUnitVisuals {
    private CombatUnitVisuals() {
    }

    /** Soldiers, militia, and bandits share the nameplate HP bar when injured. */
    public static boolean showsCombatHealthBar(Villager villager) {
        return isSoldierOrMilitia(villager) || isBandit(villager);
    }

    public static boolean isBandit(Villager villager) {
        if (!villager.isAlive()) {
            return false;
        }
        return villager.hasData(VillagerAttachments.BANDIT_DATA.get());
    }

    public static boolean isSoldierOrMilitia(Villager villager) {
        if (!villager.isAlive()) {
            return false;
        }
        Component custom = villager.getCustomName();
        if (custom == null) {
            return false;
        }
        String name = custom.getString();
        if (isBanditName(name)) {
            return false;
        }
        if (name.startsWith("Militia") || name.startsWith("Seasoned Militia")) {
            return true;
        }
        for (SoldierRanks.Rank rank : SoldierRanks.Rank.values()) {
            if (name.startsWith(rank.label())) {
                return true;
            }
        }
        if (name.contains(" kills)")) {
            return true;
        }
        return villager.hasData(VillagerAttachments.VILLAGER_KIT.get());
    }

    private static boolean isBanditName(String name) {
        return name.equals(Component.translatable("entity.villagers.bandit").getString())
                || name.startsWith("Bandit ");
    }
}
