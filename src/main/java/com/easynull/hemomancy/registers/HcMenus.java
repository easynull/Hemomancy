package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.Hemomancy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HcMenus {
    public static final DeferredRegister<MenuType<?>> menus = DeferredRegister.create(BuiltInRegistries.MENU, Hemomancy.ID);
}
