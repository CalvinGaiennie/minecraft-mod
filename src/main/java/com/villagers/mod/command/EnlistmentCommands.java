package com.villagers.mod.command;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.player.EnlistmentService;

@EventBusSubscriber(modid = VillagersMod.MODID)
public final class EnlistmentCommands {
    private EnlistmentCommands() {
    }

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("villagers")
                        .then(Commands.literal("enlist").executes(ctx -> {
                            EnlistmentService.enlist(ctx.getSource().getPlayerOrException());
                            ctx.getSource().sendSuccess(() -> Component.translatable("message.villagers.enlisted"), true);
                            return 1;
                        }))
                        .then(Commands.literal("optout").executes(ctx -> {
                            EnlistmentService.optOut(ctx.getSource().getPlayerOrException());
                            ctx.getSource().sendSuccess(() -> Component.translatable("message.villagers.opted_out"), true);
                            return 1;
                        })));
    }
}
