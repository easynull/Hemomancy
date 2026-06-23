package ru.easynull.hemomancy.net;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UpdateAlchemyS2CPacket(BlockPos pos, long progress, long needLP, boolean crafting) implements CustomPacketPayload {

    public static final Type<UpdateAlchemyS2CPacket> ID = new Type<>(ResourceLocation.fromNamespaceAndPath("hemomancy", "update_alchemy_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateAlchemyS2CPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UpdateAlchemyS2CPacket::pos,
            ByteBufCodecs.VAR_LONG, UpdateAlchemyS2CPacket::progress,
            ByteBufCodecs.VAR_LONG, UpdateAlchemyS2CPacket::needLP,
            ByteBufCodecs.BOOL, UpdateAlchemyS2CPacket::crafting,
            UpdateAlchemyS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}