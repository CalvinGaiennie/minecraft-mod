package com.villagers.mod.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import com.villagers.mod.block.entity.WantedPosterBlockEntity;
import com.villagers.mod.network.WantedPosterSavePayload;

import java.util.ArrayList;
import java.util.List;

public class WantedPosterEditScreen extends Screen {
    private final BlockPos pos;
    private final List<EditBox> lines = new ArrayList<>();

    public WantedPosterEditScreen(BlockPos pos, List<String> initial) {
        super(Component.translatable("screen.villagers.wanted_poster"));
        this.pos = pos;
        for (int i = 0; i < WantedPosterBlockEntity.MAX_NAMES; i++) {
            String text = i < initial.size() ? initial.get(i) : "";
            EditBox box = new EditBox(this.font, 0, 0, 200, 20, Component.empty());
            box.setValue(text);
            box.setMaxLength(32);
            lines.add(box);
        }
    }

    @Override
    protected void init() {
        int y = 40;
        for (EditBox box : lines) {
            box.setX(this.width / 2 - 100);
            box.setY(y);
            addRenderableWidget(box);
            y += 24;
        }
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> saveAndClose())
                .bounds(this.width / 2 - 50, y + 8, 100, 20)
                .build());
    }

    private void saveAndClose() {
        List<String> values = new ArrayList<>();
        for (EditBox box : lines) {
            values.add(box.getValue());
        }
        PacketDistributor.sendToServer(new WantedPosterSavePayload(pos, values));
        onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(
                this.font, this.title, this.width / 2, 16, 0xFFFFFF);
        graphics.drawCenteredString(
                this.font,
                Component.translatable("screen.villagers.wanted_poster.hint"),
                this.width / 2,
                28,
                0xA0A0A0);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
