package ru.easynull.hemomancy.net;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UpdateMageS2CPacket(CompoundTag nbt) implements CustomPacketPayload {
    public static final Type<UpdateMageS2CPacket> ID = new Type<>(ResourceLocation.fromNamespaceAndPath("hemomancy", "update_mage_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateMageS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.TRUSTED_COMPOUND_TAG, UpdateMageS2CPacket::nbt,
            UpdateMageS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}