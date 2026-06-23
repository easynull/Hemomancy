package ru.easynull.hemomancy.net;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UpdateNbtC2SPacket(int slot, CompoundTag nbt) implements CustomPacketPayload {

    public static final Type<UpdateNbtC2SPacket> ID = new Type<>(ResourceLocation.fromNamespaceAndPath("hemomancy", "update_nbt_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateNbtC2SPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, UpdateNbtC2SPacket::slot,
            ByteBufCodecs.COMPOUND_TAG, UpdateNbtC2SPacket::nbt,
            UpdateNbtC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}