package com.villagers.mod.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import com.villagers.mod.VillagersMod;

@EventBusSubscriber(modid = VillagersMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var output = gen.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));
    }
}
