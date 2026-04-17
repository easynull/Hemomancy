package ru.easynull.hemomancy.proxy;

import net.minecraft.item.ItemStack;

public interface Proxy {
    default void openBook(ItemStack stack){}
}
