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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.HmRecipes;

import java.util.ArrayList;
import java.util.List;

public record AlchemyRecipe(ItemStack result, List<Ingredient> inputs, long lp) implements Recipe<AlchemyRecipe.Input> {
    public static final ResourceLocation ID = Hemomancy.path("alchemy");

    @Override
    public boolean matches(AlchemyRecipe.Input inventory, Level level) {
        List<ItemStack> available = new ArrayList<>();
        for (int i = 2; i < inventory.size(); i++) {
            ItemStack stack = inventory.getItem(i);
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
        ItemStack slot1 = inventory.getItem(1);
        return slot1.isEmpty() || (slot1.is(result().getItem()) && slot1.getCount() < slot1.getMaxStackSize());
    }

    @Override
    public ItemStack assemble(AlchemyRecipe.Input inventory, HolderLookup.Provider registries) {
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
        return HmRecipes.ALCHEMY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return HmRecipes.ALCHEMY;
    }

    public static class Serializer implements RecipeSerializer<AlchemyRecipe> {
        public static final MapCodec<AlchemyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ItemStack.CODEC.fieldOf("result").forGetter(AlchemyRecipe::result),
                        Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(AlchemyRecipe::inputs),
                        Codec.LONG.fieldOf("lp").forGetter(AlchemyRecipe::lp)
                ).apply(instance, AlchemyRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, AlchemyRecipe::result,
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), AlchemyRecipe::inputs,
                ByteBufCodecs.VAR_LONG, AlchemyRecipe::lp,
                AlchemyRecipe::new
        );

        @Override
        public MapCodec<AlchemyRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public record Input(List<ItemStack> stacks) implements RecipeInput{

        @Override
        public ItemStack getItem(int i) {
            return stacks.get(i);
        }

        @Override
        public int size() {
            return stacks.size();
        }
    }

    public static final class Builder implements RecipeBuilder {
        private final ItemStack result;
        private final List<Ingredient> inputs = new ArrayList<>();
        private final long lp;

        private Builder(ItemStack result, long lp) {
            this.result = result;
            this.lp = lp;
        }

        public static Builder alchemy(ItemStack result, long lp) {
            return new Builder(result, lp);
        }

        public Builder requires(Ingredient ingredient) {
            this.inputs.add(ingredient);
            return this;
        }

        public Builder requires(ItemLike item) {
            return this.requires(Ingredient.of(item));
        }

        @Override
        public Builder unlockedBy(String name, Criterion<?> criterion) {
            return this;
        }

        @Override
        public Builder group(String group) {
            return this;
        }

        @Override
        public Item getResult() {
            return this.result.getItem();
        }

        @Override
        public void save(RecipeOutput recipeOutput, ResourceLocation id) {
            recipeOutput.accept(id, new AlchemyRecipe(this.result, this.inputs, this.lp), null);
        }
    }
}