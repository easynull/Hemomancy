package ru.easynull.hemomancy.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import ru.easynull.hemomancy.registry.recipes.AlchemyRecipe;
import ru.easynull.hemomancy.registry.recipes.FusionRecipe;

public final class HmRecipes {

    public static final RecipeType<FusionRecipe> FUSION = registerRecipeType(FusionRecipe.ID);
    public static final RecipeSerializer<FusionRecipe> FUSION_SERIALIZER = registerRecipeSerializer(FusionRecipe.ID, new FusionRecipe.Serializer());

    public static final RecipeType<AlchemyRecipe> ALCHEMY = registerRecipeType(AlchemyRecipe.ID);
    public static final RecipeSerializer<AlchemyRecipe> ALCHEMY_SERIALIZER = registerRecipeSerializer(AlchemyRecipe.ID, new AlchemyRecipe.Serializer());

    private static <T extends Recipe<?>> RecipeType<T> registerRecipeType(ResourceLocation id) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, id, new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
    }

    private static <T extends Recipe<?>> RecipeSerializer<T> registerRecipeSerializer(ResourceLocation id, RecipeSerializer<T> serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer);
    }

    public static void onInit(){}
}
