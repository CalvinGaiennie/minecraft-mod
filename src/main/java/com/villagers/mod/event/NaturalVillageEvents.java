package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.village.NaturalVillageMilitiaService;

@EventBusSubscriber(modid = VillagersMod.MODID)
public final class NaturalVillageEvents {
    private NaturalVillageEvents() {
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.level().isClientSide()) {
            return;
        }
        if (!(player.serverLevel().dimension().location().getPath().equals("overworld"))) {
            return;
        }
        ChunkPos center = player.chunkPosition();
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                NaturalVillageMilitiaService.scanChunkForBells(player.serverLevel(), new ChunkPos(center.x + dx, center.z + dz));
            }
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level) || level.isClientSide()) {
            return;
        }
        if (!level.dimension().location().getPath().equals("overworld")) {
            return;
        }
        NaturalVillageMilitiaService.scanChunkForBells(level, event.getChunk().getPos());
    }
}
