//package com.easynull.hemomancy.core.compact.jei;
//
//import com.easynull.hemomancy.Hemomancy;
//import com.easynull.hemomancy.registers.HcElements;
//import com.easynull.hemomancy.registers.HcRecipes;
//import com.easynull.hemomancy.registers.recipes.AlchemyRecipe;
//import com.google.common.collect.Lists;
//import com.mojang.blaze3d.vertex.PoseStack;
//import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
//import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
//import mezz.jei.api.gui.drawable.IDrawable;
//import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
//import mezz.jei.api.helpers.IGuiHelper;
//import mezz.jei.api.recipe.IFocusGroup;
//import mezz.jei.api.recipe.RecipeIngredientRole;
//import mezz.jei.api.recipe.RecipeType;
//import mezz.jei.api.recipe.category.IRecipeCategory;
//import mezz.jei.api.recipe.types.IRecipeType;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.item.ItemStack;
//import org.jetbrains.annotations.Nullable;
//
//import javax.annotation.Nonnull;
//import java.util.List;
//
//public class AlchemyRecipeCategory implements IRecipeCategory<AlchemyRecipe> {
//    final IDrawable icon, background;
//
//    public AlchemyRecipeCategory(IGuiHelper helper) {
//        icon = helper.createDrawableItemStack(HcElements.alchemyTable.toStack());
//        background = helper.createDrawable(Hemomancy.textures(""), 0, 0, 256, 256);
//    }
//
//    @Override
//    public IRecipeType<AlchemyRecipe> getRecipeType() {
//        return RecipeType.create("hemomancy", "alchemy", AlchemyRecipe.class);
//    }
//
//    @Override
//    public Component getTitle() {
//        return Component.translatable("item.hemomancy.alchemy_table");
//    }
//
//    @Override
//    public @Nullable IDrawable getIcon() {
//        return icon;
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayoutBuilder builder, AlchemyRecipe recipe, IFocusGroup group) {
//
//    }
//
//    @Override
//    public void draw(AlchemyRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
//        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
//    }
//}
