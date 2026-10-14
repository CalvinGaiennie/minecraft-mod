package com.villagers.mod.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import com.villagers.mod.network.AttackPlanOpenPayload;
import com.villagers.mod.network.AttackPlanSavePayload;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttackPlanScreen extends Screen {
    private final List<AttackPlanOpenPayload.SquadEntry> squads;
    private final Set<Long> selected = new HashSet<>();
    private EditBox targetX;
    private EditBox targetY;
    private EditBox targetZ;
    private EditBox laddersField;

    public AttackPlanScreen(AttackPlanOpenPayload payload) {
        super(Component.translatable("screen.villagers.attack_plan"));
        this.squads = new ArrayList<>(payload.squads());
        for (var entry : payload.squads()) {
            if (entry.selected()) {
                selected.add(entry.boxPos());
            }
        }
        BlockPos target = payload.target();
        this.initialTarget = target;
        this.initialLadders = payload.siegeLadders();
    }

    private final BlockPos initialTarget;
    private final int initialLadders;

    @Override
    protected void init() {
        int y = 36;
        for (AttackPlanOpenPayload.SquadEntry entry : squads) {
            int rowY = y;
            long box = entry.boxPos();
            BlockPos pos = BlockPos.of(box);
            boolean on = selected.contains(box);
            addRenderableWidget(Button.builder(
                            Component.literal((on ? "[x] " : "[ ] ") + entry.members() + " @ " + pos.getX() + "," + pos.getZ()),
                            b -> toggle(box))
                    .bounds(this.width / 2 - 160, rowY, 320, 20)
                    .build());
            y += 24;
        }
        targetX = new EditBox(this.font, this.width / 2 - 154, y + 8, 50, 20, Component.empty());
        targetY = new EditBox(this.font, this.width / 2 - 98, y + 8, 50, 20, Component.empty());
        targetZ = new EditBox(this.font, this.width / 2 - 42, y + 8, 50, 20, Component.empty());
        laddersField = new EditBox(this.font, this.width / 2 + 20, y + 8, 40, 20, Component.empty());
        if (initialTarget != null) {
            targetX.setValue(String.valueOf(initialTarget.getX()));
            targetY.setValue(String.valueOf(initialTarget.getY()));
            targetZ.setValue(String.valueOf(initialTarget.getZ()));
        }
        laddersField.setValue(String.valueOf(initialLadders));
        addRenderableWidget(targetX);
        addRenderableWidget(targetY);
        addRenderableWidget(targetZ);
        addRenderableWidget(laddersField);
        addRenderableWidget(Button.builder(Component.translatable("screen.villagers.attack_plan.here"), b -> setTargetFromPlayer())
                .bounds(this.width / 2 + 68, y + 8, 60, 20)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> save())
                .bounds(this.width / 2 - 40, y + 36, 80, 20)
                .build());
    }

    private void toggle(long box) {
        if (!selected.remove(box)) {
            selected.add(box);
        }
        this.clearWidgets();
        this.init();
    }

    private void setTargetFromPlayer() {
        if (minecraft != null && minecraft.player != null) {
            BlockPos p = minecraft.player.blockPosition();
            targetX.setValue(String.valueOf(p.getX()));
            targetY.setValue(String.valueOf(p.getY()));
            targetZ.setValue(String.valueOf(p.getZ()));
        }
    }

    private void save() {
        long packed = 0L;
        try {
            int x = Integer.parseInt(targetX.getValue().trim());
            int y = Integer.parseInt(targetY.getValue().trim());
            int z = Integer.parseInt(targetZ.getValue().trim());
            packed = new BlockPos(x, y, z).asLong();
        } catch (NumberFormatException ignored) {
            packed = 0L;
        }
        int ladders = 0;
        try {
            ladders = Integer.parseInt(laddersField.getValue().trim());
        } catch (NumberFormatException ignored) {
            ladders = 0;
        }
        PacketDistributor.sendToServer(new AttackPlanSavePayload(new ArrayList<>(selected), packed, ladders));
        onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
        graphics.drawCenteredString(
                this.font,
                Component.translatable("screen.villagers.attack_plan.hint"),
                this.width / 2,
                22,
                0xA0A0A0);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
