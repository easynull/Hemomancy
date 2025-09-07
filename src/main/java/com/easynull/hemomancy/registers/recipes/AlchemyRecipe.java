package com.easynull.hemomancy.registers.recipes;

import com.easynull.hemomancy.registers.HcRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mw.nullcore.core.blocks.type.ContainerHave;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record AlchemyRecipe(ItemStack result, Ingredient input, long lp) implements Recipe<AlchemyRecipe.Input> {
    @Override
    public boolean matches(AlchemyRecipe.Input input, Level level) {
        return this.input.test(input.inv().getInventory().getItem(0));
    }

    @Override
    public ItemStack assemble(AlchemyRecipe.Input recipe, HolderLookup.Provider provider) {
        return result().copy();
    }

    @Override
    public RecipeType<AlchemyRecipe> getType() {
        return HcRecipes.alchemy.get();
    }

    @Override
    public RecipeSerializer<AlchemyRecipe> getSerializer() {
        return HcRecipes.alchemySer.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class Serializer implements RecipeSerializer<AlchemyRecipe> {
        static final MapCodec<AlchemyRecipe> codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                        ItemStack.CODEC.fieldOf("result").forGetter(AlchemyRecipe::result),
                        Ingredient.CODEC.fieldOf("input").forGetter(AlchemyRecipe::input),
                        Codec.LONG.fieldOf("lp").forGetter(AlchemyRecipe::lp)
                ).apply(builder, AlchemyRecipe::new)
        );
        static final StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> streamCodec = StreamCodec.composite(
                ItemStack.STREAM_CODEC, AlchemyRecipe::result,
                Ingredient.CONTENTS_STREAM_CODEC, AlchemyRecipe::input,
                ByteBufCodecs.LONG, AlchemyRecipe::lp,
                AlchemyRecipe::new);

        @Override
        public MapCodec<AlchemyRecipe> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> streamCodec() {
            return streamCodec;
        }
    }

    public record Input(ContainerHave inv, byte tier) implements RecipeInput {
        @Override
        public ItemStack getItem(int i) {
            return inv.getInventory().getItem(i);
        }

        @Override
        public int size() {
            return inv.getInventory().getContainerSize();
        }
    }
}