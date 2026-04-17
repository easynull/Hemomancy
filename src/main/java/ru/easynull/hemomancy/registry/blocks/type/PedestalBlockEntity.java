package ru.easynull.hemomancy.registry.blocks.type;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import ru.easynull.hemomancy.api.InventoryBlockEntity;
import ru.easynull.hemomancy.registry.HmBlockEntities;

public final class PedestalBlockEntity extends InventoryBlockEntity {
    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(HmBlockEntities.BLOOD_ALTAR, pos, state, 1, 64);
    }
}