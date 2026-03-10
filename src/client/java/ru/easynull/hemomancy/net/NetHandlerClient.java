package ru.easynull.hemomancy.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.registry.blocks.type.AlchemyTableBE;
import ru.easynull.hemomancy.render.gui.MageStatueGui;

public final class NetHandlerClient {
    public static void onInit() {
        ClientPlayNetworking.registerGlobalReceiver(UpdateAlchemyS2CPacket.ID, NetHandlerClient::handleUpdateAlchemy);
        ClientPlayNetworking.registerGlobalReceiver(UpdateMageS2CPacket.ID, NetHandlerClient::handleUpdateMage);
        ClientPlayNetworking.registerGlobalReceiver(OpenStatueGuiS2CPacket.ID, NetHandlerClient::handleOpenGui);
    }

    private static void handleUpdateAlchemy(UpdateAlchemyS2CPacket packet, ClientPlayerEntity player, PacketSender sender) {
        BlockEntity be = player.getWorld().getBlockEntity(packet.pos());
        if (be instanceof AlchemyTableBE alchemy) {
            if (alchemy.needLP != packet.needLP()) alchemy.needLP = packet.needLP();
            if (alchemy.progress != packet.progress()) alchemy.progress = packet.progress();
            if (alchemy.crafting != packet.crafting()) alchemy.crafting = packet.crafting();
        }
    }

    private static void handleUpdateMage(UpdateMageS2CPacket packet, ClientPlayerEntity player, PacketSender sender) {
        var data = MagePlayer.of(player);
        data.updateData(packet.nbt());
    }

    private static void handleOpenGui(OpenStatueGuiS2CPacket packet, ClientPlayerEntity player, PacketSender sender) {
        MinecraftClient.getInstance().setScreen(new MageStatueGui(player));
    }
}
