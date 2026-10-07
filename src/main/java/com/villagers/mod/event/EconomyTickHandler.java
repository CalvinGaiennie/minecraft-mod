package com.villagers.mod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.entity.SupplyDepotBlockEntity;
import com.villagers.mod.economy.RecruiterService;
import com.villagers.mod.entity.SoldierDesertion;

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
        if (previous != null && previous == day) {
            return;
        }
        lastProcessedDay.put(level, day);
        RecruiterService.tickLevel(level);
        long week = day / 7L;
        tickSupplyNearPlayers(level, week);
    }

    private static void tickSupplyNearPlayers(ServerLevel level, long week) {
        for (var player : level.players()) {
            BlockPos center = player.blockPosition();
            for (BlockPos pos : BlockPos.betweenClosed(center.offset(-48, -8, -48), center.offset(48, 8, 48))) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof SupplyDepotBlockEntity depot) {
                    depot.tickWeeklySupply(week);
                }
            }
        }
    }
}
