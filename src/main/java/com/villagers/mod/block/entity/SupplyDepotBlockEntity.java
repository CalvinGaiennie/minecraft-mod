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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import com.villagers.mod.economy.SupplyRouting;

/** Weekly supply drop for blacksmith/fletcher/armorer gear (Stage 2 simplified). */
public class SupplyDepotBlockEntity extends BaseContainerBlockEntity {
    private static final int SLOTS = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    private long lastSupplyWeek = -1;

    public SupplyDepotBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.SUPPLY_DEPOT.get(), pos, state);
    }

    public void tickWeeklySupply(long currentWeek) {
        if (lastSupplyWeek == currentWeek) {
            return;
        }
        lastSupplyWeek = currentWeek;
        Level level = getLevel();
        if (level != null && !level.isClientSide) {
            SupplyRouting.depositWeeklyGear(level, getBlockPos(), this::tryInsertFallback);
        }
        setChanged();
    }

    private boolean tryInsertFallback(ItemStack stack) {
        return tryInsert(stack);
    }

    private boolean tryInsert(ItemStack stack) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, stack.copy());
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("LastSupplyWeek", lastSupplyWeek);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        lastSupplyWeek = tag.getLong("LastSupplyWeek");
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
        return Component.translatable("container.villagers.supply_depot");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x1, id, inventory, this, 1);
    }
}
