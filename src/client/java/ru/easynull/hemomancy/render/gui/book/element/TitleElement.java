package ru.easynull.hemomancy.render.gui.book.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import ru.easynull.hemomancy.render.gui.book.PageGui;

import static ru.easynull.hemomancy.render.gui.book.BookGui.BOOK;

public record TitleElement(Text title) implements PageElement {
    private static final int MAX_WIDTH = PageGui.BOOK_WIDTH / 2 - 23;

    @Override
    public int getHeight(int maxWidth) {
        return MinecraftClient.getInstance().textRenderer.fontHeight + 10;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        x = x + 18;
        context.drawTexture(BOOK, x - 15, y + 7, 0, 180, MAX_WIDTH, 8, 512, 512);

        var textRenderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = textRenderer.getWidth(title);
        int centerX = x + 43;

        if (textWidth <= MAX_WIDTH - 8) {
            context.drawCenteredTextWithShadow(textRenderer, title, centerX, y, 0xFFD700);
        } else {
            float scale = (float) (MAX_WIDTH - 8) / textWidth;
            context.getMatrices().push();
            context.getMatrices().translate(centerX, y, 0);
            context.getMatrices().scale(scale, scale, 1.0f);
            context.drawCenteredTextWithShadow(textRenderer, title, 0, 0, 0xFFD700);
            context.getMatrices().pop();
        }
    }
}