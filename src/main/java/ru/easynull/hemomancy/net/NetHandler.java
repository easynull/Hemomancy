package ru.easynull.hemomancy.net;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class NetHandler {
    public static void onInit() {
        PayloadTypeRegistry.playC2S().register(UpdateNbtC2SPacket.ID, UpdateNbtC2SPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(OpenStatueGuiS2CPacket.ID, OpenStatueGuiS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateMageS2CPacket.ID, UpdateMageS2CPacket.STREAM_CODEC);


        ServerPlayNetworking.registerGlobalReceiver(UpdateNbtC2SPacket.ID, NetHandler::handleUpdateNbt);
    }

    private static void handleUpdateNbt(UpdateNbtC2SPacket packet, ServerPlayNetworking.Context context) {
        ItemStack stack = context.player().getInventory().getItem(packet.slot());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(packet.nbt()));
    }
}
