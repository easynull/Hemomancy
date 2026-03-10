package ru.easynull.hemomancy.render.gui.book.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import ru.easynull.hemomancy.HemomancyClient;
import ru.easynull.hemomancy.registry.HmRecipes;
import ru.easynull.hemomancy.registry.recipes.FusionRecipe;
import ru.easynull.hemomancy.render.gui.book.PageGui;

import java.util.Optional;

import static ru.easynull.hemomancy.render.gui.book.BookGui.BOOK;

public final class FusionElement implements PageElement {
    private static final int ITEM_STEP = 22;
    private static final int SLOT_SIZE = 21;
    private static final int SLOT_HEIGHT = 21;
    private static final int BIG_SLOT_WIDTH = 29;
    private static final int BIG_SLOT_HEIGHT = 28;
    private static final int ICON_SIZE = 16;
    private static final int INPUT_OFFSET_X = 7;
    private static final int INPUT_OFFSET_Y = -59;
    private static final int OUTPUT_OFFSET_X = 7;
    private static final int OUTPUT_OFFSET_Y = -15;
    private static final int COUNT_OFFSET_X = 20;
    private static final int COUNT_OFFSET_Y = -4;
    private static final int LP_OFFSET_X = 27;
    private static final int LP_OFFSET_Y = -33;
    private static final int TIER_OFFSET_X = 11;
    private static final int TIER_OFFSET_Y = -33;

    private final Item resultItem;
    private final int height;
    private final FusionRecipe recipe;

    public FusionElement(Item resultItem) {
        this(resultItem, 100);
    }

    public FusionElement(Item resultItem, int height) {
        this.resultItem = resultItem;
        this.height = height;
        this.recipe = findRecipe();
    }

    @Override
    public int getHeight(int maxWidth) {
        return height;
    }

    private FusionRecipe findRecipe() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return null;
        Optional<FusionRecipe> found = client.world.getRecipeManager().listAllOfType(HmRecipes.FUSION).stream()
                .filter(recipe -> recipe.getOutput(null).isOf(resultItem))
                .findFirst();
        return found.orElse(null);
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        if (recipe == null) {
            findRecipe();
            return;
        }

        int gridSizePx = 3 * ITEM_STEP;
        int offsetY = (this.height - gridSizePx) / 2;
        int startX = x + 34;
        int startY = y + offsetY;

        int resultX = startX + 14;
        int resultY = startY + ITEM_STEP * 4;
        int resultSlotX = resultX - 4;
        int resultSlotY = resultY - 1;

        context.drawTexture(BOOK, resultSlotX + 8, resultSlotY - 60, 0, 190, SLOT_SIZE, SLOT_HEIGHT, 512, 512);
        context.drawTexture(BOOK, resultSlotX + 4, resultSlotY - 20, 22, 189, BIG_SLOT_WIDTH, BIG_SLOT_HEIGHT, 512, 512);
        context.drawTexture(BOOK, resultSlotX + 15, resultSlotY - 34, 9, 212, 8, 11, 512, 512);

        var renderer = MinecraftClient.getInstance().textRenderer;
        context.drawText(renderer, String.format("%s LP", recipe.lp()), resultSlotX + LP_OFFSET_X, resultSlotY + LP_OFFSET_Y, 0xFFECE3D6, false);
        context.drawText(renderer, Text.translatable("tooltip.hemomancy.tier", recipe.tier()),
                resultSlotX + TIER_OFFSET_X - renderer.getWidth(Text.translatable("tooltip.hemomancy.tier", recipe.tier())),
                resultSlotY + TIER_OFFSET_Y, 0xFFECE3D6, false);

        ItemStack[] variants = recipe.input().getMatchingStacks();
        ItemStack inputStack = variants[HemomancyClient.tickClient / 40 % variants.length];
        context.drawItem(inputStack, resultX + INPUT_OFFSET_X, resultY + INPUT_OFFSET_Y);

        ItemStack output = recipe.getOutput(null);
        context.drawItem(output, resultX + OUTPUT_OFFSET_X, resultY + OUTPUT_OFFSET_Y);
        if (output.getCount() > 1) {
            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 200f);
            context.drawText(renderer, String.valueOf(output.getCount()), resultX + COUNT_OFFSET_X, resultY + COUNT_OFFSET_Y, 0xFFFFFFFF, true);
            context.getMatrices().pop();
        }
    }

    @Override
    public ItemStack getTooltipStack(int relX, int relY) {
        if (recipe == null) return null;

        int gridSizePx = 3 * ITEM_STEP;
        int offsetX = (int) ((PageGui.BOOK_WIDTH / 2.9f - gridSizePx) / 2);
        int offsetY = (this.height - gridSizePx) / 2;
        int resultX = offsetX + 14 + INPUT_OFFSET_X;
        int resultY = offsetY + ITEM_STEP * 4 + INPUT_OFFSET_Y;
        if (relX >= resultX && relX < resultX + ICON_SIZE && relY >= resultY && relY < resultY + ICON_SIZE) {
            ItemStack[] variants = recipe.input().getMatchingStacks();
            return variants[HemomancyClient.tickClient / 40 % variants.length];
        }
        int outX = offsetX + 14 + OUTPUT_OFFSET_X;
        int outY = offsetY + ITEM_STEP * 4 + OUTPUT_OFFSET_Y;
        if (relX >= outX && relX < outX + ICON_SIZE && relY >= outY && relY < outY + 32) {
            return recipe.getOutput(null);
        }
        return null;
    }
}