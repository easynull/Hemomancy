package ru.easynull.hemomancy.registry;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.easynull.hemomancy.registry.recipes.AlchemyRecipe;
import ru.easynull.hemomancy.registry.recipes.FusionRecipe;

public final class HmRecipes {

    public static final RecipeType<FusionRecipe> FUSION = registerRecipeType(FusionRecipe.ID);
    public static final RecipeSerializer<FusionRecipe> FUSION_SERIALIZER = registerRecipeSerializer(FusionRecipe.Serializer.ID, new FusionRecipe.Serializer());

    public static final RecipeType<AlchemyRecipe> ALCHEMY = registerRecipeType(AlchemyRecipe.ID);
    public static final RecipeSerializer<AlchemyRecipe> ALCHEMY_SERIALIZER = registerRecipeSerializer(AlchemyRecipe.Serializer.ID, new AlchemyRecipe.Serializer());

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

    public static void onInit(){}
}
