package com.easynull.hemomancy;

import com.easynull.hemomancy.registers.*;
import com.mojang.logging.LogUtils;
import com.easynull.hemomancy.core.altar.Tier;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(Hemomancy.ID)
public final class Hemomancy {
    public static final String ID = "hemomancy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Hemomancy(IEventBus bus) {
        bus.addListener(this::common);
        bus.addListener(this::client);
        HcElements.init(bus);
        HcComponents.components.register(bus);
        HcBlockEntities.types.register(bus);
        HcRecipes.init(bus);
        HcConfig.init();
    }

    public void common(final FMLCommonSetupEvent event){
        Tier.init();
    }

    public void client(final FMLClientSetupEvent event){
        ItemBlockRenderTypes.setRenderLayer(HcElements.alchemyTable.get(), RenderType.cutout());
    }

    public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
    public static ResourceLocation textures(String path) {
        return path("textures/" + path + ".png");
    }
}
