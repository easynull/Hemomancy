package com.easynull.hemomancy.utils;

import com.easynull.hemomancy.registers.recipes.AlchemyRecipe;
import com.easynull.hemomancy.registers.recipes.AltarRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.Collection;

public final class RecipeUtils {
    public static AltarRecipe getAltarRecipe(AltarRecipe.Input input, Level level) {
        if (!(level instanceof ServerLevel sl)) return null;
        Collection<RecipeHolder<?>> recipes = sl.recipeAccess().getRecipes();
        for (RecipeHolder<?> holder : recipes) {
            Recipe<?> recipe = holder.value();
            if (recipe instanceof AltarRecipe altar) {
                if (altar.matches(input, level)) {
                    return altar;
                }
            }
        }
        return null;
    }

    public static AlchemyRecipe getAlchemyRecipe(AlchemyRecipe.Input input, Level level) {
        if (!(level instanceof ServerLevel sl)) return null;
        Collection<RecipeHolder<?>> recipes = sl.recipeAccess().getRecipes();
        for (RecipeHolder<?> holder : recipes) {
            Recipe<?> recipe = holder.value();
            if (recipe instanceof AlchemyRecipe altar) {
                if (altar.matches(input, level)) {
                    return altar;
                }
            }
        }
        return null;
    }
}
