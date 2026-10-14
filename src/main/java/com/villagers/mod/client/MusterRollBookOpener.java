package com.villagers.mod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;

import com.villagers.mod.network.MusterRollBookPayload;

import java.util.ArrayList;
import java.util.List;

public final class MusterRollBookOpener {
    private MusterRollBookOpener() {
    }

    public static void open(MusterRollBookPayload payload) {
        List<Component> pages = new ArrayList<>();
        for (String page : payload.pages()) {
            pages.add(Component.literal(page));
        }
        Minecraft.getInstance().setScreen(new BookViewScreen(new BookViewScreen.BookAccess(pages)));
    }
}
