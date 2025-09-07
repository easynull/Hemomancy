package com.easynull.hemomancy.registers.blocks.type;

import com.easynull.hemomancy.registers.HcBlockEntities;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PedestalBE extends ContainerBlockEntity {
    public PedestalBE(BlockPos pos, BlockState state) {
        super(HcBlockEntities.pedestal.get(), pos, state, 1, 64);
    }
}
