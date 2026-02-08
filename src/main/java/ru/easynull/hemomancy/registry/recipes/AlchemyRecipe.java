package ru.easynull.hemomancy.registry.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
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

import java.util.ArrayList;
import java.util.List;

public record AlchemyRecipe(ItemStack result, List<Ingredient> inputs, long lp) implements Recipe<Inventory> {
    public static final Identifier ID = Hemomancy.path("alchemy");

    @Override
    public boolean matches(Inventory inventory, World world) {
        List<ItemStack> available = new ArrayList<>();
        for (int i = 2; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) available.add(stack.copy());
        }
        for (Ingredient ing : inputs) {
            boolean found = false;
            for (int j = 0; j < available.size(); j++) {
                ItemStack stack = available.get(j);
                if (!stack.isEmpty() && ing.test(stack)) {
                    stack.decrement(1);
                    if (stack.isEmpty()) available.set(j, ItemStack.EMPTY);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        ItemStack slot1 = inventory.getStack(1);
        return slot1.isEmpty() || (ItemStack.areItemsEqual(slot1, result()) && slot1.getCount() < slot1.getMaxCount());

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
        return ID;
    }

    @Override
    public RecipeSerializer<AlchemyRecipe> getSerializer() {
        return HmRecipes.ALCHEMY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return HmRecipes.ALCHEMY;
    }

    public static class Serializer implements RecipeSerializer<AlchemyRecipe> {
        @Override
        public AlchemyRecipe read(Identifier id, JsonObject json) {
            return null;
        }

        @Override
        public AlchemyRecipe read(Identifier id, PacketByteBuf buf) {
            return null;
        }

        @Override
        public void write(PacketByteBuf buf, AlchemyRecipe recipe) {

        }
    }
}