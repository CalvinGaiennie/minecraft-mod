package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MessStationBlockEntity extends BaseContainerBlockEntity {
    private static final int SLOTS = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    private ItemStack villageBanner = ItemStack.EMPTY;

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

    /** Inserts one meal from the held stack; returns true if one item was added. */
    public boolean tryInsertOne(ItemStack held) {
        if (!isSoldierFood(held)) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            ItemStack slot = items.get(i);
            if (slot.isEmpty()) {
                items.set(i, held.copyWithCount(1));
                setChanged();
                return true;
            }
            if (ItemStack.isSameItemSameComponents(slot, held) && slot.getCount() < slot.getMaxStackSize()) {
                slot.grow(1);
                setChanged();
                return true;
            }
        }
        return false;
    }

    public void setFoodForTesting(ItemStack stack) {
        items.set(0, stack);
        setChanged();
    }

    /** Removes one soldier meal if stocked. */
    public boolean consumeOneFood() {
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (isSoldierFood(stack)) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    items.set(i, ItemStack.EMPTY);
                }
                setChanged();
                return true;
            }
        }
        return false;
    }

    public ItemStack getVillageBanner() {
        return villageBanner;
    }

    public void setVillageBanner(ItemStack stack) {
        this.villageBanner = stack.copyWithCount(1);
        setChanged();
    }

    public boolean hasVillageBanner() {
        return !villageBanner.isEmpty();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        if (!villageBanner.isEmpty()) {
            tag.put("VillageBanner", villageBanner.save(registries));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        villageBanner = tag.contains("VillageBanner")
                ? ItemStack.parse(registries, tag.getCompound("VillageBanner")).orElse(ItemStack.EMPTY)
                : ItemStack.EMPTY;
    }

    @Override
    public int getContainerSize() {
        return SLOTS;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.villagers.mess_station");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new ChestMenu(MenuType.GENERIC_9x1, containerId, playerInventory, this, 1);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return isSoldierFood(stack);
    }

    @Override
    public void startOpen(Player player) {
        if (!this.level.isClientSide) {
            this.setChanged();
        }
    }
}
