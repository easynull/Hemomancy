package ru.easynull.hemomancy.render.gui.book.element;

import ru.easynull.hemomancy.render.gui.book.PageGui;

import static ru.easynull.hemomancy.render.gui.book.BookGui.BOOK;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public record TitleElement(Component title) implements PageElement {
    private static final int MAX_WIDTH = PageGui.BOOK_WIDTH / 2 - 23;

    @Override
    public int getHeight(int maxWidth) {
        return Minecraft.getInstance().font.lineHeight + 10;
    }

    @Override
    public void render(GuiGraphics context, int x, int y, int mouseX, int mouseY, float delta) {
        x = x + 18;
        context.blit(BOOK, x - 15, y + 7, 0, 180, MAX_WIDTH, 8, 512, 512);

        var textRenderer = Minecraft.getInstance().font;
        int textWidth = textRenderer.width(title);
        int centerX = x + 43;

        if (textWidth <= MAX_WIDTH - 8) {
            context.drawCenteredString(textRenderer, title, centerX, y, 0xFFD700);
        } else {
            float scale = (float) (MAX_WIDTH - 8) / textWidth;
            context.pose().pushPose();
            context.pose().translate(centerX, y, 0);
            context.pose().scale(scale, scale, 1.0f);
            context.drawCenteredString(textRenderer, title, 0, 0, 0xFFD700);
            context.pose().popPose();
        }
    }
}