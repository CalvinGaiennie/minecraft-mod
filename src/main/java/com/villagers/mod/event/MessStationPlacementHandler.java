package com.villagers.mod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;

import java.util.UUID;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class MessStationPlacementHandler {

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent event) {
        if (!(event instanceof BlockEvent.EntityPlaceEvent placeEvent)) {
            return;
        }

        var levelAccessor = placeEvent.getLevel();
        if (!(levelAccessor instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return;
        }

        if (!(placeEvent.getPlacedBlock().getBlock() instanceof com.villagers.mod.block.MessStationBlock)) {
            return;
        }

        if (!(placeEvent.getEntity() instanceof net.minecraft.world.entity.player.Player player)) {
            return;
        }

        var pos = placeEvent.getPos();
        UUID playerId = player.getUUID();

        var manager = VillageManager.get(serverLevel);

        VillageData existingVillage = manager.findVillageAt(pos.getX(), pos.getY(), pos.getZ());

        if (existingVillage == null) {
            String villageName = "Village of " + player.getName().getString();
            VillageData newVillage = new VillageData(villageName, playerId, pos.getX(), pos.getY(), pos.getZ());
            manager.registerVillage(newVillage);
        } else if (existingVillage.getOwnerId() == null || !existingVillage.getOwnerId().equals(playerId)) {
            existingVillage.setOwnerId(playerId);
        }
    }
}
