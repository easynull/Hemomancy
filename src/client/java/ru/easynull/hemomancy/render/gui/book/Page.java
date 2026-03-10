package ru.easynull.hemomancy.render.gui.book;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.render.gui.book.element.PageElement;

public record Page(int lvl, String requireMod, PageElement... elements) {
    private static final int PAGE_WIDTH = 120;
    private static final int PAGE_HEIGHT = 160;
    private static final int CONTENT_OFFSET_X = 10;
    private static final int CONTENT_WIDTH = PAGE_WIDTH - 20;

    public Page(int lvl, PageElement... elements){
        this(lvl, null, elements);
    }

    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        int currentY = y;
        for (PageElement element : elements) {
            int height = element.getHeight(CONTENT_WIDTH);
            if (currentY + height > y + PAGE_HEIGHT) break;
            element.render(context, x + CONTENT_OFFSET_X, currentY, mouseX, mouseY, delta);
            currentY += height;
        }
    }

    public boolean isUnlocked(PlayerEntity player) {
        return MagePlayer.of(player).getLevel() >= lvl;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return handleEvent(mouseX, mouseY, (e, ex, ey) -> e.mouseClicked(ex, ey, button));
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return handleEvent(mouseX, mouseY, (e, ex, ey) -> e.mouseDragged(ex, ey, button, deltaX, deltaY));
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return handleEvent(mouseX, mouseY, (e, ex, ey) -> e.mouseScrolled(ex, ey, amount));
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return handleEvent(mouseX, mouseY, (e, ex, ey) -> e.mouseReleased(ex, ey, button));
    }

    private boolean handleEvent(double mouseX, double mouseY, MouseEventHandler handler) {
        if (mouseX < CONTENT_OFFSET_X || mouseX > CONTENT_OFFSET_X + CONTENT_WIDTH || mouseY < 0 || mouseY > PAGE_HEIGHT)
            return false;

        int currentY = 0;
        for (PageElement element : elements) {
            int height = element.getHeight(CONTENT_WIDTH);
            if (currentY + height > PAGE_HEIGHT) break;
            if (mouseY >= currentY && mouseY <= currentY + height) {
                double elemX = mouseX - CONTENT_OFFSET_X;
                double elemY = mouseY - currentY;
                if (handler.handle(element, elemX, elemY)) return true;
            }
            currentY += height;
        }
        return false;
    }

    public void renderTooltip(DrawContext context, int mouseX, int mouseY, int relX, int relY) {
        if (relX < CONTENT_OFFSET_X || relX > CONTENT_OFFSET_X + CONTENT_WIDTH || relY < 0 || relY > PAGE_HEIGHT)
            return;

        int currentY = 0;
        for (PageElement element : elements) {
            int height = element.getHeight(CONTENT_WIDTH);
            if (currentY + height > PAGE_HEIGHT) break;
            if (relY >= currentY && relY <= currentY + height) {
                int elemX = relX - CONTENT_OFFSET_X;
                int elemY = relY - currentY;
                ItemStack stack = element.getTooltipStack(elemX, elemY);
                if (stack != null) {
                    context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, stack, mouseX, mouseY);
                    return;
                }
            }
            currentY += height;
        }
    }

    @FunctionalInterface
    private interface MouseEventHandler {
        boolean handle(PageElement element, double elemX, double elemY);
    }
}