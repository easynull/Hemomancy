package ru.easynull.hemomancy.net;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import ru.easynull.hemomancy.Hemomancy;

public record OpenStatueGuiS2CPacket() implements FabricPacket {
    public static final PacketType<OpenStatueGuiS2CPacket> ID = PacketType.create(Hemomancy.path("open_statue_gui"), buf -> new OpenStatueGuiS2CPacket());

    @Override
    public void write(PacketByteBuf buf) {
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
