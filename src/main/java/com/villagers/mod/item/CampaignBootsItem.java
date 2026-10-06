package com.villagers.mod.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;

public class CampaignBootsItem extends ArmorItem {
    public CampaignBootsItem(Properties properties) {
        super(ArmorMaterials.IRON, Type.BOOTS, properties);
    }

    public static boolean isCampaignBoots(ItemStack stack) {
        return stack.getItem() instanceof CampaignBootsItem;
    }
}
