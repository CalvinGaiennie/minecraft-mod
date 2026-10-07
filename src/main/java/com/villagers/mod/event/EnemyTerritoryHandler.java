package com.villagers.mod.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;
import com.villagers.mod.village.VillageProtection;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class EnemyTerritoryHandler {

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        var pos = event.getPos();
        VillageData village = VillageManager.get(level).findVillageAt(pos.getX(), pos.getY(), pos.getZ());
        if (village == null || !village.isActive()) {
            return;
        }
        if (VillageProtection.mayBypassProtection(player.getUUID(), village)) {
            return;
        }
        event.setCanceled(true);
        player.displayClientMessage(Component.translatable("message.villagers.enemy_land_build"), true);
    }
}
