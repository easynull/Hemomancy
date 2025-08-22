package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.registers.recipes.AltarRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HcRecipes {
    static final DeferredRegister<RecipeType<?>> types = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Hemomancy.ID);
    static final DeferredRegister<RecipeSerializer<?>> serializer = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Hemomancy.ID);

    public static DeferredHolder<RecipeType<?>, RecipeType<AltarRecipe>> altar = types.register("altar", ()-> RecipeType.simple(Hemomancy.path("altar")));
    public static DeferredHolder<RecipeSerializer<?>, AltarRecipe.Serializer> altarSer = serializer.register("altar", AltarRecipe.Serializer::new);

    public static void init(IEventBus bus){
        serializer.register(bus);
        types.register(bus);
    }
}
