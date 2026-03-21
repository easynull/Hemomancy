package ru.easynull.hemomancy.net;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public final class NetHandler {
    public static void onInit() {
        ServerPlayNetworking.registerGlobalReceiver(UpdateNbtC2SPacket.ID, NetHandler::handleUpdateNbt);
    }

    private static void handleUpdateNbt(UpdateNbtC2SPacket packet, ServerPlayerEntity player, PacketSender sender) {
        ItemStack stack = player.getInventory().getStack(packet.slot());
        stack.setNbt(packet.nbt());
    }
}
