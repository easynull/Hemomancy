package ru.easynull.hemomancy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.RenderLayer;
import ru.easynull.hemomancy.net.NetHandlerClient;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.render.huds.ControllerHud;
import ru.easynull.hemomancy.render.types.AlchemyTableRenderer;
import ru.easynull.hemomancy.render.types.BloodAltarRenderer;

public final class HemomancyClient implements ClientModInitializer {
    public static int tickClient;

    @Override
    public void onInitializeClient() {
        NetHandlerClient.onInit();
        onRenderInit();
        onEvents();
    }

    private static void onRenderInit() {
        BlockRenderLayerMap.INSTANCE.putBlock(HmBlocks.ALCHEMY_TABLE, RenderLayer.getCutout());

        BlockEntityRendererRegistry.register(HmBlockEntities.ALCHEMY_TABLE, AlchemyTableRenderer::new);
        BlockEntityRendererRegistry.register(HmBlockEntities.BLOOD_ALTAR, BloodAltarRenderer::new);
    }

    private static void onEvents() {
        HudRenderCallback.EVENT.register((ctx, delta) -> {
            ControllerHud.onRenderControllerHud(ctx);
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused()) tickClient++;
        });
    }
}
