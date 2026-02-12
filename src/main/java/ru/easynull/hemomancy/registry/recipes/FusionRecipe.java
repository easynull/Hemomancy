package ru.easynull.hemomancy.registry.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.HmRecipes;

public record FusionRecipe(Identifier id, ItemStack result, Ingredient input, long lp, byte tier) implements Recipe<Inventory> {
    public static final Identifier ID = Hemomancy.path("fusion");

    @Override
    public boolean matches(Inventory inventory, World world) {
        ItemStack input = inventory.getStack(0);
        if (input.isEmpty()) {
            return false;
        }
        return input().test(input);
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
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
        return id();
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
        public static final Identifier ID = Hemomancy.path("bloody_fusion");

        @Override
        public FusionRecipe read(Identifier id, JsonObject json) {
            ItemStack result = ShapedRecipe.outputFromJson(json.getAsJsonObject("result"));

            Ingredient input = Ingredient.fromJson(json.get("ingredient"));

            long lp = json.get("lp").getAsLong();
            byte tier = json.get("tier").getAsByte();

            return new FusionRecipe(id, result, input, lp, tier);
        }

        @Override
        public FusionRecipe read(Identifier id, PacketByteBuf buf) {
            ItemStack result = buf.readItemStack();
            Ingredient input = Ingredient.fromPacket(buf);
            long lp = buf.readLong();
            byte tier = buf.readByte();

            return new FusionRecipe(id, result, input, lp, tier);
        }

        @Override
        public void write(PacketByteBuf buf, FusionRecipe recipe) {
            buf.writeItemStack(recipe.result());
            recipe.input().write(buf);
            buf.writeLong(recipe.lp());
            buf.writeByte(recipe.tier());
        }
    }
}
