package com.villagers.mod;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import com.villagers.mod.client.AtlasExplorationClientCache;
import com.villagers.mod.item.SoldierRankSpawnEggItem;
import com.villagers.mod.client.SquadBoxScreen;
import com.villagers.mod.client.VillagerGearScreen;
import com.villagers.mod.menu.VillagerMenus;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = VillagersMod.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = VillagersMod.MODID, value = Dist.CLIENT)
public class VillagersModClient {
    public VillagersModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        registerSpawnEggColors(event, VillagersMod.SEASONED_SOLDIER_SPAWN_EGG.get(), 0x5A4A2E, 0x8B7355);
        registerSpawnEggColors(event, VillagersMod.SERGEANT_SOLDIER_SPAWN_EGG.get(), 0x4A4A4A, 0xC0C0C0);
        registerSpawnEggColors(event, VillagersMod.HERO_SOLDIER_SPAWN_EGG.get(), 0x6B4423, 0xFFD700);
        registerSpawnEggColors(event, VillagersMod.LEGEND_SOLDIER_SPAWN_EGG.get(), 0x1A1A2E, 0x9B59B6);
    }

    private static void registerSpawnEggColors(RegisterColorHandlersEvent.Item event, SoldierRankSpawnEggItem item, int base, int accent) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? base : accent, item);
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(VillagerMenus.VILLAGER_GEAR.get(), VillagerGearScreen::new);
        event.register(VillagerMenus.SQUAD_BOX.get(), SquadBoxScreen::new);
    }

    @SubscribeEvent
    static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity().level().isClientSide()) {
            AtlasExplorationClientCache.clear();
        }
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        VillagersMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        VillagersMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
