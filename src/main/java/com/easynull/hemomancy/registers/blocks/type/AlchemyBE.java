package com.easynull.hemomancy.registers.blocks.type;

import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.core.Wandable;
import com.easynull.hemomancy.registers.HcBlockEntities;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import com.mw.nullcore.core.blocks.type.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public final class AlchemyBE extends ContainerBlockEntity implements Tickable, LpElement, Wandable {
    long lp;
    String mode = getModes()[0];

    public AlchemyBE(BlockPos pos, BlockState state) {
        super(HcBlockEntities.alchemy.get(), pos, state, 24, 64);
    }

    @Override
    public void tick() {

    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        lp = tag.getLong("lp");
        mode = tag.getString("mode");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("lp", lp);
        tag.putString("mode", mode);
    }

    @Override
    public String[] getModes() {
        return new String[]{"crafting"};
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public void setMode(String mode) {
        this.mode = mode;
    }
}
