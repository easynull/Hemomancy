package com.easynull.hemomancy.core.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.easynull.hemomancy.Hemomancy.ID;
import static com.mw.nullcore.core.network.NetworkHandler.registerClientChannel;
import static com.mw.nullcore.core.network.NetworkHandler.registerDualChannel;

@EventBusSubscriber(modid = ID, bus = EventBusSubscriber.Bus.MOD)
public final class Packets {
    @SubscribeEvent
    public static void init(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registerClientChannel(registrar, AlchemyProgressPacket.TYPE, AlchemyProgressPacket.CODEC);
    }
}
