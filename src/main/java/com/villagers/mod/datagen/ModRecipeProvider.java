package com.villagers.mod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import com.villagers.mod.VillagersMod;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.MESS_STATION.get())
                .pattern("B B")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', Items.BARREL)
                .unlockedBy("has_barrel", has(Items.BARREL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.POST_BED.get())
                .pattern("W W")
                .pattern("WWW")
                .define('W', Items.WHITE_BED)
                .unlockedBy("has_bed", has(Items.WHITE_BED))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VillagersMod.POST_BLOCK.get())
                .pattern("S")
                .pattern("I")
                .define('S', Items.COBBLESTONE_SLAB)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);
    }
}
