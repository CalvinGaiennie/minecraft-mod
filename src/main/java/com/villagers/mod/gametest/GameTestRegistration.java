package com.villagers.mod.gametest;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

import com.villagers.mod.VillagersMod;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class GameTestRegistration {
    @SubscribeEvent
    public static void registerGameTests(RegisterGameTestsEvent event) {
        for (var testClass : new Class<?>[] {
                Stage1GameTests.class, Stage23GameTests.class, Stage34GameTests.class, Stage56GameTests.class, MilitiaGameTests.class,
                ArmiesGameTests.class
        }) {
            for (var method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(net.minecraft.gametest.framework.GameTest.class)) {
                    event.register(method);
                }
            }
        }
    }
}
