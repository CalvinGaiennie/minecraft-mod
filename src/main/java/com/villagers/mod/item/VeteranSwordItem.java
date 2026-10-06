package com.villagers.mod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

import com.villagers.mod.VillagersMod;

public class VeteranSwordItem extends SwordItem {
    public VeteranSwordItem(Item.Properties properties) {
        super(Tiers.IRON, properties);
    }

    public static boolean isVeteranSword(ItemStack stack) {
        return stack.getItem() instanceof VeteranSwordItem;
    }
}
