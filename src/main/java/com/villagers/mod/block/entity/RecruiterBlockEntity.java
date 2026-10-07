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

import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

/** Issued gear storage; recruiter villagers pull replacements from here (Stage 2). */
public class RecruiterBlockEntity extends BaseContainerBlockEntity {
    public static final int MAX_RECRUITS = 10;
    private static final int SLOTS = 27;
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    private int activeRecruits;

    public RecruiterBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.RECRUITER_BOX.get(), pos, state);
    }

    public int getActiveRecruits() {
        return activeRecruits;
    }

    public void setActiveRecruits(int activeRecruits) {
        this.activeRecruits = Math.max(0, Math.min(MAX_RECRUITS, activeRecruits));
        setChanged();
    }

    public boolean canAcceptRecruit() {
        return activeRecruits < MAX_RECRUITS;
    }

    /** Adds one issued gear stack if there is a free or stackable slot. */
    public ItemStack withdrawMatchingGear(VillagerGearSlot slot) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty() && VillagerGearRules.accepts(slot, stack)) {
                ItemStack taken = stack.copyWithCount(1);
                stack.shrink(1);
                if (stack.isEmpty()) {
                    items.set(i, ItemStack.EMPTY);
                }
                setChanged();
                return taken;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean hasArrows() {
        for (ItemStack stack : items) {
            if (stack.is(net.minecraft.world.item.Items.ARROW)) {
                return true;
            }
        }
        return false;
    }

    /** Pulls up to {@code max} arrows from issued storage (same rules as arrow bins for soldiers). */
    public int withdrawArrows(int max) {
        if (max <= 0) {
            return 0;
        }
        int taken = 0;
        for (int i = 0; i < items.size() && taken < max; i++) {
            ItemStack stack = items.get(i);
            if (!stack.is(net.minecraft.world.item.Items.ARROW)) {
                continue;
            }
            int pull = Math.min(max - taken, stack.getCount());
            stack.shrink(pull);
            if (stack.isEmpty()) {
                items.set(i, ItemStack.EMPTY);
            }
            taken += pull;
        }
        if (taken > 0) {
            setChanged();
        }
        return taken;
    }

    public boolean tryInsertIssuedGear(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            ItemStack slot = items.get(i);
            if (slot.isEmpty()) {
                items.set(i, stack.copy());
                return true;
            }
            if (ItemStack.isSameItemSameComponents(slot, stack) && slot.getCount() < slot.getMaxStackSize()) {
                slot.grow(1);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("ActiveRecruits", activeRecruits);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        activeRecruits = tag.getInt("ActiveRecruits");
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
    protected Component getDefaultName() {
        return Component.translatable("container.villagers.recruiter_box");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return ChestMenu.threeRows(id, inventory, this);
    }
}
