package ru.easynull.hemomancy.render.gui.book.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import ru.easynull.hemomancy.HemomancyClient;
import ru.easynull.hemomancy.render.gui.book.PageGui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.easynull.hemomancy.render.gui.book.BookGui.BOOK;

public final class CraftingElement implements PageElement {
    private static final int ITEM_STEP = 22;
    private static final int SLOT_SIZE = 24;
    private static final int SLOT_HEIGHT = 20;
    private static final int GRID_SIZE = 3;
    private static final int ICON_SIZE = 16;
    private static final int ICON_OFFSET_X = -1;
    private static final int ICON_OFFSET_Y = 2;
    private static final int RESULT_OFFSET_X = 7;
    private static final int RESULT_OFFSET_Y = -5;
    private static final int RESULT_COUNT_OFFSET_X = 20;
    private static final int RESULT_COUNT_OFFSET_Y = 6;

    private final Item resultItem;
    private final int height;
    private final CraftingRecipe recipe;

    public CraftingElement(Item resultItem) {
        this(resultItem, 120);
    }

    public CraftingElement(Item resultItem, int height) {
        this.resultItem = resultItem;
        this.height = height;
        this.recipe = findRecipe();
    }

    @Override
    public int getHeight(int maxWidth) {
        return height;
    }

    private CraftingRecipe findRecipe() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return null;
        Optional<CraftingRecipe> found = client.world.getRecipeManager().listAllOfType(RecipeType.CRAFTING).stream()
                .filter(recipe -> recipe.getOutput(null).isOf(resultItem))
                .findFirst();
        return found.orElse(null);
    }

    private List<List<ItemStack>> getIngredientsMatrix(CraftingRecipe recipe) {
        List<List<ItemStack>> matrix = new ArrayList<>(GRID_SIZE);
        for (int row = 0; row < GRID_SIZE; row++) {
            List<ItemStack> rowItems = new ArrayList<>(GRID_SIZE);
            for (int col = 0; col < GRID_SIZE; col++) rowItems.add(ItemStack.EMPTY);
            matrix.add(rowItems);
        }
        if (recipe == null) return matrix;

        List<Ingredient> ingredients = recipe.getIngredients();
        if (recipe instanceof ShapedRecipe s) {
            int width = s.getWidth();
            int height = s.getHeight();
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int index = row * width + col;
                    if (index >= ingredients.size()) continue;
                    ItemStack[] stacks = ingredients.get(index).getMatchingStacks();
                    if (stacks.length > 0) matrix.get(row).set(col, stacks[HemomancyClient.tickClient / 40 % stacks.length].copy());
                }
            }
        }
        return matrix;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        if (recipe == null) return;

        int gridSizePx = GRID_SIZE * ITEM_STEP;
        int offsetY = (this.height - gridSizePx) / 2;
        int startX = x + 34;
        int startY = y + offsetY;

        List<List<ItemStack>> matrix = getIngredientsMatrix(recipe);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int itemX = startX + col * ITEM_STEP;
                int itemY = startY + row * ITEM_STEP;
                context.drawTexture(BOOK, itemX - 4, itemY, 0, 190, SLOT_SIZE, SLOT_HEIGHT, 512, 512);
                ItemStack stack = matrix.get(row).get(col);
                if (!stack.isEmpty()) context.drawItem(stack, itemX + ICON_OFFSET_X, itemY + ICON_OFFSET_Y);
            }
        }

        int resultX = startX + 14;
        int resultY = startY + ITEM_STEP * 4;
        context.drawTexture(BOOK, resultX - 4 + 4, resultY - 1 - 10, 22, 189, 29, 28, 512, 512);
        context.drawTexture(BOOK, resultX - 4 + 15, resultY - 1 - ITEM_STEP, 0, 212, 8, 11, 512, 512);


        ItemStack output = recipe.getOutput(null);
        context.drawItem(output, resultX + RESULT_OFFSET_X, resultY + RESULT_OFFSET_Y);
        if (output.getCount() > 1) {
            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 200f);
            context.drawText(MinecraftClient.getInstance().textRenderer, String.valueOf(output.getCount()), resultX + RESULT_COUNT_OFFSET_X, resultY + RESULT_COUNT_OFFSET_Y, 0xFFFFFFFF, true);
            context.getMatrices().pop();
        }
    }

    @Override
    public ItemStack getTooltipStack(int relX, int relY) {
        if (recipe == null) return null;

        int gridSizePx = GRID_SIZE * ITEM_STEP;
        int offsetX = (int) ((PageGui.BOOK_WIDTH / 2.9f - gridSizePx) / 2);
        int offsetY = (this.height - gridSizePx) / 2;

        List<List<ItemStack>> matrix = getIngredientsMatrix(recipe);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int itemX = offsetX + col * ITEM_STEP + ICON_OFFSET_X;
                int itemY = offsetY + row * ITEM_STEP + ICON_OFFSET_Y;
                if (relX >= itemX && relX < itemX + ICON_SIZE && relY >= itemY && relY < itemY + ICON_SIZE) {
                    ItemStack stack = matrix.get(row).get(col);
                    return stack.isEmpty() ? null : stack;
                }
            }
        }

        int resultX = offsetX + 14 + RESULT_OFFSET_X;
        int resultY = offsetY + ITEM_STEP * 4 + RESULT_OFFSET_Y;
        if (relX >= resultX && relX < resultX + ICON_SIZE && relY >= resultY && relY < resultY + ICON_SIZE) {
            return recipe.getOutput(null);
        }
        return null;
    }
}