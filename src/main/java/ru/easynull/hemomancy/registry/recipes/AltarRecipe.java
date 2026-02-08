package ru.easynull.hemomancy.registry.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.HmRecipes;

public record AltarRecipe(ItemStack result, Ingredient input, long lp, byte tier) implements Recipe<AltarRecipe.TirableInventory> {
    public static final Identifier ID = Hemomancy.path("altar");

    @Override
    public boolean matches(TirableInventory inventory, World world) {
        return this.input.test(inventory.getStack(0)) && inventory.tier >= tier();
    }

    @Override
    public ItemStack craft(TirableInventory inventory, DynamicRegistryManager registryManager) {
        return result().copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return result().copy();
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HmRecipes.ALTAR_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return HmRecipes.ALTAR;
    }

    public static class Serializer implements RecipeSerializer<AltarRecipe> {
        @Override
        public AltarRecipe read(Identifier id, JsonObject json) {
            return null;
        }

        @Override
        public AltarRecipe read(Identifier id, PacketByteBuf buf) {
            return null;
        }

        @Override
        public void write(PacketByteBuf buf, AltarRecipe recipe) {

        }
    }

    public static final class TirableInventory extends SimpleInventory {
        public final byte tier;

        public TirableInventory(int size, byte tier) {
            super(size);
            this.tier = tier;
        }
    }
}
