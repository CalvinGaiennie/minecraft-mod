package com.villagers.mod.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import com.villagers.mod.menu.SquadBoxMenu;
import com.villagers.mod.network.SquadBoxActionPayload;
import com.villagers.mod.squad.SquadService;

public class SquadBoxScreen extends AbstractContainerScreen<SquadBoxMenu> {
    private static final ResourceLocation CHEST_BG =
            ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");

    public SquadBoxScreen(SquadBoxMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = SquadBoxMenu.PANEL_HEIGHT;
        this.inventoryLabelY = SquadBoxMenu.PLAYER_INV_TOP - 11;
        this.titleLabelY = 6;
    }

    @Override
    protected void init() {
        super.init();
        int bx = this.leftPos + 4;
        int by = this.topPos + SquadBoxMenu.BUTTON_ROW_TOP;
        int w = 84;
        addRenderableWidget(Button.builder(Component.translatable("screen.villagers.squad_box.btn_mode"), b -> send(SquadBoxMenu.BTN_MODE))
                .bounds(bx, by, w, 18)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("screen.villagers.squad_box.btn_campaign"), b -> send(SquadBoxMenu.BTN_CAMPAIGN))
                .bounds(bx + w + 4, by, w, 18)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("screen.villagers.squad_box.btn_nightwatch"), b -> send(SquadBoxMenu.BTN_NIGHTWATCH))
                .bounds(bx, by + 20, w, 18)
                .build());
    }

    private void send(int action) {
        PacketDistributor.sendToServer(new SquadBoxActionPayload(this.menu.getBoxPos().asLong(), action));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        int chestBgHeight = SquadBoxMenu.CHEST_ROWS * 18 + 17;
        graphics.blit(CHEST_BG, x, y, 0, 0, this.imageWidth, chestBgHeight);
        graphics.blit(CHEST_BG, x, y + chestBgHeight, 0, 126, this.imageWidth, 96);
        graphics.fill(x, y + SquadBoxMenu.VANILLA_PANEL_HEIGHT, x + this.imageWidth, y + this.imageHeight, 0xFFC6C6C6);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
        var data = this.menu.getPanelData();
        graphics.drawString(this.font, membersLine(data), 4, SquadBoxMenu.STATS_ROW_1, 0x404040, false);
        graphics.drawString(this.font, linksLine(data), 4, SquadBoxMenu.STATS_ROW_2, 0x404040, false);
        graphics.drawString(this.font, modesLine(data), 4, SquadBoxMenu.STATS_ROW_2 + 12, 0x606060, false);
        graphics.drawString(this.font, leaderLine(data), 4, SquadBoxMenu.STATS_ROW_2 + 24, 0x404040, false);
    }

    private Component membersLine(net.minecraft.world.inventory.ContainerData data) {
        return Component.translatable(
                "screen.villagers.squad_box.members_line",
                data.get(3),
                SquadService.MAX_MEMBERS);
    }

    private Component linksLine(net.minecraft.world.inventory.ContainerData data) {
        return Component.translatable(
                "screen.villagers.squad_box.links_line",
                data.get(4),
                data.get(5));
    }

    private Component modesLine(net.minecraft.world.inventory.ContainerData data) {
        String mode = data.get(0) == 1 ? "Recruit" : "Standard";
        String camp = data.get(1) == 1 ? "Campaign" : "Garrison";
        String watch = data.get(2) == 1 ? "Nightwatch" : "Day watch";
        return Component.translatable("screen.villagers.squad_box.modes_line", mode, camp, watch);
    }

    private Component leaderLine(net.minecraft.world.inventory.ContainerData data) {
        if (data.get(3) <= 0) {
            return Component.translatable("screen.villagers.squad_box.no_members");
        }
        if (data.get(6) == 0) {
            return Component.translatable("screen.villagers.squad_box.no_leader");
        }
        String label = this.menu.getBox().getSquadLeaderLabel();
        if (label == null || label.isEmpty()) {
            if (data.get(7) == 1) {
                return Component.translatable("screen.villagers.squad_box.acting_leader");
            }
            return Component.translatable("screen.villagers.squad_box.leader_assigned");
        }
        return Component.literal(label);
    }
}
