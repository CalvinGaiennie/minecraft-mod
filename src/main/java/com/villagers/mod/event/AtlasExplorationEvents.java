package com.villagers.mod.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.atlas.AtlasExplorationService;

@EventBusSubscriber(modid = VillagersMod.MODID)
public final class AtlasExplorationEvents {
    private AtlasExplorationEvents() {
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AtlasExplorationService.queueTerrainSync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AtlasExplorationService.tickPlayer(player);
        }
    }
}
