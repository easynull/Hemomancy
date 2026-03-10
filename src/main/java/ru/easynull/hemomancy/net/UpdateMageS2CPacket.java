package ru.easynull.hemomancy.net;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import ru.easynull.hemomancy.Hemomancy;

public record UpdateMageS2CPacket(NbtCompound nbt) implements FabricPacket {
    public static final PacketType<UpdateMageS2CPacket> ID = PacketType.create(Hemomancy.path("update_mage"), UpdateMageS2CPacket::read);

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeNbt(nbt());
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }

    public static UpdateMageS2CPacket read(PacketByteBuf buf){
        NbtCompound nbt = buf.readNbt();
        return new UpdateMageS2CPacket(nbt);
    }
}
