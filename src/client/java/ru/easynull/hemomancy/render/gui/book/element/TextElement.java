package ru.easynull.hemomancy.render.gui.book.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import ru.easynull.hemomancy.render.gui.book.PageGui;

import java.util.List;

public final class TextElement implements PageElement {
    private static final float SCROLL_SPEED = 0.4f;
    private static final int PAUSE_DURATION = 320;
    private static final int TEXT_COLOR = 0x3F0000;
    private static final int TEXT_OFFSET_X = -15;
    private static final int SCISSOR_OFFSET_X = -20;
    private static final int WRAP_WIDTH = 110;

    private final Text text;
    private final int displayHeight;
    private List<OrderedText> wrappedLines;
    private int totalHeight;

    private float scrollY;
    private int pauseTicks;
    private boolean movingDown;

    public TextElement(Text text, int displayHeight) {
        this.text = text;
        this.displayHeight = displayHeight;
    }

    private void wrap() {
        if (wrappedLines != null) return;
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        wrappedLines = renderer.wrapLines(text, WRAP_WIDTH);
        totalHeight = wrappedLines.size() * renderer.fontHeight;
    }

    @Override
    public int getHeight(int maxWidth) {
        return displayHeight + 3;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        wrap();
        x = x + 23;
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        updateScrolling(delta);

        float maxScroll = Math.max(0, totalHeight - displayHeight);
        scrollY = MathHelper.clamp(scrollY, 0, maxScroll);

        int firstLine = (int) (scrollY / renderer.fontHeight);
        int lastLine = Math.min((int) Math.ceil((scrollY + displayHeight) / renderer.fontHeight) + 1, wrappedLines.size());

        context.enableScissor(x + SCISSOR_OFFSET_X, y, x + PageGui.BOOK_WIDTH, y + displayHeight);
        for (int i = firstLine; i < lastLine; i++) {
            OrderedText line = wrappedLines.get(i);
            int lineY = y + i * renderer.fontHeight - (int) scrollY;
            context.drawText(renderer, line, x + TEXT_OFFSET_X, lineY, TEXT_COLOR, false);
        }
        context.disableScissor();
    }

    private void updateScrolling(float delta) {
        if (totalHeight <= displayHeight) {
            scrollY = 0;
            return;
        }

        float maxScroll = totalHeight - displayHeight;

        if (pauseTicks > 0) {
            pauseTicks--;
            return;
        }

        float step = SCROLL_SPEED * delta;
        if (movingDown) {
            scrollY = Math.min(scrollY + step, maxScroll);
            if (scrollY >= maxScroll) {
                pauseTicks = PAUSE_DURATION;
                movingDown = false;
            }
        } else {
            scrollY = Math.max(scrollY - step, 0);
            if (scrollY <= 0) {
                pauseTicks = PAUSE_DURATION;
                movingDown = true;
            }
        }
    }
}