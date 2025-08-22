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

public record AltarRecipe(Ingredient input, ItemStack output, long lp, byte tier) implements Recipe<AltarRecipe.AltarInput> {
    @Override
    public boolean matches(AltarInput input, Level level) {
        return this.input.test(input.inv().getInventory().getItem(0));
    }

    @Override
    public ItemStack assemble(AltarRecipe.AltarInput recipe, HolderLookup.Provider provider) {
        return output().copy();
    }

    @Override
    public RecipeSerializer<AltarRecipe> getSerializer() {
        return HcRecipes.altarSer.get();
    }

    @Override
    public RecipeType<AltarRecipe> getType() {
        return HcRecipes.altar.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<AltarRecipe> {
        final MapCodec<AltarRecipe> codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                Ingredient.CODEC.fieldOf("input").forGetter(AltarRecipe::input),
                ItemStack.CODEC.fieldOf("output").forGetter(AltarRecipe::output),
                Codec.LONG.fieldOf("lp").forGetter(AltarRecipe::lp),
                Codec.BYTE.fieldOf("tier").forGetter(AltarRecipe::tier)
                ).apply(builder, AltarRecipe::new)
        );
        final StreamCodec<RegistryFriendlyByteBuf, AltarRecipe> streamCodec = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, AltarRecipe::input,
                ItemStack.STREAM_CODEC, AltarRecipe::output,
                ByteBufCodecs.LONG, AltarRecipe::lp,
                ByteBufCodecs.BYTE, AltarRecipe::tier,
                AltarRecipe::new);

        @Override
        public MapCodec<AltarRecipe> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AltarRecipe> streamCodec() {
            return streamCodec;
        }
    }

    public record AltarInput(ContainerHave inv, byte tier) implements RecipeInput{
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
