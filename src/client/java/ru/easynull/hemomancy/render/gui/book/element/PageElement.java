package ru.easynull.hemomancy.render.gui.book.element;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public interface PageElement {
    int getHeight(int maxWidth);

    void render(GuiGraphics context, int x, int y, int mouseX, int mouseY, float delta);

    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    default boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    default boolean mouseScrolled(double mouseX, double mouseY, double horAmount, double vertAmount) {
        return false;
    }

    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    default ItemStack getTooltipStack(int relX, int relY){
        return null;
    }

    default void onClose(){}
}