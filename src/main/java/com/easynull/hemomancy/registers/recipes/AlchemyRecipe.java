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
    public boolean matches(Input input, Level level) {
        NonNullList<ItemStack> available = NonNullList.create();
        for (int i = 2; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) available.add(stack.copy());
        }
        for (Ingredient ing : inputs) {
            boolean found = false;
            for (int j = 0; j < available.size(); j++) {
                ItemStack stack = available.get(j);
                if (!stack.isEmpty() && ing.test(stack)) {
                    stack.shrink(1);
                    if (stack.isEmpty()) available.set(j, ItemStack.EMPTY);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        ItemStack slot1 = input.getItem(1);
        return slot1.isEmpty() || (ItemStack.isSameItemSameComponents(slot1, result()) && slot1.getCount() < slot1.getMaxStackSize());
    }

    @Override
    public ItemStack assemble(Input recipe, HolderLookup.Provider provider) {
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
        ).apply(builder, AlchemyRecipe::new));
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