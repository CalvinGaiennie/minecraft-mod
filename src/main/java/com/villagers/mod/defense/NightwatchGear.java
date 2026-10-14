package com.villagers.mod.defense;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;
import com.villagers.mod.squad.SquadService;

public final class NightwatchGear {
    private NightwatchGear() {
    }

    /** Squad box nightwatch toggle (nightwatch helmet item removed). */
    public static boolean hasNightwatchHelmet(Villager villager) {
        return hasSquadNightwatch(villager);
    }

    public static boolean isNightwatchHelmet(ItemStack stack) {
        return false;
    }

    public static boolean hasSquadNightwatch(Villager villager) {
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null || data.getSquadBoxPos() == null) {
            return false;
        }
        if (!(villager.level() instanceof ServerLevel level)) {
            return false;
        }
        return SquadService.squadNightwatch(level, data.getSquadBoxPos());
    }

    public static boolean hasRoyalGuardChest(Villager villager) {
        return isRoyalGuardChest(VillagerGearRules.get(villager, VillagerGearSlot.CHESTPLATE));
    }

    public static boolean isRoyalGuardChest(ItemStack stack) {
        return !stack.isEmpty() && stack.is(com.villagers.mod.VillagersMod.ROYAL_GUARD_CHESTPLATE.get());
    }
}
