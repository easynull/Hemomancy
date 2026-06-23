package ru.easynull.hemomancy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.renderer.RenderType;
import ru.easynull.hemomancy.net.NetHandlerClient;
import ru.easynull.hemomancy.proxy.ClientProxy;
import ru.easynull.hemomancy.proxy.MainProxy;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.HmFluids;
import ru.easynull.hemomancy.render.block.AlchemyTableRenderer;
import ru.easynull.hemomancy.render.block.BloodAltarRenderer;
import ru.easynull.hemomancy.render.hud.BookHud;

public final class HemomancyClient implements ClientModInitializer {
    public static int tickClient;

    @Override
    public void onInitializeClient() {
        MainProxy.setProxy(new ClientProxy());
        NetHandlerClient.onInit();
        onRender();
        onEvents();
    }

    private static void onRender() {
        FluidRenderHandlerRegistry.INSTANCE.register(HmFluids.BLOOD, HmFluids.FLOWING_BLOOD,
                new SimpleFluidRenderHandler(Hemomancy.path("block/blood_still"), Hemomancy.path("block/blood_flowing"), 0xFFFFFF)
        );

        BlockRenderLayerMap.INSTANCE.putBlock(HmBlocks.ALCHEMY_TABLE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), HmFluids.BLOOD, HmFluids.FLOWING_BLOOD);

        BlockEntityRendererRegistry.register(HmBlockEntities.ALCHEMY_TABLE, AlchemyTableRenderer::new);
        BlockEntityRendererRegistry.register(HmBlockEntities.BLOOD_ALTAR, BloodAltarRenderer::new);
    }

    private static void onEvents() {
        HudRenderCallback.EVENT.register((ctx, delta) -> {
            BookHud.onRender(ctx);
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused()) tickClient++;
        });
    }
}
