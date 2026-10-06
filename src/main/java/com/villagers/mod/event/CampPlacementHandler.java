package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.war.CampaignService;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class CampPlacementHandler {
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!event.getPlacedBlock().is(VillagersMod.CAMP_BLOCK.get())) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        BlockEntity be = level.getBlockEntity(event.getPos());
        if (be == null) {
            level.setBlockEntity(new com.villagers.mod.block.entity.CampBlockEntity(
                    event.getPos(), event.getPlacedBlock()));
        }
        CampaignService.onCampPlaced(level, event.getPos(), player);
    }
}
