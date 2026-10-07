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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TaxBoxBlockEntity extends BaseContainerBlockEntity {
    private static final int SLOTS = 27;
    private static final String TAG_LAST_TAX_DAY = "LastTaxDay";
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    /** In-game day index when tax was last settled on open; {@code -1} = never. */
    private long lastTaxDay = -1L;

    public TaxBoxBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.TAX_BOX.get(), pos, state);
    }

    public boolean tryInsertTax(ItemStack stack) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack slot = items.get(i);
            if (slot.isEmpty()) {
                items.set(i, stack.copy());
                setChanged();
                return true;
            }
            if (ItemStack.isSameItemSameComponents(slot, stack) && slot.getCount() < slot.getMaxStackSize()) {
                slot.grow(stack.getCount());
                setChanged();
                return true;
            }
        }
        return false;
    }

    public long getLastTaxDay() {
        return lastTaxDay;
    }

    public void setLastTaxDay(long day) {
        this.lastTaxDay = day;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putLong(TAG_LAST_TAX_DAY, lastTaxDay);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
        lastTaxDay = tag.contains(TAG_LAST_TAX_DAY) ? tag.getLong(TAG_LAST_TAX_DAY) : -1L;
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
        return Component.translatable("container.villagers.tax_box");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return ChestMenu.threeRows(id, inventory, this);
    }
}
