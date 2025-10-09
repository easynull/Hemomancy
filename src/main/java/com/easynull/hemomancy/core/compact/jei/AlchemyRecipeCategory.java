package com.easynull.hemomancy.core.compact.jei;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.registers.HcElements;
import com.easynull.hemomancy.registers.HcRecipes;
import com.easynull.hemomancy.registers.recipes.AlchemyRecipe;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
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

public final class AlchemyRecipeCategory implements IRecipeCategory<AlchemyRecipe> {
    public static final IRecipeType<AlchemyRecipe> type = RecipeType.create("hemomancy", "alchemy", AlchemyRecipe.class);
    final IDrawable icon, background;

    public AlchemyRecipeCategory(IGuiHelper helper) {
        icon = helper.createDrawableItemStack(HcElements.alchemyTable.toStack());
        background = helper.createDrawable(ResourceLocation.withDefaultNamespace("textures/gui/demo_background.png"), 4, 3, 172, 120);
    }

    @Override
    public IRecipeType<AlchemyRecipe> getRecipeType() {
        return type;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("item.hemomancy.alchemy_table");
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
    public void setRecipe(IRecipeLayoutBuilder builder, AlchemyRecipe recipe, IFocusGroup group) {
        int centerX = background.getWidth() / 2 - 10;
        int centerY = background.getHeight() / 2 - 10;
        float radius = 35f;
        var inputs = recipe.inputs();
        for (int i = 0; i < inputs.size(); i++) {
            float angle = (float) (2 * Math.PI * i / inputs.size());
            int x = (int) (centerX + Math.cos(angle) * radius);
            int y = (int) (centerY + Math.sin(angle) * radius);
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(inputs.get(i));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, centerX, centerY).addItemStack(recipe.result());
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 5, 100).addItemStacks(getItems("hemomancy:orbs"));
    }

    @Override
    public void draw(AlchemyRecipe recipe, IRecipeSlotsView view, GuiGraphics gg, double mouseX, double mouseY) {
        gg.drawString(Minecraft.getInstance().font, "LP: " + recipe.lp(), 5, 5, 0xFF0000, false);
    }

    public static List<ItemStack> getItems(String tag) {
        TagKey<Item> tagKey = TagKey.create(Registries.ITEM, ResourceLocation.parse(tag));
        return BuiltInRegistries.ITEM.getOrThrow(tagKey).stream().map(ItemStack::new).collect(Collectors.toList());
    }

    public static List<BlockState> getBlocks(String tag) {
        TagKey<Block> tagKey = TagKey.create(Registries.BLOCK, ResourceLocation.parse(tag));
        return BuiltInRegistries.BLOCK.getOrThrow(tagKey).stream().map(Holder::value).map(Block::defaultBlockState).collect(Collectors.toList());
    }
}
