package ru.easynull.hemomancy.net;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import ru.easynull.hemomancy.Hemomancy;

public record UpdateNbtC2SPacket(int slot, NbtCompound nbt) implements FabricPacket {
    public static final PacketType<UpdateNbtC2SPacket> ID = PacketType.create(Hemomancy.path("update_nbt_packet"), UpdateNbtC2SPacket::read);

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(slot());
        buf.writeNbt(nbt());
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }

    public static UpdateNbtC2SPacket read(PacketByteBuf buf){
        int slot = buf.readInt();
        NbtCompound nbt = buf.readNbt();
        return new UpdateNbtC2SPacket(slot, nbt);
    }
}
