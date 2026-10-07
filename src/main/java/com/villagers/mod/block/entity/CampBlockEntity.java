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

import java.util.UUID;

/** Field mess + storage for soldiers on campaign (54 slots). */
public class CampBlockEntity extends BaseContainerBlockEntity {
    private static final int SLOTS = 54;
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    @org.jetbrains.annotations.Nullable
    private UUID ownerId;

    public CampBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.CAMP.get(), pos, state);
    }

    @org.jetbrains.annotations.Nullable
    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
        setChanged();
    }

    public boolean consumeOneFood() {
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (MessStationBlockEntity.isSoldierFood(stack)) {
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

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        if (ownerId != null) {
            tag.putUUID("Owner", ownerId);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
        ownerId = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;
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
        return Component.translatable("container.villagers.camp_block");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return ChestMenu.sixRows(id, inventory, this);
    }

    @Override
    public void startOpen(Player player) {
        if (!level.isClientSide) {
            setChanged();
        }
    }
}
