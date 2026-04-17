package ru.easynull.hemomancy.proxy;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import ru.easynull.hemomancy.render.gui.book.BookGui;

public final class ClientProxy implements Proxy{
    @Override
    public void openBook(ItemStack stack) {
        MinecraftClient.getInstance().setScreen(new BookGui());
    }
}
