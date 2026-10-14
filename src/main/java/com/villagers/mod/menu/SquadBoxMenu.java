package com.villagers.mod.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.squad.SquadMode;
import com.villagers.mod.squad.SquadService;

import net.minecraft.world.entity.npc.Villager;

public class SquadBoxMenu extends AbstractContainerMenu {
    /** Vanilla 3-row chest (166) + squad button strip below hotbar. */
    public static final int CHEST_ROWS = 3;
    public static final int CHEST_TOP = 18;
    public static final int PLAYER_INV_TOP = 84;
    public static final int HOTBAR_TOP = 142;
    public static final int VANILLA_PANEL_HEIGHT = 166;
    /** Below hotbar, above toggle buttons — squad summary text. */
    public static final int STATS_ROW_1 = 163;
    public static final int STATS_ROW_2 = 175;
    public static final int BUTTON_ROW_TOP = 214;
    public static final int PANEL_HEIGHT = 246;

    private static final int COLS = 9;
    private static final int SLOTS = CHEST_ROWS * COLS;

    public static final int BTN_CAMPAIGN = 0;
    public static final int BTN_NIGHTWATCH = 1;
    public static final int BTN_MODE = 2;

    private final RecruiterBlockEntity box;
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public SquadBoxMenu(int containerId, Inventory playerInventory, RecruiterBlockEntity box) {
        super(VillagerMenus.SQUAD_BOX.get(), containerId);
        this.box = box;
        this.access = ContainerLevelAccess.create(box.getLevel(), box.getBlockPos());
        this.data = new SimpleContainerData(8);
        this.addDataSlots(data);

        for (int row = 0; row < CHEST_ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                this.addSlot(new Slot(box, col + row * COLS, 8 + col * 18, CHEST_TOP + row * 18));
            }
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, PLAYER_INV_TOP + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, HOTBAR_TOP));
        }
        syncPanelData();
    }

    private void syncPanelData() {
        if (box == null) {
            return;
        }
        data.set(0, box.getSquadMode() == SquadMode.RECRUIT ? 1 : 0);
        data.set(1, box.isSquadOnCampaign() ? 1 : 0);
        data.set(2, box.isNightwatchEnabled() ? 1 : 0);
        if (box.getLevel() instanceof ServerLevel server) {
            SquadService.syncSquadLeadership(server, box.getBlockPos());
            data.set(3, SquadService.memberCount(server, box.getBlockPos()));
            int acting = 0;
            if (box.getSquadLeaderId() != null) {
                for (Villager member : SquadService.members(server, box.getBlockPos())) {
                    if (member.getUUID().equals(box.getSquadLeaderId())) {
                        SoldierData leaderData = member.getData(VillagerAttachments.SOLDIER_DATA.get());
                        if (leaderData != null && leaderData.isActingLeader()) {
                            acting = 1;
                        }
                        break;
                    }
                }
            }
            data.set(7, acting);
        }
        data.set(4, box.getLinkedBeds().size());
        data.set(5, box.getSquadRamparts().size());
        data.set(6, box.getSquadLeaderId() != null ? 1 : 0);
    }

    public RecruiterBlockEntity getBox() {
        return box;
    }

    @Override
    public void broadcastChanges() {
        syncPanelData();
        super.broadcastChanges();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, VillagersMod.RECRUITER_BOX.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index < SLOTS) {
                if (!this.moveItemStackTo(slotStack, SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, SLOTS, false)) {
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

    public boolean ownsBox(RecruiterBlockEntity other) {
        return box != null && other != null && box.getBlockPos().equals(other.getBlockPos());
    }

    public BlockPos getBoxPos() {
        return box.getBlockPos();
    }

    public void handleButton(ServerPlayer serverPlayer, int id) {
        if (!(box.getLevel() instanceof ServerLevel level)) {
            return;
        }
        switch (id) {
            case BTN_CAMPAIGN -> {
                SquadService.toggleCampaign(level, box.getBlockPos(), serverPlayer);
                SquadService.notifyPlayer(
                        serverPlayer,
                        box.isSquadOnCampaign()
                                ? "message.villagers.squad_campaign_on"
                                : "message.villagers.squad_campaign_off");
                box.setChanged();
            }
            case BTN_NIGHTWATCH -> {
                box.setNightwatchEnabled(!box.isNightwatchEnabled());
                SquadService.notifyPlayer(
                        serverPlayer,
                        box.isNightwatchEnabled()
                                ? "message.villagers.squad_nightwatch_on"
                                : "message.villagers.squad_nightwatch_off");
            }
            case BTN_MODE -> {
                box.setSquadMode(box.getSquadMode() == SquadMode.STANDARD ? SquadMode.RECRUIT : SquadMode.STANDARD);
                SquadService.notifyPlayer(
                        serverPlayer,
                        box.getSquadMode() == SquadMode.RECRUIT
                                ? "message.villagers.squad_mode_recruit"
                                : "message.villagers.squad_mode_standard");
            }
            default -> {
            }
        }
        syncPanelData();
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (player instanceof ServerPlayer serverPlayer) {
            handleButton(serverPlayer, id);
            return true;
        }
        return false;
    }

    public ContainerData getPanelData() {
        return data;
    }
}
