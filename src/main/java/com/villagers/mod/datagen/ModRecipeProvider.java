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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.POST_BLOCK.get())
                .pattern("S")
                .pattern("I")
                .define('S', Items.COBBLESTONE_SLAB)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "post_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.VILLAGE_MARKER.get())
                .pattern("SAS")
                .pattern("ACA")
                .pattern("SAS")
                .define('S', Items.STONE)
                .define('A', Items.AMETHYST_BLOCK)
                .define('C', Items.LAPIS_BLOCK)
                .unlockedBy("has_lapis", has(Items.LAPIS_BLOCK))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "village_marker"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.RAMPART.get())
                .pattern("SIS")
                .pattern("S S")
                .define('S', Items.STONE_BRICKS)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_stone_bricks", has(Items.STONE_BRICKS))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "rampart"));

    }
}
