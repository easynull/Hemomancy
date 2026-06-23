package ru.easynull.hemomancy.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import ru.easynull.hemomancy.Hemomancy;

public record OpenStatueGuiS2CPacket() implements CustomPacketPayload {
    public static final Type<OpenStatueGuiS2CPacket> ID = new Type<>(Hemomancy.path("open_statue_gui_packet"));
    public static final StreamCodec<FriendlyByteBuf, OpenStatueGuiS2CPacket> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {},
            buf -> new OpenStatueGuiS2CPacket()
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}