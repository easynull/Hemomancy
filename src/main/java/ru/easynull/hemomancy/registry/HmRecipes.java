package ru.easynull.hemomancy.registry;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.easynull.hemomancy.registry.recipes.AlchemyRecipe;
import ru.easynull.hemomancy.registry.recipes.AltarRecipe;

public final class HmRecipes {

    public static final RecipeType<AltarRecipe> ALTAR = registerRecipeType(AltarRecipe.ID);
    public static final RecipeSerializer<AltarRecipe> ALTAR_SERIALIZER = registerRecipeSerializer(AltarRecipe.ID, new AltarRecipe.Serializer());

    public static final RecipeType<AlchemyRecipe> ALCHEMY = registerRecipeType(AlchemyRecipe.ID);
    public static final RecipeSerializer<AlchemyRecipe> ALCHEMY_SERIALIZER = registerRecipeSerializer(AlchemyRecipe.ID, new AlchemyRecipe.Serializer());

    private static <T extends Recipe<?>> RecipeType<T> registerRecipeType(Identifier id) {
        return Registry.register(Registries.RECIPE_TYPE, id, new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
    }

    private static <T extends Recipe<?>> RecipeSerializer<T> registerRecipeSerializer(Identifier id, RecipeSerializer<T> serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, id, serializer);
    }
}
