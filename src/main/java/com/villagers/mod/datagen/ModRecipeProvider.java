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
                .pattern("B")
                .pattern("B")
                .pattern("B")
                .pattern("B")
                .pattern("B")
                .pattern("B")
                .pattern("B")
                .define('B', Items.BARREL)
                .unlockedBy("has_barrel", has(Items.BARREL))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "mess_station"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.POST_BED.get())
                .pattern("W")
                .pattern("W")
                .pattern("W")
                .pattern("W")
                .pattern("W")
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
    }
}
