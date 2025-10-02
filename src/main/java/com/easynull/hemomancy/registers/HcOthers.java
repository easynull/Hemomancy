package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.client.render.type.AlchemyTableRender;
import com.easynull.hemomancy.client.render.type.AltarRender;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;

import static com.easynull.hemomancy.Hemomancy.ID;

@EventBusSubscriber(modid = ID, bus = EventBusSubscriber.Bus.MOD)
public final class HcOthers {
    @SubscribeEvent
    public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(HcBlockEntities.bloodAltar.get(), AltarRender::new);
        event.registerBlockEntityRenderer(HcBlockEntities.alchemyTable.get(), AlchemyTableRender::new);
    }

    @SubscribeEvent
    public static void registerModelCondition(RegisterConditionalItemModelPropertyEvent event) {
        event.register(Hemomancy.path("active"), HcComponents.Active.codec);
    }
}
