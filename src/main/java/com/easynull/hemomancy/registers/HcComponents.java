package com.easynull.hemomancy.registers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.easynull.hemomancy.Hemomancy.ID;

public final class HcComponents {
    public static final DeferredRegister.DataComponents components = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ID);

    public static Supplier<DataComponentType<Long>> lp = components.registerComponentType("lp", builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.LONG));
    public static Supplier<DataComponentType<Boolean>> active = components.registerComponentType("active", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    public static Supplier<DataComponentType<BlockState>> blockState = components.registerComponentType("block_state", builder -> builder.persistent(BlockState.CODEC).networkSynchronized(ByteBufCodecs.fromCodec(BlockState.CODEC)));
    public static Supplier<DataComponentType<BlockPos>> pos = components.registerComponentType("block_pos", builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));
}
