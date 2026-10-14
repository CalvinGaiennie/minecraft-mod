package com.villagers.mod.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;

import com.villagers.mod.VillagersMod;

@EventBusSubscriber(modid = VillagersMod.MODID, value = Dist.CLIENT)
public final class CombatHealthBarRenderer {
    private static final int SEGMENTS = 10;

    private CombatHealthBarRenderer() {
    }

    /** Draw HP on the nameplate (reliable in world space; GUI quads on entities often do not show). */
    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event) {
        if (!(event.getEntity() instanceof Villager villager)) {
            return;
        }
        if (!shouldDrawHealthBar(villager)) {
            return;
        }
        MutableComponent label = Component.empty().append(event.getOriginalContent());
        label.append(Component.literal("\n"));
        label.append(healthBarLine(villager));
        event.setContent(label);
    }

    static boolean shouldDrawHealthBar(Villager villager) {
        if (!CombatUnitVisuals.showsCombatHealthBar(villager)) {
            return false;
        }
        return villager.getHealth() < villager.getMaxHealth() - 0.01F;
    }

    private static Component healthBarLine(Villager villager) {
        float fraction = Mth.clamp(villager.getHealth() / villager.getMaxHealth(), 0.0F, 1.0F);
        int filled = Mth.clamp(Math.round(fraction * SEGMENTS), 0, SEGMENTS);
        ChatFormatting fillColor = fraction > 0.35F ? ChatFormatting.GREEN : ChatFormatting.YELLOW;

        MutableComponent line = Component.literal("").withStyle(ChatFormatting.DARK_GRAY);
        line.append(Component.literal("["));
        for (int i = 0; i < SEGMENTS; i++) {
            line.append(Component.literal("|").withStyle(i < filled ? fillColor : ChatFormatting.DARK_GRAY));
        }
        line.append(Component.literal("] "));
        line.append(Component.literal(String.format("%.0f/%.0f", villager.getHealth(), villager.getMaxHealth()))
                .withStyle(ChatFormatting.WHITE));
        return line;
    }
}
