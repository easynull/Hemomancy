package ru.easynull.hemomancy.render.gui.book.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import ru.easynull.hemomancy.render.gui.book.PageGui;

import java.util.List;

public final class TextElement implements PageElement {
    private static final float SCROLL_SPEED = 0.4f;
    private static final int PAUSE_DURATION = 160;
    private static final int TEXT_COLOR = 0x3F0000;
    private static final int TEXT_OFFSET_X = -15;
    private static final int SCISSOR_OFFSET_X = -20;
    private static final int WRAP_WIDTH = 110;

    private final Component text;
    private final int displayHeight;
    private List<FormattedCharSequence> wrappedLines;
    private int totalHeight;

    private float scrollY;
    private int pauseTicks;
    private boolean movingDown;

    public TextElement(Component text, int displayHeight) {
        this.text = text;
        this.displayHeight = displayHeight;
    }

    private void wrap() {
        if (wrappedLines != null) return;
        Font renderer = Minecraft.getInstance().font;
        wrappedLines = renderer.split(text, WRAP_WIDTH);
        totalHeight = wrappedLines.size() * renderer.lineHeight;
    }

    @Override
    public int getHeight(int maxWidth) {
        return displayHeight + 3;
    }

    @Override
    public void render(GuiGraphics context, int x, int y, int mouseX, int mouseY, float delta) {
        wrap();
        x = x + 23;
        Font renderer = Minecraft.getInstance().font;
        updateScrolling(delta);

        float maxScroll = Math.max(0, totalHeight - displayHeight);
        scrollY = Mth.clamp(scrollY, 0, maxScroll);

        int firstLine = (int) (scrollY / renderer.lineHeight);
        int lastLine = Math.min((int) Math.ceil((scrollY + displayHeight) / renderer.lineHeight) + 1, wrappedLines.size());

        context.enableScissor(x + SCISSOR_OFFSET_X, y, x + PageGui.BOOK_WIDTH, y + displayHeight);
        for (int i = firstLine; i < lastLine; i++) {
            FormattedCharSequence line = wrappedLines.get(i);
            int lineY = y + i * renderer.lineHeight - (int) scrollY;
            context.drawString(renderer, line, x + TEXT_OFFSET_X, lineY, TEXT_COLOR, false);
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