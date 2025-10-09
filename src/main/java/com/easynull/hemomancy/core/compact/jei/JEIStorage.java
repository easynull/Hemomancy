package com.easynull.hemomancy.core.compact.jei;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.registers.recipes.AlchemyRecipe;
import com.easynull.hemomancy.registers.recipes.AltarRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@JeiPlugin
public final class JEIStorage implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return Hemomancy.path("main");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        reg.addRecipeCategories(new AltarRecipeCategory(reg.getJeiHelpers().getGuiHelper()), new AlchemyRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        reg.addRecipes(AltarRecipeCategory.type, AltarRecipe.recipes);
        reg.addRecipes(AlchemyRecipeCategory.type, AlchemyRecipe.recipes);
    }
}
