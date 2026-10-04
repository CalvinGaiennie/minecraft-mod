package com.villagers.mod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.SoldierBehavior;
import com.villagers.mod.entity.SoldierConversion;
import com.villagers.mod.entity.SoldierDesertion;
import com.villagers.mod.entity.VillagerAttachments;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class SoldierTickHandler {
    private static final Map<Level, Long> lastProcessedDay = new HashMap<>();

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (level.isClientSide || !(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return;
        }

        long day = level.getDayTime() / SoldierDesertion.TICKS_PER_DAY;
        Long previousDay = lastProcessedDay.get(level);
        boolean newDay = previousDay == null || previousDay != day;
        if (newDay) {
            lastProcessedDay.put(level, day);
        }

        boolean tryConversion = level.getGameTime() % 20 == 0;

        for (var entity : serverLevel.getAllEntities()) {
            if (!(entity instanceof Villager villager)) {
                continue;
            }

            if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                if (hasLostWeapon(villager)) {
                    SoldierConversion.dischargeHonorable(villager);
                    continue;
                }
                if (newDay) {
                    var data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
                    SoldierDesertion.recordNightWithoutBed(villager, data);
                    if (SoldierDesertion.shouldDesertNow(villager)) {
                        SoldierDesertion.desert(villager);
                        continue;
                    }
                }
                SoldierBehavior.updateDailyRoutine(villager);
            } else if (tryConversion) {
                SoldierConversion.tryConvert(villager);
            }
        }
    }

    private static boolean hasLostWeapon(Villager villager) {
        return villager.getMainHandItem().isEmpty();
    }
}
