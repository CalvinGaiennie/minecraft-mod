package com.villagers.mod.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.entity.SoldierConversion;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

import org.jetbrains.annotations.Nullable;

public class VillagerGearMenu extends AbstractContainerMenu {
    public static final int GEAR_PANEL_HEIGHT = 104;
    private static final int PLAYER_INV_START_Y = 118;
    private static final int HOTBAR_Y = 176;

    private final SimpleContainer gearInventory = new SimpleContainer(VillagerGearSlot.values().length);

    @Nullable
    private final Villager villager;

    public VillagerGearMenu(int containerId, Inventory playerInventory, @Nullable Villager villager) {
        super(VillagerMenus.VILLAGER_GEAR.get(), containerId);
        this.villager = villager;

        if (villager != null && !playerInventory.player.level().isClientSide()) {
            VillagerGearRules.loadIntoContainer(villager, gearInventory);
            gearInventory.addListener(container -> VillagerGearRules.saveFromContainer(villager, gearInventory));
        }

        for (VillagerGearSlot gearSlot : VillagerGearSlot.values()) {
            this.addSlot(new GearSlot(villager, gearInventory, gearSlot, gearSlot.ordinal(), gearSlot.x(), gearSlot.y()));
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(
                        playerInventory, col + row * 9 + 9, 8 + col * 18, PLAYER_INV_START_Y + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, HOTBAR_Y));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        int gearSlots = VillagerGearSlot.values().length;
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index < gearSlots) {
                if (!this.moveItemStackTo(slotStack, gearSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, gearSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    public static void open(Player player, Villager villager) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        boolean soldier = villager.hasData(VillagerAttachments.SOLDIER_DATA.get());
        Component title = Component.translatable(soldier ? "container.villagers.soldier_gear" : "container.villagers.recruit_gear");
        serverPlayer.openMenu(
                new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return title;
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player ignored) {
                        return new VillagerGearMenu(containerId, inventory, villager);
                    }
                },
                (RegistryFriendlyByteBuf buf) -> buf.writeVarInt(villager.getId()));
    }

    @Override
    public boolean stillValid(Player player) {
        return villager != null && villager.isAlive() && player.canInteractWithEntity(villager, 4.0);
    }

    @Override
    public void removed(Player player) {
        if (villager != null && !player.level().isClientSide()) {
            VillagerGearRules.saveFromContainer(villager, gearInventory);
            VillagerGearRules.applyToEntity(villager);
            com.villagers.mod.entity.MilitiaConversion.onGearUpdated(villager);
        }
        super.removed(player);
        if (villager == null || player.level().isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (SoldierConversion.tryConvert(villager)) {
            serverPlayer.displayClientMessage(Component.translatable("message.villagers.convert_success"), true);
        } else {
            Component reason = SoldierConversion.blockReason(villager);
            if (reason != null && villager.getVillagerData().getProfession() == net.minecraft.world.entity.npc.VillagerProfession.NONE
                    && !villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                serverPlayer.displayClientMessage(reason, true);
            }
        }
    }

    @Nullable
    public Villager getVillager() {
        return villager;
    }

    public static final class GearSlot extends Slot {
        @Nullable
        private final Villager villager;
        private final VillagerGearSlot gearSlot;

        GearSlot(@Nullable Villager villager, SimpleContainer container, VillagerGearSlot gearSlot, int index, int x, int y) {
            super(container, index, x, y);
            this.villager = villager;
            this.gearSlot = gearSlot;
        }

        public VillagerGearSlot gearSlot() {
            return gearSlot;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (villager != null && villager.hasData(VillagerAttachments.MILITIA_DATA.get()) && gearSlot == VillagerGearSlot.LEGGINGS) {
                return false;
            }
            return VillagerGearRules.accepts(gearSlot, stack);
        }

        @Override
        public int getMaxStackSize() {
            return gearSlot == VillagerGearSlot.ARROWS ? VillagerGearRules.MAX_ARROWS : 1;
        }

        @Override
        public int getMaxStackSize(ItemStack stack) {
            return gearSlot == VillagerGearSlot.ARROWS ? VillagerGearRules.MAX_ARROWS : 1;
        }
    }
}
