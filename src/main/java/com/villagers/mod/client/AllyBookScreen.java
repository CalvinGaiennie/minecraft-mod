package com.villagers.mod.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import com.villagers.mod.network.AllyBookActionPayload;
import com.villagers.mod.network.AllyBookOpenPayload;

import java.util.ArrayList;
import java.util.List;

public class AllyBookScreen extends Screen {
    private final List<AllyBookOpenPayload.Entry> entries;
    private EditBox addField;

    public AllyBookScreen(AllyBookOpenPayload payload) {
        super(Component.translatable("screen.villagers.ally_book"));
        this.entries = new ArrayList<>(payload.entries());
    }

    @Override
    protected void init() {
        int y = 32;
        for (AllyBookOpenPayload.Entry entry : entries) {
            int rowY = y;
            addRenderableWidget(Button.builder(Component.literal(entry.displayName() + " (" + entry.rank() + ")"), b -> {})
                    .bounds(this.width / 2 - 154, rowY, 200, 20)
                    .build());
            addRenderableWidget(Button.builder(Component.literal("X"), b -> remove(entry.id()))
                    .bounds(this.width / 2 + 50, rowY, 20, 20)
                    .build());
            addRenderableWidget(Button.builder(Component.literal("O"), b -> setRank(entry.id(), "officer"))
                    .bounds(this.width / 2 + 74, rowY, 20, 20)
                    .build());
            addRenderableWidget(Button.builder(Component.literal("C"), b -> setRank(entry.id(), "co-owner"))
                    .bounds(this.width / 2 + 98, rowY, 20, 20)
                    .build());
            y += 24;
        }
        addField = new EditBox(this.font, this.width / 2 - 100, y + 8, 140, 20, Component.empty());
        addField.setMaxLength(32);
        addRenderableWidget(addField);
        addRenderableWidget(Button.builder(Component.translatable("gui.add"), b -> add("friend"))
                .bounds(this.width / 2 + 44, y + 8, 56, 20)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> onClose())
                .bounds(this.width / 2 - 40, y + 36, 80, 20)
                .build());
    }

    private void add(String rank) {
        PacketDistributor.sendToServer(new AllyBookActionPayload(
                AllyBookActionPayload.Action.ADD, addField.getValue(), rank));
    }

    private void remove(String id) {
        PacketDistributor.sendToServer(new AllyBookActionPayload(
                AllyBookActionPayload.Action.REMOVE, id, "friend"));
    }

    private void setRank(String id, String rank) {
        PacketDistributor.sendToServer(new AllyBookActionPayload(
                AllyBookActionPayload.Action.SET_RANK, id, rank));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
        graphics.drawCenteredString(
                this.font,
                Component.translatable("screen.villagers.ally_book.hint"),
                this.width / 2,
                22,
                0xA0A0A0);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
