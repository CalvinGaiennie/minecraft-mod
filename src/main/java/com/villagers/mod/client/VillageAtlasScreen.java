package com.villagers.mod.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import com.villagers.mod.atlas.MapPalette;
import com.villagers.mod.network.VillageAtlasPayload;

import org.lwjgl.glfw.GLFW;

public class VillageAtlasScreen extends Screen {
    private static final int SCREEN_MARGIN = 12;
    private static final int PANEL_PADDING = 10;
    private static final int HEADER_H = 34;
    private static final int FOOTER_H = 26;
    private static final int LIST_W = 148;

    /** Finest zoom: one screen pixel per world block. */
    private static final double MIN_BLOCKS_PER_PIXEL = 1.0;
    private static final double MAX_BLOCKS_PER_PIXEL = 8192.0;

    private static final int COLOR_SPAWN = 0xFFFFFFFF;
    private static final int COLOR_PLAYER = 0xFF4488FF;
    private static final int COLOR_CLAIM = 0xFF3D8B3D;
    private static final int COLOR_CLAIM_EDGE = 0xFF7CFC7C;

    private final VillageAtlasPayload data;
    private VillageAtlasPayload.HamletEntry hovered;

    private double viewCenterX;
    private double viewCenterZ;
    private double blocksPerPixel;
    private boolean viewInitialized;
    private boolean dragging;
    private double dragLastX;
    private double dragLastY;

    private int panelLeft;
    private int panelTop;
    private int panelW;
    private int panelH;
    private int mapLeft;
    private int mapTop;
    private int mapSize;
    private int listX;
    private int listTop;
    private int listMaxY;

    public VillageAtlasScreen(VillageAtlasPayload data) {
        super(Component.translatable("screen.villagers.village_atlas"));
        this.data = data;
        this.blocksPerPixel = MIN_BLOCKS_PER_PIXEL;
    }

    @Override
    protected void init() {
        this.clearWidgets();
        layoutPanel();
    }

    private void layoutPanel() {
        panelLeft = SCREEN_MARGIN;
        panelTop = SCREEN_MARGIN;
        panelW = Math.max(200, this.width - SCREEN_MARGIN * 2);
        panelH = Math.max(160, this.height - SCREEN_MARGIN * 2);

        int innerW = panelW - PANEL_PADDING * 2;
        int innerH = panelH - PANEL_PADDING * 2 - HEADER_H - FOOTER_H;

        boolean sideList = innerW >= 280 + LIST_W;
        int mapMaxW = sideList ? innerW - LIST_W - 8 : innerW;
        mapSize = Math.max(64, Math.min(mapMaxW, innerH));
        if (!sideList && innerH > mapSize + 60) {
            mapSize = Math.max(64, Math.min(mapMaxW, innerH - 56));
        }

        mapLeft = panelLeft + PANEL_PADDING;
        mapTop = panelTop + PANEL_PADDING + HEADER_H;

        listX = sideList ? mapLeft + mapSize + 8 : mapLeft;
        listTop = sideList ? mapTop : mapTop + mapSize + 6;
        listMaxY = panelTop + panelH - PANEL_PADDING - FOOTER_H;
    }

    private void ensureViewInitialized() {
        if (viewInitialized) {
            return;
        }
        layoutPanel();
        viewCenterX = data.playerBlockX();
        viewCenterZ = data.playerBlockZ();
        blocksPerPixel = MIN_BLOCKS_PER_PIXEL;
        fitAllInView();
        viewInitialized = true;
    }

    private void fitAllInView() {
        double minX = data.playerBlockX();
        double maxX = minX;
        double minZ = data.playerBlockZ();
        double maxZ = minZ;
        minX = Math.min(minX, data.worldSpawnX());
        maxX = Math.max(maxX, data.worldSpawnX());
        minZ = Math.min(minZ, data.worldSpawnZ());
        maxZ = Math.max(maxZ, data.worldSpawnZ());
        for (VillageAtlasPayload.HamletEntry entry : data.hamlets()) {
            minX = Math.min(minX, entry.centerX() - entry.radius());
            maxX = Math.max(maxX, entry.centerX() + entry.radius());
            minZ = Math.min(minZ, entry.centerZ() - entry.radius());
            maxZ = Math.max(maxZ, entry.centerZ() + entry.radius());
        }
        double span = Math.max(maxX - minX, maxZ - minZ);
        span = Math.max(span, 32.0);
        double fit = span / (mapSize * 0.9);
        blocksPerPixel = Math.clamp(Math.max(blocksPerPixel, fit), MIN_BLOCKS_PER_PIXEL, MAX_BLOCKS_PER_PIXEL);
        viewCenterX = (minX + maxX) * 0.5;
        viewCenterZ = (minZ + maxZ) * 0.5;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ensureViewInitialized();
        layoutPanel();

        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        graphics.fill(panelLeft, panelTop, panelLeft + panelW, panelTop + panelH, 0xE0101010);
        graphics.renderOutline(panelLeft, panelTop, panelW, panelH, 0xFF8B7355);

        int titleX = panelLeft + (panelW - this.font.width(this.title)) / 2;
        graphics.drawString(this.font, this.title, titleX, panelTop + PANEL_PADDING + 2, 0xFFE8D4A8, false);

        Component summary = Component.translatable(
                "screen.villagers.village_atlas.summary",
                data.totalSoldiers(),
                data.totalKills(),
                data.hamlets().size());
        graphics.drawString(
                this.font, summary, panelLeft + PANEL_PADDING, panelTop + PANEL_PADDING + 14, 0xFFCCCCCC, false);

        hovered = renderMap(graphics, mouseX, mouseY);

        graphics.drawString(
                this.font,
                Component.translatable("screen.villagers.village_atlas.hamlets"),
                listX,
                listTop,
                0xFFE8D4A8,
                false);

        int y = listTop + 12;
        for (VillageAtlasPayload.HamletEntry entry : data.hamlets()) {
            if (y > listMaxY) {
                graphics.drawString(this.font, Component.literal("…"), listX, y, 0xFF888888, false);
                break;
            }
            int color = entry == hovered ? 0xFF7CFC7C : 0xFFDDDDDD;
            String line = entry.shortId() + "  " + entry.villageName();
            Component hamletCounts = Component.translatable(
                    "screen.villagers.village_atlas.hamlet_counts",
                    entry.presentCount(),
                    entry.squadRosterCount());
            graphics.drawString(this.font, Component.literal(line), listX, y, color, false);
            int nameWidth = this.font.width(line);
            graphics.drawString(this.font, hamletCounts, listX + nameWidth + 4, y, 0xFFAAAAAA, false);
            y += 10;
        }
        if (data.hamlets().isEmpty()) {
            graphics.drawString(
                    this.font,
                    Component.translatable("screen.villagers.village_atlas.no_hamlets"),
                    listX,
                    y,
                    0xFFAAAAAA,
                    false);
        }

        int footerY = panelTop + panelH - PANEL_PADDING - FOOTER_H + 4;
        graphics.drawString(
                this.font,
                Component.translatable("screen.villagers.village_atlas.map_hint"),
                mapLeft,
                footerY,
                0xFF888888,
                false);

        String zoom = formatZoom(blocksPerPixel);
        graphics.drawString(this.font, Component.literal(zoom), mapLeft, footerY + 10, 0xFF666666, false);

        if (hovered != null) {
            Component tip = Component.translatable(
                    "screen.villagers.village_atlas.tooltip",
                    hovered.shortId(),
                    hovered.villageName(),
                    hovered.centerX(),
                    hovered.centerZ(),
                    hovered.radius(),
                    hovered.presentCount(),
                    hovered.squadRosterCount());
            graphics.renderTooltip(this.font, tip, mouseX, mouseY);
        }
    }

    private static String formatZoom(double blocksPerPixel) {
        if (blocksPerPixel >= 10.0) {
            return String.format("%.0f blk/px", blocksPerPixel);
        }
        if (blocksPerPixel == 1.0) {
            return "1 blk/px (1:1)";
        }
        return String.format("%.2f blk/px", blocksPerPixel);
    }

    private VillageAtlasPayload.HamletEntry renderMap(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.fill(mapLeft, mapTop, mapLeft + mapSize, mapTop + mapSize, MapPalette.UNEXPLORED_ARGB);
        graphics.renderOutline(mapLeft, mapTop, mapSize, mapSize, 0xFF4A6741);

        drawExploredTerrain(graphics);

        WorldBounds visible = visibleWorldBounds();

        for (VillageAtlasPayload.HamletEntry entry : data.hamlets()) {
            drawHamletClaim(graphics, entry, visible);
        }

        drawWorldBlock(graphics, data.worldSpawnX(), data.worldSpawnZ(), COLOR_SPAWN);
        drawWorldBlock(graphics, data.playerBlockX(), data.playerBlockZ(), COLOR_PLAYER);

        VillageAtlasPayload.HamletEntry hoveredEntry = null;
        int bestDist = Integer.MAX_VALUE;
        for (VillageAtlasPayload.HamletEntry entry : data.hamlets()) {
            if (!hamletContainsWorldBlock(entry, mouseWorldX(mouseX), mouseWorldZ(mouseY))) {
                continue;
            }
            int[] screen = worldToScreen(entry.centerX(), entry.centerZ());
            int dx = mouseX - screen[0];
            int dy = mouseY - screen[1];
            int dist = dx * dx + dy * dy;
            if (dist < bestDist) {
                bestDist = dist;
                hoveredEntry = entry;
            }
        }

        return hoveredEntry;
    }

    private void drawHamletClaim(
            GuiGraphics graphics, VillageAtlasPayload.HamletEntry entry, WorldBounds visible) {
        if (blocksPerPixel <= 2.0) {
            drawClaimBlockPixels(graphics, entry, visible);
            return;
        }
        int[] center = worldToScreen(entry.centerX(), entry.centerZ());
        int screenR = (int) Math.ceil(entry.radius() / blocksPerPixel);
        if (screenR <= 0) {
            drawWorldBlock(graphics, entry.centerX(), entry.centerZ(), COLOR_CLAIM_EDGE);
            return;
        }
        fillScreenDisk(graphics, center[0], center[1], screenR, COLOR_CLAIM);
        drawScreenCircleOutline(graphics, center[0], center[1], screenR, COLOR_CLAIM_EDGE);
    }

    private void drawClaimBlockPixels(
            GuiGraphics graphics, VillageAtlasPayload.HamletEntry entry, WorldBounds visible) {
        int cx = entry.centerX();
        int cz = entry.centerZ();
        int r = entry.radius();
        long r2 = (long) r * r;

        int minX = Math.max(cx - r, (int) Math.floor(visible.minX));
        int maxX = Math.min(cx + r, (int) Math.ceil(visible.maxX));
        int minZ = Math.max(cz - r, (int) Math.floor(visible.minZ));
        int maxZ = Math.min(cz + r, (int) Math.ceil(visible.maxZ));

        for (int wx = minX; wx <= maxX; wx++) {
            int dx = wx - cx;
            long dx2 = (long) dx * dx;
            for (int wz = minZ; wz <= maxZ; wz++) {
                int dz = wz - cz;
                if (dx2 + (long) dz * dz > r2) {
                    continue;
                }
                drawWorldBlockBlended(graphics, wx, wz, COLOR_CLAIM, 0.45f);
            }
        }

        if (blocksPerPixel == MIN_BLOCKS_PER_PIXEL) {
            drawWorldBlock(graphics, cx, cz, COLOR_CLAIM_EDGE);
        }
    }

    private void drawExploredTerrain(GuiGraphics graphics) {
        double mapCenterX = mapLeft + mapSize * 0.5;
        double mapCenterY = mapTop + mapSize * 0.5;
        for (int sy = mapTop; sy < mapTop + mapSize; sy++) {
            for (int sx = mapLeft; sx < mapLeft + mapSize; sx++) {
                double wx = viewCenterX + (sx - mapCenterX) * blocksPerPixel;
                double wz = viewCenterZ + (sy - mapCenterY) * blocksPerPixel;
                int color = terrainColorAt(wx, wz);
                if (color != MapPalette.UNEXPLORED_ARGB) {
                    graphics.fill(sx, sy, sx + 1, sy + 1, color);
                }
            }
        }
    }

    private int terrainColorAt(double wx, double wz) {
        if (blocksPerPixel <= 2.0) {
            int bx = (int) Math.floor(wx);
            int bz = (int) Math.floor(wz);
            if (!AtlasExplorationClientCache.isExploredBlock(bx, bz)) {
                return MapPalette.UNEXPLORED_ARGB;
            }
            return AtlasExplorationClientCache.argbAtBlock(bx, bz);
        }
        int samples = (int) Math.min(8, Math.max(2, Math.ceil(blocksPerPixel)));
        double half = blocksPerPixel * 0.5;
        long rSum = 0;
        long gSum = 0;
        long bSum = 0;
        int count = 0;
        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < samples; j++) {
                double ox = (i + 0.5) / samples - 0.5;
                double oz = (j + 0.5) / samples - 0.5;
                int bx = (int) Math.floor(wx + ox * half * 2);
                int bz = (int) Math.floor(wz + oz * half * 2);
                if (!AtlasExplorationClientCache.isExploredBlock(bx, bz)) {
                    continue;
                }
                int argb = AtlasExplorationClientCache.argbAtBlock(bx, bz);
                rSum += (argb >> 16) & 0xFF;
                gSum += (argb >> 8) & 0xFF;
                bSum += argb & 0xFF;
                count++;
            }
        }
        if (count == 0) {
            return MapPalette.UNEXPLORED_ARGB;
        }
        return 0xFF000000
                | ((int) (rSum / count) << 16)
                | ((int) (gSum / count) << 8)
                | (int) (bSum / count);
    }

    private void drawWorldBlockBlended(
            GuiGraphics graphics, int worldX, int worldZ, int overlayArgb, float overlayStrength) {
        int sx = worldToScreenX(worldX);
        int sy = worldToScreenZ(worldZ);
        if (!inMap(sx, sy)) {
            return;
        }
        int base = AtlasExplorationClientCache.isExploredBlock(worldX, worldZ)
                ? AtlasExplorationClientCache.argbAtBlock(worldX, worldZ)
                : MapPalette.UNEXPLORED_ARGB;
        graphics.fill(sx, sy, sx + 1, sy + 1, blendArgb(base, overlayArgb, overlayStrength));
    }

    private static int blendArgb(int base, int overlay, float overlayStrength) {
        float baseStrength = 1.0f - overlayStrength;
        int br = (base >> 16) & 0xFF;
        int bg = (base >> 8) & 0xFF;
        int bb = base & 0xFF;
        int or = (overlay >> 16) & 0xFF;
        int og = (overlay >> 8) & 0xFF;
        int ob = overlay & 0xFF;
        int r = (int) (br * baseStrength + or * overlayStrength);
        int g = (int) (bg * baseStrength + og * overlayStrength);
        int b = (int) (bb * baseStrength + ob * overlayStrength);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    private void drawWorldBlock(GuiGraphics graphics, int worldX, int worldZ, int color) {
        int sx = worldToScreenX(worldX);
        int sy = worldToScreenZ(worldZ);
        if (!inMap(sx, sy)) {
            return;
        }
        graphics.fill(sx, sy, sx + 1, sy + 1, color);
    }

    private void fillScreenDisk(GuiGraphics graphics, int cx, int cy, int radius, int color) {
        for (int dy = -radius; dy <= radius; dy++) {
            int halfW = (int) Math.floor(Math.sqrt((double) radius * radius - (double) dy * dy));
            int y = cy + dy;
            if (y < mapTop || y >= mapTop + mapSize) {
                continue;
            }
            int x1 = cx - halfW;
            int x2 = cx + halfW;
            int clipL = Math.max(x1, mapLeft);
            int clipR = Math.min(x2 + 1, mapLeft + mapSize);
            if (clipL < clipR) {
                graphics.fill(clipL, y, clipR, y + 1, color);
            }
        }
    }

    private void drawScreenCircleOutline(GuiGraphics graphics, int cx, int cy, int radius, int color) {
        if (radius <= 1) {
            if (inMap(cx, cy)) {
                graphics.fill(cx, cy, cx + 1, cy + 1, color);
            }
            return;
        }
        int x = radius;
        int y = 0;
        int err = 1 - radius;
        while (x >= y) {
            plotCirclePoints(graphics, cx, cy, x, y, color);
            y++;
            if (err < 0) {
                err += 2 * y + 1;
            } else {
                x--;
                err += 2 * (y - x) + 1;
            }
        }
    }

    private void plotCirclePoints(GuiGraphics graphics, int cx, int cy, int x, int y, int color) {
        setMapPixel(graphics, cx + x, cy + y, color);
        setMapPixel(graphics, cx - x, cy + y, color);
        setMapPixel(graphics, cx + x, cy - y, color);
        setMapPixel(graphics, cx - x, cy - y, color);
        setMapPixel(graphics, cx + y, cy + x, color);
        setMapPixel(graphics, cx - y, cy + x, color);
        setMapPixel(graphics, cx + y, cy - x, color);
        setMapPixel(graphics, cx - y, cy - x, color);
    }

    private void setMapPixel(GuiGraphics graphics, int x, int y, int color) {
        if (inMap(x, y)) {
            graphics.fill(x, y, x + 1, y + 1, color);
        }
    }

    private record WorldBounds(double minX, double maxX, double minZ, double maxZ) {}

    private WorldBounds visibleWorldBounds() {
        double half = mapSize * 0.5 * blocksPerPixel;
        return new WorldBounds(
                viewCenterX - half, viewCenterX + half, viewCenterZ - half, viewCenterZ + half);
    }

    private int[] worldToScreen(int worldX, int worldZ) {
        return new int[] { worldToScreenX(worldX), worldToScreenZ(worldZ) };
    }

    private int worldToScreenX(int worldX) {
        int mapCenterX = mapLeft + mapSize / 2;
        return mapCenterX + (int) Math.floor((worldX - viewCenterX) / blocksPerPixel);
    }

    private int worldToScreenZ(int worldZ) {
        int mapCenterY = mapTop + mapSize / 2;
        return mapCenterY + (int) Math.floor((worldZ - viewCenterZ) / blocksPerPixel);
    }

    private double mouseWorldX(double mouseX) {
        double mapCenterX = mapLeft + mapSize * 0.5;
        return viewCenterX + (mouseX - mapCenterX) * blocksPerPixel;
    }

    private double mouseWorldZ(double mouseY) {
        double mapCenterY = mapTop + mapSize * 0.5;
        return viewCenterZ + (mouseY - mapCenterY) * blocksPerPixel;
    }

    private static boolean hamletContainsWorldBlock(VillageAtlasPayload.HamletEntry entry, double wx, double wz) {
        double dx = wx - entry.centerX();
        double dz = wz - entry.centerZ();
        return dx * dx + dz * dz <= (long) entry.radius() * entry.radius();
    }

    private boolean inMap(int sx, int sy) {
        return sx >= mapLeft && sx < mapLeft + mapSize && sy >= mapTop && sy < mapTop + mapSize;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isOverMap(mouseX, mouseY)) {
            dragging = true;
            dragLastX = mouseX;
            dragLastY = mouseY;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (dragging && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            viewCenterX -= (mouseX - dragLastX) * blocksPerPixel;
            viewCenterZ -= (mouseY - dragLastY) * blocksPerPixel;
            dragLastX = mouseX;
            dragLastY = mouseY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        layoutPanel();
        ensureViewInitialized();
        if (!isOverMap(mouseX, mouseY)) {
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        double anchorWorldX = mouseWorldX(mouseX);
        double anchorWorldZ = mouseWorldZ(mouseY);
        double mapCenterX = mapLeft + mapSize * 0.5;
        double mapCenterY = mapTop + mapSize * 0.5;
        double offsetX = mouseX - mapCenterX;
        double offsetY = mouseY - mapCenterY;

        double factor = scrollY > 0 ? 0.82 : 1.22;
        double newBlocksPerPixel =
                Math.clamp(blocksPerPixel * factor, MIN_BLOCKS_PER_PIXEL, MAX_BLOCKS_PER_PIXEL);
        if (newBlocksPerPixel == blocksPerPixel) {
            return true;
        }
        blocksPerPixel = newBlocksPerPixel;
        viewCenterX = anchorWorldX - offsetX * blocksPerPixel;
        viewCenterZ = anchorWorldZ - offsetY * blocksPerPixel;
        return true;
    }

    private boolean isOverMap(double mouseX, double mouseY) {
        return mouseX >= mapLeft && mouseX < mapLeft + mapSize && mouseY >= mapTop && mouseY < mapTop + mapSize;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
