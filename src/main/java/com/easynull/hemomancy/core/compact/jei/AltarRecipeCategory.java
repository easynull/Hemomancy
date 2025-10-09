package com.easynull.hemomancy.core.compact.jei;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.registers.HcElements;
import com.easynull.hemomancy.registers.recipes.AlchemyRecipe;
import com.easynull.hemomancy.registers.recipes.AltarRecipe;
import com.mw.nullcore.client.render.Transform;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import static com.easynull.hemomancy.core.compact.jei.AlchemyRecipeCategory.getItems;

public final class AltarRecipeCategory implements IRecipeCategory<AltarRecipe> {
    public static final IRecipeType<AltarRecipe> type = RecipeType.create("hemomancy", "altar", AltarRecipe.class);
    final IDrawable icon, background;

    public AltarRecipeCategory(IGuiHelper helper) {
        icon = helper.createDrawableItemStack(HcElements.bloodAltar.toStack());
        background = helper.createDrawable(ResourceLocation.withDefaultNamespace("textures/gui/demo_background.png"), 4, 3, 122, 116);
    }

    @Override
    public IRecipeType<AltarRecipe> getRecipeType() {
        return type;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("item.hemomancy.blood_altar");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AltarRecipe recipe, IFocusGroup group) {
        int centerX = background.getWidth() / 2 - 10;
        int centerY = background.getHeight() / 2 - 10;
        builder.addSlot(RecipeIngredientRole.INPUT, centerX, centerY).addIngredients(recipe.input());
        builder.addSlot(RecipeIngredientRole.OUTPUT, centerX, centerY - 20).addItemStack(recipe.result());
    }

    @Override
    public void draw(AltarRecipe recipe, IRecipeSlotsView view, GuiGraphics gg, double mouseX, double mouseY) {
        int centerX = background.getWidth() / 2 - 19;
        int centerY = background.getHeight() / 2 - 7;
        Transform.create(gg.pose(), tr -> tr.autoPose(()-> {
            tr.scale(centerX, centerY, 200, 2.2f, 2.2f, 2.2f);
            gg.renderItem(HcElements.bloodAltar.toStack(), centerX, centerY);
        }));
        gg.drawString(Minecraft.getInstance().font, "LP: " + recipe.lp(), 5, 5, 0xFF0000, false);
    }
}
