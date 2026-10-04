package com.villagers.mod.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import com.villagers.mod.VillagersMod;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.MESS_STATION.get())
                .pattern("B B")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', Items.BARREL)
                .unlockedBy("has_barrel", has(Items.BARREL))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "mess_station"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.POST_BED.get())
                .pattern("W W")
                .pattern("WWW")
                .define('W', Items.WHITE_BED)
                .unlockedBy("has_bed", has(Items.WHITE_BED))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "post_bed"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.POST_BLOCK.get())
                .pattern("S")
                .pattern("I")
                .define('S', Items.COBBLESTONE_SLAB)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "post_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.BARRACKS.get())
                .pattern("PPP")
                .pattern("PSP")
                .pattern("PPP")
                .define('P', Items.OAK_PLANKS)
                .define('S', Items.CRAFTING_TABLE)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "barracks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.WATCHTOWER.get())
                .pattern("B B")
                .pattern("B B")
                .pattern("BBB")
                .define('B', Items.BRICK)
                .unlockedBy("has_brick", has(Items.BRICK))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "watchtower"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.ARMORY.get())
                .pattern("III")
                .pattern("ICI")
                .pattern("III")
                .define('I', Items.IRON_BLOCK)
                .define('C', Items.CHEST)
                .unlockedBy("has_iron_block", has(Items.IRON_BLOCK))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "armory"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.VILLAGE_GATE.get())
                .pattern("DFD")
                .pattern("DFD")
                .pattern("DFD")
                .define('D', Items.OAK_DOOR)
                .define('F', Items.IRON_BARS)
                .unlockedBy("has_iron_bars", has(Items.IRON_BARS))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "village_gate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.VILLAGE_MARKER.get())
                .pattern("SAS")
                .pattern("ACA")
                .pattern("SAS")
                .define('S', Items.STONE)
                .define('A', Items.AMETHYST_BLOCK)
                .define('C', Items.LAPIS_BLOCK)
                .unlockedBy("has_lapis", has(Items.LAPIS_BLOCK))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "village_marker"));
    }
}
