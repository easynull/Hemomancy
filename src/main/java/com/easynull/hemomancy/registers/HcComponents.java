package com.easynull.hemomancy.registers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mw.nullcore.registers.NcComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static com.easynull.hemomancy.Hemomancy.ID;

public final class HcComponents {
    public static final DeferredRegister.DataComponents components = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ID);

    public static Supplier<DataComponentType<Long>> LP = components.registerComponentType("LP", builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.LONG));
    public static Supplier<DataComponentType<ResourceKey<Level>>> DIMENSION = components.registerComponentType("dimension", builder -> builder.persistent(ResourceKey.codec(Registries.DIMENSION)).networkSynchronized(ResourceKey.streamCodec(Registries.DIMENSION)));

    public record Enabled() implements ConditionalItemModelProperty {
        public static final MapCodec<Enabled> CODEC = MapCodec.unit(new Enabled());
        @Override
        public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext ctx) {
            return stack.getOrDefault(NcComponents.ENABLED, false);
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return CODEC;
        }
    }
}
