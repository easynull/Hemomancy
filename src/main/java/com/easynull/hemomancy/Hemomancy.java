package com.easynull.hemomancy;

import com.easynull.hemomancy.registers.*;
import com.mojang.logging.LogUtils;
import com.easynull.hemomancy.core.altar.Tier;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(Hemomancy.ID)
public final class Hemomancy {
    public static final String ID = "hemomancy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Hemomancy(IEventBus bus) {
        bus.addListener(this::common);
        HcElements.init(bus);
        HcComponents.components.register(bus);
        HcBlockEntities.types.register(bus);
        HcMenus.menus.register(bus);
        HcRecipes.init(bus);
    }

    public void common(final FMLCommonSetupEvent event){
        Tier.init();
    }

    public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
