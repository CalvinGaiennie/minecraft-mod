package com.villagers.mod.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import com.villagers.mod.gear.VillagerGearSlot;
import com.villagers.mod.menu.VillagerGearMenu;

public class VillagerGearScreen extends AbstractContainerScreen<VillagerGearMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND =
            ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
    private static final int ARMOR_COLUMN_X = 44;
    private static final int WEAPONS_COLUMN_X = 128;

    public VillagerGearScreen(VillagerGearMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = VillagerGearMenu.GEAR_PANEL_HEIGHT + 96;
        this.inventoryLabelY = VillagerGearMenu.GEAR_PANEL_HEIGHT - 12;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        graphics.fill(x, y, x + this.imageWidth, y + VillagerGearMenu.GEAR_PANEL_HEIGHT, 0xFFC6C6C6);
        for (VillagerGearSlot gearSlot : VillagerGearSlot.values()) {
            graphics.blit(CONTAINER_BACKGROUND, x + gearSlot.x(), y + gearSlot.y(), 7, 17, 18, 18);
        }
        graphics.blit(
                CONTAINER_BACKGROUND,
                x,
                y + VillagerGearMenu.GEAR_PANEL_HEIGHT,
                0,
                126,
                this.imageWidth,
                96);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);

        Component armorHeader = Component.translatable("container.villagers.gear_armor");
        Component weaponsHeader = Component.translatable("container.villagers.gear_weapons");
        graphics.drawString(
                this.font,
                armorHeader,
                ARMOR_COLUMN_X + 9 - this.font.width(armorHeader) / 2,
                22,
                0x404040,
                false);
        graphics.drawString(
                this.font,
                weaponsHeader,
                WEAPONS_COLUMN_X + 9 - this.font.width(weaponsHeader) / 2,
                22,
                0x404040,
                false);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.hoveredSlot instanceof VillagerGearMenu.GearSlot gearSlot && !gearSlot.hasItem()) {
            graphics.renderTooltip(this.font, gearSlot.gearSlot().tip(), mouseX, mouseY);
        } else {
            super.renderTooltip(graphics, mouseX, mouseY);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
