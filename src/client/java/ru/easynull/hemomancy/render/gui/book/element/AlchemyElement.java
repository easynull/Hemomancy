package ru.easynull.hemomancy.render.gui.book.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.HemomancyClient;
import ru.easynull.hemomancy.registry.HmRecipes;
import ru.easynull.hemomancy.registry.recipes.AlchemyRecipe;
import ru.easynull.hemomancy.render.gui.book.PageGui;

import java.util.List;
import java.util.Optional;

import static ru.easynull.hemomancy.render.gui.book.BookGui.BOOK;

public final class AlchemyElement implements PageElement {
    private static final int ITEM_STEP = 22;
    private static final int SLOT_SIZE = 21;
    private static final int BIG_SLOT_WIDTH = 29;
    private static final int BIG_SLOT_HEIGHT = 28;
    private static final int ICON_SIZE = 16;
    private static final int OUTPUT_OFFSET_X = 7;
    private static final int OUTPUT_OFFSET_Y = -58;
    private static final int COUNT_OFFSET_X = 20;
    private static final int COUNT_OFFSET_Y = -4;
    private static final int RADIUS = 40;

    private final Item resultItem;
    private final int height;
    private final AlchemyRecipe recipe;
//    private List<ItemStack> orbVariants;

    public AlchemyElement(Item resultItem) {
        this(resultItem, 120);
    }

    public AlchemyElement(Item resultItem, int height) {
        this.resultItem = resultItem;
        this.height = height;
        this.recipe = findRecipe();
    }

    @Override
    public int getHeight(int maxWidth) {
        return height;
    }

    private AlchemyRecipe findRecipe() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return null;
        Optional<AlchemyRecipe> found = client.world.getRecipeManager().listAllOfType(HmRecipes.ALCHEMY).stream()
                .filter(recipe -> recipe.getOutput(null).isOf(resultItem))
                .findFirst();
        return found.orElse(null);
    }

//    private List<ItemStack> getOrbVariants() {
//        if (orbVariants != null) return orbVariants;
//        TagKey<Item> orbTag = TagKey.of(RegistryKeys.ITEM, Hemomancy.path("orbs"));
//        var items = Registries.ITEM.getEntryList(orbTag);
//        orbVariants = items.map(registryEntries -> registryEntries.stream()
//                .map(entry -> new ItemStack(entry.value()))
//                .toList()).orElseGet(List::of);
//        return orbVariants;
//    }

    private ItemStack getAnimatedIngredient(Ingredient ing) {
        ItemStack[] stacks = ing.getMatchingStacks();
        if (stacks.length == 0) return ItemStack.EMPTY;
        int idx = (HemomancyClient.tickClient / 40) % stacks.length;
        return stacks[idx].copy();
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

        int centerX = resultSlotX + 8 + SLOT_SIZE / 2;
        int centerY = resultSlotY - 60 + SLOT_SIZE / 2;

        List<Ingredient> ingredients = recipe.inputs();
        int count = ingredients.size();
        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count - Math.PI / 2;
            int slotX = (int) (centerX + RADIUS * Math.cos(angle) - (double) SLOT_SIZE / 2);
            int slotY = (int) (centerY + RADIUS * Math.sin(angle) - (double) SLOT_SIZE / 2);
            context.drawTexture(BOOK, slotX - 1, slotY, 50, 189, SLOT_SIZE + 3, SLOT_SIZE + 2, 512, 512);

            ItemStack stack = getAnimatedIngredient(ingredients.get(i));
            if (!stack.isEmpty()) {
                context.drawItem(stack, slotX + 4, slotY + 3);
            }
        }

//        List<ItemStack> orbs = getOrbVariants();
//        if (!orbs.isEmpty()) {
//            int idx = (HemomancyClient.tickClient / 40) % orbs.size();
//            context.drawItem(orbs.get(idx), resultX + 7, resultY - 59);
//            context.drawTexture(PageGui.BOOK, resultSlotX + 8, resultSlotY - 60, 0, 190, SLOT_SIZE, SLOT_SIZE, 512, 512);
//        }

        context.drawTexture(BOOK, resultSlotX + 4, resultSlotY + OUTPUT_OFFSET_Y - 5, 22, 189, BIG_SLOT_WIDTH, BIG_SLOT_HEIGHT, 512, 512);
        context.drawTexture(BOOK, resultSlotX + 15, resultSlotY + OUTPUT_OFFSET_Y - 18, 0, 223, 8, 10, 512, 512);

        var renderer = MinecraftClient.getInstance().textRenderer;
        context.drawText(renderer, String.format("%s LP", recipe.lp()), resultSlotX, resultSlotY + 6, 0xFFECE3D6, false);

        ItemStack output = recipe.getOutput(null);
        context.drawItem(output, resultX + OUTPUT_OFFSET_X, resultY + OUTPUT_OFFSET_Y);
        if (output.getCount() > 1) {
            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 200f);
            context.drawText(renderer, String.valueOf(output.getCount()), resultX + COUNT_OFFSET_X, resultY + OUTPUT_OFFSET_Y + 11, 0xFFFFFFFF, true);
            context.getMatrices().pop();
        }
    }

    @Override
    public ItemStack getTooltipStack(int relX, int relY) {
        if (recipe == null) return null;

        int gridSizePx = 3 * ITEM_STEP;
        int offsetX = (int) ((PageGui.BOOK_WIDTH / 2.8f - gridSizePx) / 2);
        int offsetY = (int) ((this.height - gridSizePx) / 2.5f);
        int resultX = offsetX + 12;
        int resultY = offsetY + ITEM_STEP * 4;
        int resultSlotX = resultX - 4;
        int resultSlotY = resultY - 1;

        int centerX = resultSlotX + 8 + SLOT_SIZE / 2;
        int centerY = resultSlotY - 60 + SLOT_SIZE / 2;

        List<Ingredient> ingredients = recipe.inputs();
        int count = ingredients.size();
        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count - Math.PI / 2;
            int slotX = (int) (centerX + RADIUS * Math.cos(angle) - (double) SLOT_SIZE / 2) + 1;
            int slotY = (int) (centerY + RADIUS * Math.sin(angle) - (double) SLOT_SIZE / 2) + 4;
            int itemX = slotX + (SLOT_SIZE - ICON_SIZE) / 2;
            int itemY = slotY + (SLOT_SIZE - ICON_SIZE) / 2;
            if (relX >= itemX && relX < itemX + ICON_SIZE && relY >= itemY && relY < itemY + ICON_SIZE) {
                return getAnimatedIngredient(ingredients.get(i));
            }
        }

//        int orbX = resultSlotX + 8 + (SLOT_SIZE - ICON_SIZE) / 2;
//        int orbY = resultSlotY - 60 + (SLOT_SIZE - ICON_SIZE) / 2;
//        if (relX >= orbX && relX < orbX + ICON_SIZE && relY >= orbY && relY < orbY + ICON_SIZE) {
//            List<ItemStack> orbs = getOrbVariants();
//            if (!orbs.isEmpty()) {
//                int idx = (HemomancyClient.tickClient / 40) % orbs.size();
//                return orbs.get(idx);
//            }
//            return null;
//        }

        int outIconX = resultX + OUTPUT_OFFSET_X;
        int outIconY = resultY + OUTPUT_OFFSET_Y;
        if (relX >= outIconX && relX < outIconX + ICON_SIZE && relY >= outIconY && relY < outIconY + 22) {
            return recipe.getOutput(null);
        }

        return null;
    }
}