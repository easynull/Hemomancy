package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.registers.blocks.type.AltarBE;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.easynull.hemomancy.Hemomancy.ID;

public final class HcBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> types = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ID);

    public static final Supplier<BlockEntityType<AltarBE>> bloodAltar = types.register("blood_altar", ()-> new BlockEntityType<>(AltarBE::new, HcElements.bloodAltar.get()));
}
