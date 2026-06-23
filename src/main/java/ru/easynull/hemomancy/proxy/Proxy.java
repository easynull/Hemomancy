package ru.easynull.hemomancy.proxy;

import net.minecraft.world.item.ItemStack;

public interface Proxy {
    default void openBook(ItemStack stack){}
}
