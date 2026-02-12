package ru.easynull.hemomancy.compact.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.recipes.AlchemyRecipe;

public final class AlchemyRecipeCategory implements IRecipeCategory<AlchemyRecipe> {
    public static final RecipeType<AlchemyRecipe> TYPE = RecipeType.create(Hemomancy.ID, "alchemy", AlchemyRecipe.class);

    private final IDrawable icon;

    public AlchemyRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemLike(HmBlocks.ALCHEMY_TABLE);
    }

    @Override
    public RecipeType<AlchemyRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("jei.category.hemomancy.altar");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlchemyRecipe recipe, IFocusGroup focuses) {
        int x = 20;
        int y = 30;
        int index = 0;
        for (Ingredient ing : recipe.inputs()) {
            builder.addSlot(RecipeIngredientRole.INPUT, x + (index % 5) * 18, y + (index / 5) * 18)
                    .addIngredients(ing);
            index++;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 30).addItemStack(recipe.result());
    }

    @Override
    public void draw(AlchemyRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext ctx, double mouseX, double mouseY) {
        String lpText = "LP: " + recipe.lp();
        ctx.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, lpText, 5, 5, 0xFF0000);
    }
}
