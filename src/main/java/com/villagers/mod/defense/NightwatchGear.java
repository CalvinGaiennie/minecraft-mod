package com.villagers.mod.defense;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

public final class NightwatchGear {
    private NightwatchGear() {
    }

    public static boolean hasNightwatchHelmet(Villager villager) {
        return isNightwatchHelmet(VillagerGearRules.get(villager, VillagerGearSlot.HELMET));
    }

    public static boolean isNightwatchHelmet(ItemStack stack) {
        return !stack.isEmpty() && stack.is(VillagersMod.NIGHTWATCH_HELMET.get());
    }

    public static boolean hasRoyalGuardChest(Villager villager) {
        return isRoyalGuardChest(VillagerGearRules.get(villager, VillagerGearSlot.CHESTPLATE));
    }

    public static boolean isRoyalGuardChest(ItemStack stack) {
        return !stack.isEmpty() && stack.is(VillagersMod.ROYAL_GUARD_CHESTPLATE.get());
    }
}
