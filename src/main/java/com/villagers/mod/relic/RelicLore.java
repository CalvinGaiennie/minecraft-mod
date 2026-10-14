package com.villagers.mod.relic;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import com.villagers.mod.VillagersMod;

import java.util.List;

public final class RelicLore {
    private RelicLore() {
    }

    public static void append(RelicKind kind, List<Component> tooltip) {
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("item.villagers.relic.lore_header").withStyle(ChatFormatting.DARK_AQUA));
        for (int line = 1; line <= kind.loreLines(); line++) {
            tooltip.add(Component.translatable(loreKey(kind, line)).withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(Component.translatable("item.villagers.relic.lore_footer").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
    }

    private static String loreKey(RelicKind kind, int line) {
        return "item." + VillagersMod.MODID + ".relic." + kind.id() + ".lore." + line;
    }
}
