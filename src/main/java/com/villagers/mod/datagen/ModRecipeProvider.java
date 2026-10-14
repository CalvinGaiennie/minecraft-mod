package com.villagers.mod.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import com.villagers.mod.item.LeverHornItem;
import com.villagers.mod.item.SoldierButtonItem;
import com.villagers.mod.item.SoldierLeverItem;
import com.villagers.mod.item.SignalRocketItem;
import com.villagers.mod.war.LeverHornService;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import com.villagers.mod.VillagersMod;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        buildStructureBlocks(recipeOutput);
        buildCommandItems(recipeOutput);
        buildAnimalJobBlocks(recipeOutput);
        buildNecromancerItems(recipeOutput);
        buildNecromancerGear(recipeOutput);
        buildWarAndUtility(recipeOutput);
    }

    private void buildStructureBlocks(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.MESS_STATION.get())
                .pattern("SSS")
                .pattern(" B ")
                .define('S', ItemTags.WOODEN_SLABS)
                .define('B', Items.BARREL)
                .unlockedBy("has_barrel", has(Items.BARREL))
                .save(recipeOutput, id("mess_station"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.POST_BLOCK.get())
                .pattern("S")
                .pattern("I")
                .define('S', Items.COBBLESTONE_SLAB)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput, id("post_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.VILLAGE_MARKER.get())
                .pattern("SAS")
                .pattern("ACA")
                .pattern("SAS")
                .define('S', Items.STONE)
                .define('A', Items.AMETHYST_BLOCK)
                .define('C', Items.LAPIS_BLOCK)
                .unlockedBy("has_lapis", has(Items.LAPIS_BLOCK))
                .save(recipeOutput, id("village_marker"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.TAX_BOX.get())
                .pattern(" G ")
                .pattern("ICI")
                .pattern("I I")
                .define('G', Items.GOLD_INGOT)
                .define('I', Items.IRON_INGOT)
                .define('C', Items.CHEST)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(recipeOutput, id("tax_box"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.RECRUITER_BOX.get())
                .requires(Items.CHEST)
                .requires(Items.IRON_SWORD)
                .requires(ItemTags.BANNERS)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(recipeOutput, id("recruiter_box"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.ARROW_BIN.get())
                .requires(Items.BARREL)
                .requires(Items.ARROW)
                .requires(Items.STRING)
                .unlockedBy("has_barrel", has(Items.BARREL))
                .save(recipeOutput, id("arrow_bin"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.RAMPART.get())
                .requires(Items.STONE_BRICKS)
                .requires(Items.STONE_BRICK_WALL)
                .requires(Items.IRON_INGOT)
                .unlockedBy("has_stone_bricks", has(Items.STONE_BRICKS))
                .save(recipeOutput, id("rampart"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.FALLBACK_BLOCK.get())
                .requires(ItemTags.BANNERS)
                .requires(Items.COBBLESTONE_WALL)
                .unlockedBy("has_banner", has(ItemTags.BANNERS))
                .save(recipeOutput, id("fallback_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.HAVEN_BLOCK.get())
                .requires(Items.WHITE_BANNER)
                .requires(Items.HAY_BLOCK)
                .unlockedBy("has_hay", has(Items.HAY_BLOCK))
                .save(recipeOutput, id("haven_block"));
    }

    private void buildCommandItems(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.ATTACK_PLAN.get())
                .requires(Items.PAPER)
                .requires(Items.INK_SAC)
                .requires(Items.FEATHER)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(recipeOutput, id("attack_plan"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, VillagersMod.WAR_HORN.get())
                .pattern(" C ")
                .pattern("C C")
                .pattern(" L ")
                .define('C', Items.COPPER_INGOT)
                .define('L', Items.LEATHER)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(recipeOutput, id("war_horn"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, VillagersMod.AWAKEN_HORN.get())
                .requires(VillagersMod.WAR_HORN.get())
                .requires(Items.YELLOW_DYE)
                .unlockedBy("has_war_horn", has(VillagersMod.WAR_HORN.get()))
                .save(recipeOutput, id("awaken_horn"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, VillagersMod.FALLBACK_HORN.get())
                .requires(VillagersMod.WAR_HORN.get())
                .requires(Items.WHITE_DYE)
                .unlockedBy("has_war_horn", has(VillagersMod.WAR_HORN.get()))
                .save(recipeOutput, id("fallback_horn"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, VillagersMod.CHARGE_HORN.get())
                .requires(VillagersMod.WAR_HORN.get())
                .requires(Items.RED_DYE)
                .unlockedBy("has_war_horn", has(VillagersMod.WAR_HORN.get()))
                .save(recipeOutput, id("charge_horn"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, VillagersMod.GUARD_CHIEF_HORN.get())
                .requires(VillagersMod.WAR_HORN.get())
                .requires(Items.GOLD_INGOT)
                .unlockedBy("has_war_horn", has(VillagersMod.WAR_HORN.get()))
                .save(recipeOutput, id("guard_chief_horn"));

        for (DyeColor color : LeverHornService.HORN_COLORS) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, LeverHornItem.withColor(color, false))
                    .requires(VillagersMod.WAR_HORN.get())
                    .requires(Ingredient.of(net.minecraft.world.item.DyeItem.byColor(color)))
                    .unlockedBy("has_war_horn", has(VillagersMod.WAR_HORN.get()))
                    .save(recipeOutput, id("lever_horn_" + color.getName()));
        }

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, LeverHornItem.withColor(DyeColor.WHITE, true))
                .requires(VillagersMod.LEVER_HORN.get())
                .requires(Items.IRON_INGOT)
                .unlockedBy("has_lever_horn", has(VillagersMod.LEVER_HORN.get()))
                .save(recipeOutput, id("lever_horn_random"));

        for (DyeColor color : LeverHornService.HORN_COLORS) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, SoldierLeverItem.withColor(color))
                    .requires(Items.LEVER)
                    .requires(Ingredient.of(net.minecraft.world.item.DyeItem.byColor(color)))
                    .unlockedBy("has_lever", has(Items.LEVER))
                    .save(recipeOutput, id("soldier_lever_" + color.getName()));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, SoldierButtonItem.withColor(color))
                    .requires(ItemTags.BUTTONS)
                    .requires(Ingredient.of(net.minecraft.world.item.DyeItem.byColor(color)))
                    .unlockedBy("has_button", has(ItemTags.BUTTONS))
                    .save(recipeOutput, id("soldier_button_" + color.getName()));
        }

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.KINGDOM_MAP.get())
                .requires(Items.MAP)
                .requires(Items.GOLD_INGOT)
                .unlockedBy("has_map", has(Items.MAP))
                .save(recipeOutput, id("kingdom_map"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, SignalRocketItem.create(false, 1))
                .requires(Items.FIREWORK_ROCKET)
                .requires(Items.WHITE_DYE)
                .unlockedBy("has_firework", has(Items.FIREWORK_ROCKET))
                .save(recipeOutput, id("rally_signal_rocket"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, SignalRocketItem.create(true, 1))
                .requires(Items.FIREWORK_ROCKET)
                .requires(Items.RED_DYE)
                .unlockedBy("has_firework", has(Items.FIREWORK_ROCKET))
                .save(recipeOutput, id("strike_signal_rocket"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, SignalRocketItem.create(false, 3))
                .requires(Items.FIREWORK_ROCKET)
                .requires(Items.GUNPOWDER)
                .requires(Items.GUNPOWDER)
                .requires(Items.WHITE_DYE)
                .unlockedBy("has_firework", has(Items.FIREWORK_ROCKET))
                .save(recipeOutput, id("rally_signal_rocket_3"));
    }

    private void buildAnimalJobBlocks(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.FEED_TROUGH.get())
                .requires(ItemTags.WOODEN_SLABS)
                .requires(ItemTags.WOODEN_SLABS)
                .requires(ItemTags.WOODEN_SLABS)
                .requires(Items.HAY_BLOCK)
                .requires(Items.EMERALD_BLOCK)
                .unlockedBy("has_hay", has(Items.HAY_BLOCK))
                .save(recipeOutput, id("feed_trough"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.SHEARING_POST.get())
                .requires(Items.OAK_FENCE)
                .requires(Items.SHEARS)
                .requires(Items.EMERALD_BLOCK)
                .unlockedBy("has_shears", has(Items.SHEARS))
                .save(recipeOutput, id("shearing_post"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.NESTING_BOX.get())
                .requires(Items.BARREL)
                .requires(Items.HAY_BLOCK)
                .requires(Items.EMERALD_BLOCK)
                .unlockedBy("has_barrel", has(Items.BARREL))
                .save(recipeOutput, id("nesting_box"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.SLOP_BUCKET.get())
                .requires(Items.BUCKET)
                .requires(Items.CARROT)
                .requires(Items.EMERALD_BLOCK)
                .unlockedBy("has_bucket", has(Items.BUCKET))
                .save(recipeOutput, id("slop_bucket"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.HITCHING_POST.get())
                .requires(Items.OAK_FENCE)
                .requires(Items.LEAD)
                .requires(Items.GOLD_BLOCK)
                .unlockedBy("has_lead", has(Items.LEAD))
                .save(recipeOutput, id("hitching_post"));
    }

    private void buildNecromancerItems(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, VillagersMod.HORCRUX_COMPASS.get())
                .requires(Items.COMPASS)
                .requires(VillagersMod.PHYLACTERY_SHARD.get())
                .requires(Items.SOUL_SAND)
                .requires(Items.WITHER_ROSE)
                .unlockedBy("has_compass", has(Items.COMPASS))
                .save(recipeOutput, id("horcrux_compass"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.FLUTE_CHARGE.get())
                .requires(Items.GLOWSTONE_DUST)
                .requires(Items.BLAZE_POWDER)
                .requires(Items.ENDER_PEARL)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput, id("flute_charge"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.QUEST_FLUTE.get())
                .requires(VillagersMod.QUEST_FLUTE.get())
                .requires(VillagersMod.FLUTE_CHARGE.get())
                .unlockedBy("has_quest_flute", has(VillagersMod.QUEST_FLUTE.get()))
                .save(recipeOutput, id("quest_flute_recharge"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.KINGSTREE_CURE.get())
                .pattern("DND")
                .pattern("EHE")
                .pattern("EEE")
                .define('D', Items.DRAGON_BREATH)
                .define('N', Items.NETHER_STAR)
                .define('E', Items.ECHO_SHARD)
                .define('H', Items.HEART_OF_THE_SEA)
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(recipeOutput, id("kingstree_cure"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.END_ANCHOR.get())
                .pattern("PPP")
                .pattern("PBP")
                .pattern("CCC")
                .define('P', Items.ENDER_PEARL)
                .define('B', Items.BEACON)
                .define('C', Items.END_CRYSTAL)
                .unlockedBy("has_beacon", has(Items.BEACON))
                .save(recipeOutput, id("end_anchor"));
    }

    private void buildNecromancerGear(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, VillagersMod.NECROMANCER_HOOD.get())
                .pattern("WMW")
                .pattern("WBW")
                .define('W', Items.BLACK_WOOL)
                .define('M', Items.PHANTOM_MEMBRANE)
                .define('B', Items.BONE)
                .unlockedBy("has_phantom_membrane", has(Items.PHANTOM_MEMBRANE))
                .save(recipeOutput, id("necromancer_hood"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, VillagersMod.NECROMANCER_ROBE.get())
                .pattern("W W")
                .pattern("MSM")
                .pattern("WWW")
                .define('W', Items.BLACK_WOOL)
                .define('M', Items.PHANTOM_MEMBRANE)
                .define('S', Items.SOUL_SAND)
                .unlockedBy("has_soul_sand", has(Items.SOUL_SAND))
                .save(recipeOutput, id("necromancer_robe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, VillagersMod.NECROMANCER_WAND.get())
                .pattern("  E")
                .pattern(" W ")
                .pattern("B  ")
                .define('E', Items.ECHO_SHARD)
                .define('W', Items.WITHER_SKELETON_SKULL)
                .define('B', Items.BLAZE_ROD)
                .unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD))
                .save(recipeOutput, id("necromancer_wand"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.GRAVE_SHROUD.get())
                .pattern("WWW")
                .pattern("WSW")
                .define('W', Items.BLACK_WOOL)
                .define('S', Items.SOUL_SAND)
                .unlockedBy("has_soul_sand", has(Items.SOUL_SAND))
                .save(recipeOutput, id("grave_shroud"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.GRAVE_MARKER.get())
                .pattern(" L ")
                .pattern(" B ")
                .pattern("CCC")
                .define('L', Items.SOUL_LANTERN)
                .define('B', Items.BONE_BLOCK)
                .define('C', Items.COBBLESTONE)
                .unlockedBy("has_soul_lantern", has(Items.SOUL_LANTERN))
                .save(recipeOutput, id("grave_marker"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.CURSED_EFFIGY.get())
                .pattern(" P ")
                .pattern("BHB")
                .pattern(" S ")
                .define('P', Items.CARVED_PUMPKIN)
                .define('B', Items.BONE)
                .define('H', Items.HAY_BLOCK)
                .define('S', Items.STICK)
                .unlockedBy("has_pumpkin", has(Items.CARVED_PUMPKIN))
                .save(recipeOutput, id("cursed_effigy"));
    }

    private void buildWarAndUtility(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, VillagersMod.CALTROPS.get(), 4)
                .requires(Items.IRON_NUGGET)
                .requires(Items.IRON_NUGGET)
                .requires(Items.IRON_NUGGET)
                .requires(Items.IRON_NUGGET)
                .requires(Items.FLINT)
                .unlockedBy("has_flint", has(Items.FLINT))
                .save(recipeOutput, id("caltrops"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.MUSTER_ROLL.get())
                .requires(Items.BOOK)
                .requires(Items.IRON_INGOT)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(recipeOutput, id("muster_roll"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.REASSIGNMENT_WRIT.get())
                .requires(Items.PAPER)
                .requires(Items.INK_SAC)
                .requires(Items.IRON_INGOT)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(recipeOutput, id("reassignment_writ"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.ROYAL_GUARD_POST.get())
                .requires(VillagersMod.RAMPART.get())
                .requires(Items.GOLD_INGOT)
                .unlockedBy("has_rampart", has(VillagersMod.RAMPART.get()))
                .save(recipeOutput, id("royal_guard_post"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VillagersMod.WANTED_POSTER.get())
                .requires(Items.PAPER)
                .requires(Items.INK_SAC)
                .requires(Items.IRON_NUGGET)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(recipeOutput, id("wanted_poster"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.TRAINING_DUMMY.get())
                .pattern(" A ")
                .pattern(" H ")
                .pattern(" S ")
                .define('A', Items.ARMOR_STAND)
                .define('H', Items.HAY_BLOCK)
                .define('S', Items.STICK)
                .unlockedBy("has_armor_stand", has(Items.ARMOR_STAND))
                .save(recipeOutput, id("training_dummy"));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, path);
    }
}
