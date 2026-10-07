package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;
import java.util.Map;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.threat.NewMoonRaidService;
import com.villagers.mod.threat.SwarmService;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class ThreatTickHandler {
    private static final Map<ResourceKey<Level>, Long> lastSwarmDayByDimension = new HashMap<>();

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (level.getGameTime() % 20 == 0) {
            SwarmService.tick(level);
        }
        long day = level.getDayTime() / 24000L;
        long lastSwarmDay = lastSwarmDayByDimension.getOrDefault(level.dimension(), -1L);
        if (day != lastSwarmDay && level.getDayTime() % 24000L < 100) {
            lastSwarmDayByDimension.put(level.dimension(), day);
            for (var player : level.players()) {
                SwarmService.trySpawnNightly(level, player);
            }
        }
        if (level.getGameTime() % 100 == 0) {
            for (var player : level.players()) {
                NewMoonRaidService.tickForPlayer(level, player);
            }
        }
    }
}
