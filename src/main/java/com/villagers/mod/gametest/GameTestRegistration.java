package com.villagers.mod.gametest;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

import com.villagers.mod.VillagersMod;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class GameTestRegistration {
    @SubscribeEvent
    public static void registerGameTests(RegisterGameTestsEvent event) {
        for (var method : Stage1GameTests.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(net.minecraft.gametest.framework.GameTest.class)) {
                event.register(method);
            }
        }
    }
}
