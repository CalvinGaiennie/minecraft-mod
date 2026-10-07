package com.villagers.mod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import com.villagers.mod.block.ArrowBinBlock;
import com.villagers.mod.block.CaltropBlock;
import com.villagers.mod.block.CampBlock;
import com.villagers.mod.block.FallbackBlock;
import com.villagers.mod.block.HavenBlock;
import com.villagers.mod.block.MessStationBlock;
import com.villagers.mod.block.PostBlock;
import com.villagers.mod.block.RampartBlock;
import com.villagers.mod.block.RecruiterBlock;
import com.villagers.mod.block.RoyalGuardPostBlock;
import com.villagers.mod.block.SupplyDepotBlock;
import com.villagers.mod.block.TaxBoxBlock;
import com.villagers.mod.block.MobSpawnerCapBlock;
import com.villagers.mod.block.SimpleModBlock;
import com.villagers.mod.block.TrainingDummyBlock;
import com.villagers.mod.block.VillageMarkerBlock;
import com.villagers.mod.block.entity.VillagerBlockEntities;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.event.VillagerAttributeSetup;
import com.villagers.mod.item.AwakenHornItem;
import com.villagers.mod.item.CaltropsItem;
import com.villagers.mod.item.CampaignBootsItem;
import com.villagers.mod.item.VillageBannerItem;
import com.villagers.mod.item.MusterRollItem;
import com.villagers.mod.item.NightwatchHelmetItem;
import com.villagers.mod.item.ReassignmentWritItem;
import com.villagers.mod.item.RoyalGuardChestplateItem;
import com.villagers.mod.item.NecromancerWandItem;
import com.villagers.mod.item.VillageChronicleItem;
import com.villagers.mod.item.VillageMapItem;
import com.villagers.mod.item.VeteranSwordItem;
import com.villagers.mod.item.NecromancerHoodItem;
import com.villagers.mod.item.NecromancerRobeItem;
import com.villagers.mod.menu.VillagerMenus;
import com.villagers.mod.network.VillagersModPayloadRegistration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(VillagersMod.MODID)
public class VillagersMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "villagers";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> MESS_STATION = BLOCKS.register("mess_station", () -> new MessStationBlock());
    public static final DeferredItem<BlockItem> MESS_STATION_ITEM = ITEMS.register("mess_station", () -> new BlockItem(MESS_STATION.get(), new Item.Properties()));

    public static final DeferredBlock<Block> POST_BLOCK = BLOCKS.register("post_block", PostBlock::new);
    public static final DeferredItem<BlockItem> POST_BLOCK_ITEM = ITEMS.register("post_block", () -> new BlockItem(POST_BLOCK.get(), new Item.Properties()));

    public static final DeferredBlock<Block> VILLAGE_MARKER = BLOCKS.register("village_marker", () -> new VillageMarkerBlock());
    public static final DeferredItem<BlockItem> VILLAGE_MARKER_ITEM = ITEMS.register("village_marker", () -> new BlockItem(VILLAGE_MARKER.get(), new Item.Properties()));

    public static final DeferredBlock<Block> RAMPART = BLOCKS.register("rampart", RampartBlock::new);
    public static final DeferredItem<BlockItem> RAMPART_ITEM = ITEMS.register("rampart", () -> new BlockItem(RAMPART.get(), new Item.Properties()));

    public static final DeferredBlock<Block> FALLBACK_BLOCK = BLOCKS.register("fallback_block", () -> new FallbackBlock());
    public static final DeferredItem<BlockItem> FALLBACK_BLOCK_ITEM = ITEMS.register("fallback_block", () -> new BlockItem(FALLBACK_BLOCK.get(), new Item.Properties()));

    public static final DeferredBlock<Block> TAX_BOX = BLOCKS.register("tax_box", () -> new TaxBoxBlock());
    public static final DeferredItem<BlockItem> TAX_BOX_ITEM = ITEMS.register("tax_box", () -> new BlockItem(TAX_BOX.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ARROW_BIN = BLOCKS.register("arrow_bin", () -> new ArrowBinBlock());
    public static final DeferredItem<BlockItem> ARROW_BIN_ITEM = ITEMS.register("arrow_bin", () -> new BlockItem(ARROW_BIN.get(), new Item.Properties()));

    public static final DeferredBlock<Block> RECRUITER_BOX = BLOCKS.register("recruiter_box", () -> new RecruiterBlock());
    public static final DeferredItem<BlockItem> RECRUITER_BOX_ITEM = ITEMS.register("recruiter_box", () -> new BlockItem(RECRUITER_BOX.get(), new Item.Properties()));

    public static final DeferredBlock<Block> SUPPLY_DEPOT = BLOCKS.register("supply_depot", () -> new SupplyDepotBlock());
    public static final DeferredItem<BlockItem> SUPPLY_DEPOT_ITEM = ITEMS.register("supply_depot", () -> new BlockItem(SUPPLY_DEPOT.get(), new Item.Properties()));

    public static final DeferredBlock<Block> HAVEN_BLOCK = BLOCKS.register("haven_block", () -> new HavenBlock());
    public static final DeferredItem<BlockItem> HAVEN_BLOCK_ITEM = ITEMS.register("haven_block", () -> new BlockItem(HAVEN_BLOCK.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ROYAL_GUARD_POST = BLOCKS.register("royal_guard_post", () -> new RoyalGuardPostBlock());
    public static final DeferredItem<BlockItem> ROYAL_GUARD_POST_ITEM = ITEMS.register("royal_guard_post", () -> new BlockItem(ROYAL_GUARD_POST.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CAMP_BLOCK = BLOCKS.register("camp_block", () -> new CampBlock());
    public static final DeferredItem<BlockItem> CAMP_BLOCK_ITEM = ITEMS.register("camp_block", () -> new BlockItem(CAMP_BLOCK.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CALTROP_BLOCK = BLOCKS.register("caltrop", () -> new CaltropBlock());
    public static final DeferredItem<BlockItem> CALTROPS = ITEMS.register("caltrops", () -> new CaltropsItem(CALTROP_BLOCK.get(), new Item.Properties().stacksTo(64)));

    public static final DeferredItem<VeteranSwordItem> VETERAN_SWORD = ITEMS.register("veteran_sword", () -> new VeteranSwordItem(new Item.Properties()));

    public static final DeferredItem<NightwatchHelmetItem> NIGHTWATCH_HELMET =
            ITEMS.register("nightwatch_helmet", () -> new NightwatchHelmetItem(new Item.Properties()));
    public static final DeferredItem<RoyalGuardChestplateItem> ROYAL_GUARD_CHESTPLATE =
            ITEMS.register("royal_guard_chestplate", () -> new RoyalGuardChestplateItem(new Item.Properties()));
    public static final DeferredItem<CampaignBootsItem> CAMPAIGN_BOOTS =
            ITEMS.register("campaign_boots", () -> new CampaignBootsItem(new Item.Properties()));
    public static final DeferredItem<ReassignmentWritItem> REASSIGNMENT_WRIT =
            ITEMS.register("reassignment_writ", () -> new ReassignmentWritItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<VillageBannerItem> VILLAGE_BANNER =
            ITEMS.register("village_banner", () -> new VillageBannerItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<AwakenHornItem> AWAKEN_HORN = ITEMS.register("awaken_horn", () -> new AwakenHornItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<MusterRollItem> MUSTER_ROLL = ITEMS.register("muster_roll", () -> new MusterRollItem(new Item.Properties().stacksTo(1)));

    public static final DeferredBlock<Block> BANDIT_CAMP = BLOCKS.register("bandit_camp", () -> new SimpleModBlock(Blocks.RED_WOOL));
    public static final DeferredItem<BlockItem> BANDIT_CAMP_ITEM = ITEMS.register("bandit_camp", () -> new BlockItem(BANDIT_CAMP.get(), new Item.Properties()));
    public static final DeferredBlock<Block> NECROMANCER_CRYPT = BLOCKS.register("necromancer_crypt", () -> new SimpleModBlock(Blocks.DEEPSLATE_BRICKS));
    public static final DeferredItem<BlockItem> NECROMANCER_CRYPT_ITEM = ITEMS.register("necromancer_crypt", () -> new BlockItem(NECROMANCER_CRYPT.get(), new Item.Properties()));
    public static final DeferredBlock<Block> WILD_RANCH = BLOCKS.register("wild_ranch", () -> new SimpleModBlock(Blocks.HAY_BLOCK));
    public static final DeferredItem<BlockItem> WILD_RANCH_ITEM = ITEMS.register("wild_ranch", () -> new BlockItem(WILD_RANCH.get(), new Item.Properties()));
    public static final DeferredBlock<Block> FARMER_STATION = BLOCKS.register("farmer_station", () -> new SimpleModBlock(Blocks.COMPOSTER));
    public static final DeferredItem<BlockItem> FARMER_STATION_ITEM = ITEMS.register("farmer_station", () -> new BlockItem(FARMER_STATION.get(), new Item.Properties()));
    public static final DeferredBlock<Block> MOB_SPAWNER_CAP = BLOCKS.register("mob_spawner_cap", () -> new MobSpawnerCapBlock());
    public static final DeferredItem<BlockItem> MOB_SPAWNER_CAP_ITEM = ITEMS.register("mob_spawner_cap", () -> new BlockItem(MOB_SPAWNER_CAP.get(), new Item.Properties()));
    public static final DeferredBlock<Block> TRAINING_DUMMY = BLOCKS.register("training_dummy", () -> new TrainingDummyBlock());
    public static final DeferredItem<BlockItem> TRAINING_DUMMY_ITEM = ITEMS.register("training_dummy", () -> new BlockItem(TRAINING_DUMMY.get(), new Item.Properties()));
    public static final DeferredBlock<Block> GRAVE_MARKER = BLOCKS.register("grave_marker", () -> new SimpleModBlock(Blocks.CRYING_OBSIDIAN));
    public static final DeferredItem<BlockItem> GRAVE_MARKER_ITEM = ITEMS.register("grave_marker", () -> new BlockItem(GRAVE_MARKER.get(), new Item.Properties()));
    public static final DeferredBlock<Block> CURSED_EFFIGY = BLOCKS.register("cursed_effigy", () -> new SimpleModBlock(Blocks.SOUL_SAND));
    public static final DeferredItem<BlockItem> CURSED_EFFIGY_ITEM = ITEMS.register("cursed_effigy", () -> new BlockItem(CURSED_EFFIGY.get(), new Item.Properties()));
    public static final DeferredItem<NecromancerWandItem> NECROMANCER_WAND =
            ITEMS.register("necromancer_wand", () -> new NecromancerWandItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> PHYLACTERY_SHARD =
            ITEMS.register("phylactery_shard", () -> new Item(new Item.Properties()));
    public static final DeferredItem<NecromancerHoodItem> NECROMANCER_HOOD =
            ITEMS.register("necromancer_hood", () -> new NecromancerHoodItem(new Item.Properties()));
    public static final DeferredItem<NecromancerRobeItem> NECROMANCER_ROBE =
            ITEMS.register("necromancer_robe", () -> new NecromancerRobeItem(new Item.Properties()));
    public static final DeferredItem<Item> SOUL_BOTTLE =
            ITEMS.register("soul_bottle", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> GRAVE_SHROUD =
            ITEMS.register("grave_shroud", () -> new Item(new Item.Properties()));
    public static final DeferredItem<VillageChronicleItem> VILLAGE_CHRONICLE =
            ITEMS.register("village_chronicle", () -> new VillageChronicleItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<VillageMapItem> VILLAGE_MAP =
            ITEMS.register("village_map", () -> new VillageMapItem(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VILLAGERS_TAB = CREATIVE_MODE_TABS.register("villagers_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.villagers"))
            .icon(() -> MESS_STATION_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(MESS_STATION_ITEM.get());
                output.accept(POST_BLOCK_ITEM.get());
                output.accept(VILLAGE_MARKER_ITEM.get());
                output.accept(RAMPART_ITEM.get());
                output.accept(FALLBACK_BLOCK_ITEM.get());
                output.accept(TAX_BOX_ITEM.get());
                output.accept(ARROW_BIN_ITEM.get());
                output.accept(RECRUITER_BOX_ITEM.get());
                output.accept(SUPPLY_DEPOT_ITEM.get());
                output.accept(HAVEN_BLOCK_ITEM.get());
                output.accept(ROYAL_GUARD_POST_ITEM.get());
                output.accept(CAMP_BLOCK_ITEM.get());
                output.accept(NIGHTWATCH_HELMET.get());
                output.accept(ROYAL_GUARD_CHESTPLATE.get());
                output.accept(CAMPAIGN_BOOTS.get());
                output.accept(REASSIGNMENT_WRIT.get());
                output.accept(VILLAGE_BANNER.get());
                output.accept(CALTROPS.get());
                output.accept(VETERAN_SWORD.get());
                output.accept(AWAKEN_HORN.get());
                output.accept(MUSTER_ROLL.get());
                output.accept(BANDIT_CAMP_ITEM.get());
                output.accept(NECROMANCER_CRYPT_ITEM.get());
                output.accept(WILD_RANCH_ITEM.get());
                output.accept(FARMER_STATION_ITEM.get());
                output.accept(MOB_SPAWNER_CAP_ITEM.get());
                output.accept(TRAINING_DUMMY_ITEM.get());
                output.accept(GRAVE_MARKER_ITEM.get());
                output.accept(CURSED_EFFIGY_ITEM.get());
                output.accept(NECROMANCER_WAND.get());
                output.accept(PHYLACTERY_SHARD.get());
                output.accept(NECROMANCER_HOOD.get());
                output.accept(NECROMANCER_ROBE.get());
                output.accept(SOUL_BOTTLE.get());
                output.accept(GRAVE_SHROUD.get());
                output.accept(VILLAGE_CHRONICLE.get());
                output.accept(VILLAGE_MAP.get());
            })
            .withTabsAfter(CreativeModeTabs.SEARCH)
            .build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public VillagersMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(VillagerAttributeSetup::onModifyAttributes);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so block entities get registered
        VillagerBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so attachments get registered
        VillagerAttachments.ATTACHMENTS.register(modEventBus);
        VillagerMenus.MENUS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(VillagersModPayloadRegistration::registerPayloads);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(MESS_STATION_ITEM);
            event.accept(POST_BLOCK_ITEM);
            event.accept(VILLAGE_MARKER_ITEM);
            event.accept(RAMPART_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(AWAKEN_HORN);
            event.accept(MUSTER_ROLL);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
