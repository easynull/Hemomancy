package ru.easynull.hemomancy.registry.blocks.type;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import ru.easynull.hemomancy.api.ContainerBlockEntity;
import ru.easynull.hemomancy.registry.HmBlockEntities;

public final class PedestalBlockEntity extends ContainerBlockEntity {
    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(HmBlockEntities.BLOOD_ALTAR, pos, state, 1, 64);
    }
}