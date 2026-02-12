package ru.easynull.hemomancy.compact.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.compact.jei.categories.AlchemyRecipeCategory;
import ru.easynull.hemomancy.compact.jei.categories.AltarRecipeCategory;
import ru.easynull.hemomancy.registry.HmRecipes;

import java.util.List;

@JeiPlugin
public final class JEIIntegration implements IModPlugin {
    @Override
    public @NotNull Identifier getPluginUid() {
        return Hemomancy.path("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        reg.addRecipeCategories(new AlchemyRecipeCategory(reg.getJeiHelpers().getGuiHelper()), new AltarRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        var world = MinecraftClient.getInstance().world;
        reg.addRecipes(AlchemyRecipeCategory.TYPE, getRecipes(world, HmRecipes.ALCHEMY));
        reg.addRecipes(AltarRecipeCategory.TYPE, getRecipes(world, HmRecipes.FUSION));
    }

    public static <C extends Inventory, T extends Recipe<C>> List<T> getRecipes(World world, RecipeType<T> type) {
        RecipeManager manager = world.getRecipeManager();
        return manager.listAllOfType(type);
    }
}
