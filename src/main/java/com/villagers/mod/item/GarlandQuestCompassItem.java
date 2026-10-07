package com.villagers.mod.item;

import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;

public class GarlandQuestCompassItem extends CompassItem {
    public GarlandQuestCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(net.minecraft.core.component.DataComponents.LODESTONE_TRACKER);
    }
}
