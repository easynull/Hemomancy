package ru.easynull.hemomancy.render.gui.book.element;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.easynull.hemomancy.render.gui.book.PageGui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.easynull.hemomancy.render.gui.book.BookGui.BOOK;

public record ItemsElement(List<Item> items) implements PageElement {
    private static final int SLOT_STEP = 22;
    private static final int SLOT_WIDTH = 24;
    private static final int SLOT_HEIGHT = 20;
    private static final int ICON_SIZE = 16;
    private static final int ICON_OFFSET = 3;
    private static final int MAX_ITEMS = 4;

    public ItemsElement(Item... items) {
        this(Arrays.stream(items).filter(Objects::nonNull).toList());
    }

    @Override
    public int getHeight(int maxWidth) {
        return 19;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        int count = Math.min(items.size(), MAX_ITEMS);
        if (count == 0) return;
        x = x + 16;
        float groupWidth = (count - 1) * SLOT_STEP + SLOT_WIDTH;
        int firstSlotLeft = (int) (x + 40 - groupWidth / 2.3f);
        int firstIconX = firstSlotLeft + ICON_OFFSET;

        for (int i = 0; i < count; i++) {
            int itemX = firstIconX + i * SLOT_STEP;
            int slotX = itemX - ICON_OFFSET;
            context.drawTexture(BOOK, slotX, y, 0, 190, SLOT_WIDTH, SLOT_HEIGHT, 512, 512);
            context.drawItem(items.get(i).getDefaultStack(), itemX, y + 2);
        }
    }

    @Override
    public ItemStack getTooltipStack(int relX, int relY) {
        int count = Math.min(items.size(), MAX_ITEMS);
        if (count == 0) return null;

        float groupWidth = (count - 1) * SLOT_STEP + SLOT_WIDTH;
        int firstSlotLeft = (int) (40 - groupWidth / 2.0f);
        int firstIconX = firstSlotLeft + ICON_OFFSET;

        for (int i = 0; i < count; i++) {
            int itemX = firstIconX + i * SLOT_STEP;
            if (relX >= itemX && relX < itemX + ICON_SIZE && relY >= 0 && relY < ICON_SIZE) {
                return items.get(i).getDefaultStack();
            }
        }
        return null;
    }
}