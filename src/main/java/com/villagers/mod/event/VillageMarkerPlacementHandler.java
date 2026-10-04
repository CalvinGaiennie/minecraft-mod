package com.villagers.mod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.village.VillageManager;

import java.util.UUID;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class VillageMarkerPlacementHandler {

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent placeEvent) {
        if (!(placeEvent.getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return;
        }
        if (!(placeEvent.getPlacedBlock().getBlock() instanceof com.villagers.mod.block.VillageMarkerBlock)) {
            return;
        }
        if (!(placeEvent.getEntity() instanceof net.minecraft.world.entity.player.Player player)) {
            return;
        }

        var pos = placeEvent.getPos();
        UUID playerId = player.getUUID();
        var manager = VillageManager.get(serverLevel);

        if (!manager.canClaim(playerId, pos.getX(), pos.getZ(), VillageManager.HAMLET_RADIUS)) {
            placeEvent.setCanceled(true);
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("Surveyor's markers can't overlap another player's village."),
                    true);
        }
    }
}
