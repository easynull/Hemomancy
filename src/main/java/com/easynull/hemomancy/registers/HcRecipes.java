package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.registers.recipes.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HcRecipes {
    static final DeferredRegister<RecipeType<?>> types = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Hemomancy.ID);
    static final DeferredRegister<RecipeSerializer<?>> serializer = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Hemomancy.ID);

    public static DeferredHolder<RecipeType<?>, RecipeType<AltarRecipe>> altar = types.register("altar", ()-> RecipeType.simple(Hemomancy.toPath("altar")));
    public static DeferredHolder<RecipeSerializer<?>, AltarRecipe.Serializer> altarSer = serializer.register("altar", AltarRecipe.Serializer::new);

    public static DeferredHolder<RecipeType<?>, RecipeType<AlchemyRecipe>> alchemy = types.register("alchemy", ()-> RecipeType.simple(Hemomancy.toPath("alchemy")));
    public static DeferredHolder<RecipeSerializer<?>, AlchemyRecipe.Serializer> alchemySer = serializer.register("alchemy", AlchemyRecipe.Serializer::new);

//    public static DeferredHolder<RecipeType<?>, RecipeType<RitualRecipe>> ritual = types.register("ritual", ()-> RecipeType.simple(Hemomancy.toPath("ritual")));
//    public static DeferredHolder<RecipeSerializer<?>, RitualRecipe.Serializer> ritualSer = serializer.register("ritual", RitualRecipe.Serializer::new);

    public static void init(IEventBus bus){
        serializer.register(bus);
        types.register(bus);
    }
}
