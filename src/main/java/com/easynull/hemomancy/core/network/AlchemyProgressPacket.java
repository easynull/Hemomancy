package com.easynull.hemomancy.core.network;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.registers.blocks.type.AlchemyTableBE;
import com.mw.nullcore.core.network.ClientChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AlchemyProgressPacket(BlockPos pos, long progress, boolean crafting, long needLP) implements ClientChannel<AlchemyProgressPacket> {
    public static final Type<AlchemyProgressPacket> TYPE = new Type<>(Hemomancy.path("alchemy_progress"));
    public static final StreamCodec<FriendlyByteBuf, AlchemyProgressPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, AlchemyProgressPacket::pos, ByteBufCodecs.LONG, AlchemyProgressPacket::progress, ByteBufCodecs.BOOL, AlchemyProgressPacket::crafting, ByteBufCodecs.LONG, AlchemyProgressPacket::needLP, AlchemyProgressPacket::new);

    @Override
    public @NotNull Type<AlchemyProgressPacket> type() {
        return TYPE;
    }

    @Override
    public void handleClient(AlchemyProgressPacket packet, IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(packet.pos()) instanceof AlchemyTableBE alchemy) {
            alchemy.progress = packet.progress();
            alchemy.crafting = packet.crafting();
            alchemy.needLP = packet.needLP();
        }
    }
}
