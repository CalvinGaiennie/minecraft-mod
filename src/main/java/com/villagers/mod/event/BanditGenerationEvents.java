package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.armies.BanditGenerationScheduler;

@EventBusSubscriber(modid = VillagersMod.MODID)
public final class BanditGenerationEvents {
    private BanditGenerationEvents() {
    }

    /** Start after join so world loading is not blocked by distant chunk gen. */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ServerLevel level = player.serverLevel();
        if (!level.dimension().equals(Level.OVERWORLD)) {
            return;
        }
        BanditGenerationScheduler.scheduleIfNeeded(level);
    }
}
