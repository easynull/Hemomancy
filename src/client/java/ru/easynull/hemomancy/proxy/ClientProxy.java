package ru.easynull.hemomancy.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import ru.easynull.hemomancy.render.gui.book.BookGui;
import ru.easynull.hemomancy.proxy.Proxy;

public final class ClientProxy implements Proxy {
    @Override
    public void openBook(ItemStack stack) {
        Minecraft.getInstance().setScreen(new BookGui());
    }
}
