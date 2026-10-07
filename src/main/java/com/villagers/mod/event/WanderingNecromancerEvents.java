package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.threat.WanderingNecromancerService;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class WanderingNecromancerEvents {
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) {
            return;
        }
        WanderingNecromancerService.onExOwnerLogin(level, event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Villager villager) || villager.level().isClientSide) {
            return;
        }
        if (!WanderingNecromancerService.isWanderingNecromancer(villager)) {
            return;
        }
        if (villager.level() instanceof ServerLevel level) {
            WanderingNecromancerService.onNecromancerDeath(level, villager);
        }
    }
}
