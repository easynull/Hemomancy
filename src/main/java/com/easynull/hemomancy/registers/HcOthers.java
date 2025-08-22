package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.client.render.type.AltarRender;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static com.easynull.hemomancy.Hemomancy.ID;

@EventBusSubscriber(modid = ID, bus = EventBusSubscriber.Bus.MOD)
public final class HcOthers {
    public static void registerScreens(RegisterMenuScreensEvent event) {
//        event.register(HcMenus.menu.get(), Screen::new);
    }
    @SubscribeEvent
    public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(HcBlockEntities.bloodAltar.get(), AltarRender::new);
    }
}
