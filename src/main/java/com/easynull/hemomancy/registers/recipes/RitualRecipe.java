//package com.easynull.hemomancy.registers.recipes;
//
//import com.easynull.hemomancy.registers.HcRecipes;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import com.mw.nullcore.core.blocks.type.ContainerHave;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.*;
//import net.minecraft.world.level.Level;
//
//public record RitualRecipe(ItemStack result, Ingredient input, long lp) implements Recipe<RitualRecipe.Input> {
//    @Override
//    public boolean matches(RitualRecipe.Input input, Level level) {
//        return this.input.test(input.inv().getInventory().getItem(0));
//    }
//
//    @Override
//    public ItemStack assemble(RitualRecipe.Input recipe, HolderLookup.Provider provider) {
//        return result().copy();
//    }
//
//    @Override
//    public RecipeType<RitualRecipe> getType() {
//        return HcRecipes.ritual.get();
//    }
//
//    @Override
//    public RecipeSerializer<RitualRecipe> getSerializer() {
//        return HcRecipes.ritualSer.get();
//    }
//
//    @Override
//    public PlacementInfo placementInfo() {
//        return PlacementInfo.NOT_PLACEABLE;
//    }
//
//    @Override
//    public RecipeBookCategory recipeBookCategory() {
//        return RecipeBookCategories.CRAFTING_MISC;
//    }
//
//    public static class Serializer implements RecipeSerializer<RitualRecipe> {
//        static final MapCodec<RitualRecipe> codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
//                        ItemStack.CODEC.fieldOf("result").forGetter(RitualRecipe::result),
//                        Ingredient.CODEC.fieldOf("input").forGetter(RitualRecipe::input),
//                        Codec.LONG.fieldOf("lp").forGetter(RitualRecipe::lp)
//                ).apply(builder, RitualRecipe::new)
//        );
//        static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> streamCodec = StreamCodec.composite(
//                ItemStack.STREAM_CODEC, RitualRecipe::result,
//                Ingredient.CONTENTS_STREAM_CODEC, RitualRecipe::input,
//                ByteBufCodecs.LONG, RitualRecipe::lp,
//                RitualRecipe::new);
//
//        @Override
//        public MapCodec<RitualRecipe> codec() {
//            return codec;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> streamCodec() {
//            return streamCodec;
//        }
//    }
//
//    public record Input(ContainerHave inv, byte tier) implements RecipeInput {
//        @Override
//        public ItemStack getItem(int i) {
//            return inv.getInventory().getItem(i);
//        }
//
//        @Override
//        public int size() {
//            return inv.getInventory().getContainerSize();
//        }
//    }
//}