package com.villagers.mod.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.armies.ArmiesQuestService;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class ArmiesRecipeEvents {
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack result = event.getCrafting();
        if (!result.is(VillagersMod.GARLAND_QUEST_COMPASS.get())) {
            return;
        }
        ArmiesQuestService.onGarlandCompassCrafted(player, result);
    }
}
