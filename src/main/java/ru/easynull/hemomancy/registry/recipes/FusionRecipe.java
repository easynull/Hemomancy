package ru.easynull.hemomancy.registry.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.HmRecipes;

public record FusionRecipe(ItemStack result, Ingredient input, long lp, byte tier) implements Recipe<SingleRecipeInput> {
    public static final ResourceLocation ID = Hemomancy.path("fusion");

    @Override
    public boolean matches(SingleRecipeInput inventory, Level level) {
        ItemStack input = inventory.getItem(0);
        if (input.isEmpty()) {
            return false;
        }
        return input().test(input);
    }

    @Override
    public ItemStack assemble(SingleRecipeInput inventory, HolderLookup.Provider registries) {
        return result().copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HmRecipes.FUSION_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return HmRecipes.FUSION;
    }

    public static class Serializer implements RecipeSerializer<FusionRecipe> {
        public static final MapCodec<FusionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ItemStack.CODEC.fieldOf("result").forGetter(FusionRecipe::result),
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(FusionRecipe::input),
                        Codec.LONG.fieldOf("lp").forGetter(FusionRecipe::lp),
                        Codec.BYTE.fieldOf("tier").forGetter(FusionRecipe::tier)
                ).apply(instance, FusionRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, FusionRecipe> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, FusionRecipe::result,
                Ingredient.CONTENTS_STREAM_CODEC, FusionRecipe::input,
                ByteBufCodecs.VAR_LONG, FusionRecipe::lp,
                ByteBufCodecs.BYTE, FusionRecipe::tier,
                FusionRecipe::new
        );

        @Override
        public MapCodec<FusionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FusionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static final class Builder implements RecipeBuilder {
        private final ItemStack result;
        private final Ingredient input;
        private final long lp;
        private final byte tier;

        private Builder(ItemStack result, Ingredient input, long lp, byte tier) {
            this.result = result;
            this.input = input;
            this.lp = lp;
            this.tier = tier;
        }

        public static Builder fusion(ItemStack result, Ingredient input, long lp, int tier) {
            return new Builder(result, input, lp, (byte) tier);
        }

        @Override
        public Builder unlockedBy(String name, Criterion<?> criterion) {
            return this;
        }

        @Override
        public Builder group(@Nullable String group) {
            return this;
        }

        @Override
        public Item getResult() {
            return this.result.getItem();
        }

        @Override
        public void save(RecipeOutput recipeOutput, ResourceLocation id) {
            recipeOutput.accept(id, new FusionRecipe(this.result, this.input, this.lp, this.tier), null);
        }
    }
}