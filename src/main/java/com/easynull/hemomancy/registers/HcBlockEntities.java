package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.registers.blocks.type.*;
import com.mw.nullcore.core.holders.OuterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

import static com.easynull.hemomancy.Hemomancy.ID;

public final class HcBlockEntities {
    public static final OuterBlockEntity types = OuterBlockEntity.create(ID);

    public static final Supplier<BlockEntityType<AltarBE>> bloodAltar = types.register("blood_altar", ()-> new BlockEntityType<>(AltarBE::new, HcElements.bloodAltar.get()));
    public static final Supplier<BlockEntityType<PedestalBE>> pedestal = types.register("pedestal", ()-> new BlockEntityType<>(PedestalBE::new, HcElements.pedestal.get()));
    public static final Supplier<BlockEntityType<AlchemyBE>> alchemy = types.register("alchemy", ()-> new BlockEntityType<>(AlchemyBE::new, HcElements.alchemy.get()));
}
