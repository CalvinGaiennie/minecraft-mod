package com.villagers.mod.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import com.villagers.mod.network.MusterRollPayload;
import com.villagers.mod.util.MusterRollHelper;

import java.util.List;

public class MusterRollScreen extends Screen {
    private static final int MAP_X = 12;
    private static final int MAP_Y = 52;
    private static final int MAP_SIZE = 148;
    private static final int LIST_X = 168;

    private final MusterRollPayload data;
    private MusterRollPayload.TerritoryEntry hoveredTerritory;

    public MusterRollScreen(MusterRollPayload data) {
        super(Component.translatable("screen.villagers.muster_roll"));
        this.data = data;
    }

    @Override
    protected void init() {
        this.clearWidgets();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        int left = (this.width - 310) / 2;
        int top = (this.height - 220) / 2;

        graphics.fill(left, top, left + 310, top + 220, 0xE0101010);
        graphics.renderOutline(left, top, 310, 220, 0xFF8B7355);

        graphics.drawString(
                this.font,
                this.title,
                left + (310 - this.font.width(this.title)) / 2,
                top + 8,
                0xFFE8D4A8,
                false);

        Component ownedLine = Component.translatable(
                "screen.villagers.muster_roll.owned",
                data.ownedSoldierCount(),
                data.ownedTotalKills(),
                data.territories().size());
        graphics.drawString(this.font, ownedLine, left + 12, top + 24, 0xFFCCCCCC, false);

        Component nearbyLine = Component.translatable(
                "screen.villagers.muster_roll.nearby",
                MusterRollHelper.NEARBY_RADIUS,
                data.nearbySoldierCount(),
                data.nearbyTotalKills());
        graphics.drawString(this.font, nearbyLine, left + 12, top + 36, 0xFFAAAAAA, false);

        int mapLeft = left + MAP_X;
        int mapTop = top + MAP_Y;
        hoveredTerritory = renderTerritoryMap(graphics, mapLeft, mapTop, MAP_SIZE, mouseX, mouseY);

        graphics.drawString(
                this.font,
                Component.translatable("screen.villagers.muster_roll.map_hint"),
                mapLeft,
                mapTop + MAP_SIZE + 4,
                0xFF888888,
                false);

        renderVillageList(graphics, left + LIST_X, top + 52, top + 210, mouseX, mouseY);

        if (hoveredTerritory != null) {
            Component tip = Component.translatable(
                    "screen.villagers.muster_roll.tooltip",
                    hoveredTerritory.villageName(),
                    hoveredTerritory.centerX(),
                    hoveredTerritory.centerZ(),
                    hoveredTerritory.radius(),
                    hoveredTerritory.soldierCount(),
                    hoveredTerritory.totalKills());
            graphics.renderTooltip(this.font, tip, mouseX, mouseY);
        }
    }

    private MusterRollPayload.TerritoryEntry renderTerritoryMap(
            GuiGraphics graphics, int mapLeft, int mapTop, int mapSize, int mouseX, int mouseY) {
        graphics.fill(mapLeft, mapTop, mapLeft + mapSize, mapTop + mapSize, 0xFF1B3D1B);
        graphics.renderOutline(mapLeft, mapTop, mapSize, mapSize, 0xFF4A6741);

        List<MusterRollPayload.TerritoryEntry> territories = data.territories();
        if (territories.isEmpty()) {
            Component empty = Component.translatable("screen.villagers.muster_roll.no_territory");
            graphics.drawString(
                    this.font,
                    empty,
                    mapLeft + (mapSize - this.font.width(empty)) / 2,
                    mapTop + mapSize / 2 - 4,
                    0xFFAAAAAA,
                    false);
            int center = mapSize / 2;
            graphics.fill(mapLeft + center - 1, mapTop + center - 1, mapLeft + center + 2, mapTop + center + 2, 0xFFFFFFFF);
            return null;
        }

        double minX = data.playerBlockX();
        double maxX = data.playerBlockX();
        double minZ = data.playerBlockZ();
        double maxZ = data.playerBlockZ();
        for (MusterRollPayload.TerritoryEntry entry : territories) {
            minX = Math.min(minX, entry.centerX() - entry.radius());
            maxX = Math.max(maxX, entry.centerX() + entry.radius());
            minZ = Math.min(minZ, entry.centerZ() - entry.radius());
            maxZ = Math.max(maxZ, entry.centerZ() + entry.radius());
        }

        double spanX = Math.max(maxX - minX, 16.0);
        double spanZ = Math.max(maxZ - minZ, 16.0);
        double scale = (mapSize - 8) / Math.max(spanX, spanZ);

        MusterRollPayload.TerritoryEntry hovered = null;
        for (MusterRollPayload.TerritoryEntry entry : territories) {
            int cx = mapLeft + 4 + (int) ((entry.centerX() - minX) * scale);
            int cz = mapTop + 4 + (int) ((entry.centerZ() - minZ) * scale);
            int pixelRadius = Math.max(3, (int) (entry.radius() * scale));
            drawCircleOutline(graphics, cx, cz, pixelRadius, 0xFF7CFC7C);
            if (mouseX >= cx - pixelRadius && mouseX <= cx + pixelRadius
                    && mouseY >= cz - pixelRadius && mouseY <= cz + pixelRadius) {
                hovered = entry;
            }
        }

        renderPlayerMarker(graphics, mapLeft, mapTop, mapSize, minX, minZ, scale);
        return hovered;
    }

    private void renderPlayerMarker(
            GuiGraphics graphics, int mapLeft, int mapTop, int mapSize, double minX, double minZ, double scale) {
        int px = mapLeft + 4 + (int) ((data.playerBlockX() - minX) * scale);
        int pz = mapTop + 4 + (int) ((data.playerBlockZ() - minZ) * scale);
        graphics.fill(px - 1, pz - 1, px + 2, pz + 2, 0xFFFFFFFF);
        graphics.fill(px, pz - 2, px + 1, pz + 3, 0xFF3366FF);
        graphics.fill(px - 2, pz, px + 3, pz + 1, 0xFF3366FF);
    }

    private void drawCircleOutline(GuiGraphics graphics, int centerX, int centerY, int radius, int color) {
        for (int angle = 0; angle < 360; angle += 6) {
            double rad = Math.toRadians(angle);
            int x = centerX + (int) (Math.cos(rad) * radius);
            int y = centerY + (int) (Math.sin(rad) * radius);
            graphics.fill(x, y, x + 1, y + 1, color);
        }
    }

    private void renderVillageList(GuiGraphics graphics, int listX, int listTop, int listBottom, int mouseX, int mouseY) {
        graphics.drawString(this.font, Component.translatable("screen.villagers.muster_roll.villages"), listX, listTop - 12, 0xFFE8D4A8, false);

        int y = listTop;
        for (MusterRollPayload.TerritoryEntry entry : data.territories()) {
            if (y > listBottom - 10) {
                graphics.drawString(this.font, Component.literal("…"), listX, y, 0xFF888888, false);
                break;
            }
            String line = entry.villageName() + " (" + entry.soldierCount() + ")";
            int color = entry == hoveredTerritory ? 0xFF7CFC7C : 0xFFDDDDDD;
            graphics.drawString(this.font, Component.literal(line), listX, y, color, false);
            y += 10;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
