package com.easynull.hemomancy.registers;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.easynull.hemomancy.Hemomancy.ID;

public final class HcComponents {
    public static final DeferredRegister.DataComponents components = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ID);

    public static Supplier<DataComponentType<Long>> lp = components.registerComponentType("lp", builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.LONG));
}
