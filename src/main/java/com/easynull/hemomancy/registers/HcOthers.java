package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.client.render.type.AlchemyRender;
import com.easynull.hemomancy.client.render.type.AltarRender;
import com.easynull.hemomancy.client.render.type.PedestalRender;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.easynull.hemomancy.Hemomancy.ID;

@EventBusSubscriber(modid = ID, bus = EventBusSubscriber.Bus.MOD)
public final class HcOthers {
    @SubscribeEvent
    public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(HcBlockEntities.bloodAltar.get(), AltarRender::new);
        event.registerBlockEntityRenderer(HcBlockEntities.pedestal.get(), PedestalRender::new);
        event.registerBlockEntityRenderer(HcBlockEntities.alchemy.get(), AlchemyRender::new);
    }
}
