package ru.easynull.hemomancy.net;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import ru.easynull.hemomancy.Hemomancy;

public record UpdateAlchemyS2CPacket(BlockPos pos, long progress, long needLP, boolean crafting) implements FabricPacket {
    public static final PacketType<UpdateAlchemyS2CPacket> ID = PacketType.create(Hemomancy.path("update_alchemy_packet"), UpdateAlchemyS2CPacket::read);

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(pos());
        buf.writeLong(progress());
        buf.writeLong(needLP());
        buf.writeBoolean(crafting());
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }

    public static UpdateAlchemyS2CPacket read(PacketByteBuf buf){
        BlockPos pos = buf.readBlockPos();
        long progress = buf.readLong();
        long needLP = buf.readLong();
        boolean crafting = buf.readBoolean();
        return new UpdateAlchemyS2CPacket(pos, progress, needLP, crafting);
    }
}
