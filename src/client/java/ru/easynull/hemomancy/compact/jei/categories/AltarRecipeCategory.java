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
import net.minecraft.text.Text;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.recipes.FusionRecipe;
import ru.easynull.hemomancy.render.Transform;

public final class AltarRecipeCategory implements IRecipeCategory<FusionRecipe> {
    public static final RecipeType<FusionRecipe> TYPE = RecipeType.create(Hemomancy.ID, "altar", FusionRecipe.class);
    private static final ItemStack ICON = new ItemStack(HmBlocks.BLOOD_ALTAR);

    private final IDrawable icon;

    public AltarRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ICON);
    }

    @Override
    public RecipeType<FusionRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, FusionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 30)
                .addIngredients(recipe.input());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 30)
                .addItemStack(recipe.result());
    }

    @Override
    public void draw(FusionRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext ctx, double mouseX, double mouseY) {
        int centerX = getWidth() / 2 - 19;
        int centerY = getHeight() / 2 - 7;
        Transform.create(ctx.getMatrices(), tr -> tr.autoPose(()-> {
            tr.scale(centerX, centerY, 200, 2.2f, 2.2f, 2.2f);
            ctx.drawItem(ICON, centerX, centerY);
        }));
        String lpText = "LP: " + recipe.lp() + " (Tier " + recipe.tier() + ")";
        ctx.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, lpText, 5, 5, 0xFF0000);
    }
}
