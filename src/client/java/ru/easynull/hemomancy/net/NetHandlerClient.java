package ru.easynull.hemomancy.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.registry.blocks.type.AlchemyTableBlockEntity;
import ru.easynull.hemomancy.render.gui.MageStatueGui;

public final class NetHandlerClient {
    public static void onInit() {
        //ClientPlayNetworking.registerGlobalReceiver(UpdateAlchemyS2CPacket.ID, NetHandlerClient::handleUpdateAlchemy);
        ClientPlayNetworking.registerGlobalReceiver(UpdateMageS2CPacket.ID, NetHandlerClient::handleUpdateMage);
        ClientPlayNetworking.registerGlobalReceiver(OpenStatueGuiS2CPacket.ID, NetHandlerClient::handleOpenGui);
    }

    private static void handleUpdateAlchemy(UpdateAlchemyS2CPacket packet, ClientPlayNetworking.Context context) {
        BlockEntity be = context.player().level().getBlockEntity(packet.pos());
        if (be instanceof AlchemyTableBlockEntity alchemy) {
            if (alchemy.needLP != packet.needLP()) alchemy.needLP = packet.needLP();
            if (alchemy.progress != packet.progress()) alchemy.progress = packet.progress();
            if (alchemy.crafting != packet.crafting()) alchemy.crafting = packet.crafting();
        }
    }

    private static void handleUpdateMage(UpdateMageS2CPacket packet, ClientPlayNetworking.Context context) {
        var data = MagePlayer.of(context.player());
        data.updateData(packet.nbt());
    }

    private static void handleOpenGui(OpenStatueGuiS2CPacket packet, ClientPlayNetworking.Context context) {
        Minecraft.getInstance().setScreen(new MageStatueGui(context.player()));
    }
}
