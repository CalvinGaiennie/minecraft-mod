package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MessStationBlockEntity extends BlockEntity {
    private static final int SLOTS = 9;
    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    public MessStationBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.MESS_STATION.get(), pos, state);
    }

    public boolean hasSoldierFood() {
        for (ItemStack stack : items) {
            if (isSoldierFood(stack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSoldierFood(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return stack.is(Items.BREAD) || stack.is(Items.COOKED_BEEF) || stack.is(Items.COOKED_PORKCHOP)
                || stack.is(Items.COOKED_CHICKEN) || stack.is(Items.COOKED_MUTTON) || stack.is(Items.BAKED_POTATO)
                || stack.is(Items.SWEET_BERRIES);
    }

    public void setFoodForTesting(ItemStack stack) {
        items.set(0, stack);
        setChanged();
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
