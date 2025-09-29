package com.easynull.hemomancy.registers.recipes;

import com.easynull.hemomancy.registers.HcRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mw.nullcore.core.blocks.type.ContainerHave;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public record AlchemyRecipe(ItemStack result, List<Ingredient> inputs, long lp) implements Recipe<AlchemyRecipe.Input> {
    @Override
    public boolean matches(AlchemyRecipe.Input input, Level level) {
        List<Ingredient> remainingIngredients = new ArrayList<>(inputs);
        List<ItemStack> availableItems = new ArrayList<>();

        for (int i = 1; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if(i == 1 && stack.getCount() < stack.getMaxStackSize() && ItemStack.isSameItemSameComponents(stack, result())) continue;
            if (!stack.isEmpty()) {
                availableItems.add(stack);
            }
        }
        if (availableItems.size() != inputs.size()) return false;
        for (ItemStack item : availableItems) {
            boolean foundMatch = false;
            Iterator<Ingredient> iterator = remainingIngredients.iterator();
            while (iterator.hasNext()) {
                Ingredient ingredient = iterator.next();
                if (ingredient.test(item)) {
                    iterator.remove();
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) {
                return false;
            }
        }

        return remainingIngredients.isEmpty();
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
                        Ingredient.CODEC.listOf(2, 10).fieldOf("inputs").forGetter(AlchemyRecipe::inputs),
                        Codec.LONG.fieldOf("lp").forGetter(AlchemyRecipe::lp)
                ).apply(builder, AlchemyRecipe::new)
        );
        static final StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> streamCodec = StreamCodec.composite(
                ItemStack.STREAM_CODEC, AlchemyRecipe::result,
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), AlchemyRecipe::inputs,
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

    public record Input(ContainerHave inv) implements RecipeInput {
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