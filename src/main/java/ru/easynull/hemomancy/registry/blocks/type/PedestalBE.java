package ru.easynull.hemomancy.registry.blocks.type;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import ru.easynull.hemomancy.api.InventoryBE;
import ru.easynull.hemomancy.registry.HmBlockEntities;

public final class PedestalBE extends InventoryBE {
    public PedestalBE(BlockPos pos, BlockState state) {
        super(HmBlockEntities.BLOOD_ALTAR, pos, state, 1, 64);
    }
}