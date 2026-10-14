package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.SoldierDesertion;
import com.villagers.mod.performance.DayEconomyWorkQueue;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class EconomyTickHandler {
    private static final Map<ServerLevel, Long> lastProcessedDay = new HashMap<>();

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        long day = level.getDayTime() / SoldierDesertion.TICKS_PER_DAY;
        Long previous = lastProcessedDay.get(level);
        if (previous == null || previous != day) {
            lastProcessedDay.put(level, day);
            DayEconomyWorkQueue.enqueueDayRollover(level);
        }
        DayEconomyWorkQueue.tick(level);
    }
}
