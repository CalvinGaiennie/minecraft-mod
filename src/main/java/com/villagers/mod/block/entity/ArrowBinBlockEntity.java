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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ArrowBinBlockEntity extends BaseContainerBlockEntity {
    private static final int SLOTS = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    public ArrowBinBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.ARROW_BIN.get(), pos, state);
    }

    public static boolean accepts(ItemStack stack) {
        return stack.is(Items.ARROW);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
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
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return accepts(stack);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.villagers.arrow_bin");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x1, id, inventory, this, 1);
    }
}
