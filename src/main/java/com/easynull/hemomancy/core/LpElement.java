package com.easynull.hemomancy.core;

import com.easynull.hemomancy.registers.HcComponents;
import com.mw.nullcore.core.NcUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface LpElement {
    default long getLp(Object target) {
        target = getRealTarget() == null ? target : getRealTarget();
        if (target instanceof ItemStack stack) return stack.getOrDefault(HcComponents.LP, 0L);
        else if (target instanceof BlockEntity be) return be.getPersistentData().getLong("LP");
        return 0;
    }

    default long getMaxLp() {
        return 0;
    }

    default boolean reducerLp(long amount, Object target) {
        if (getMaxLp() <= 0) return false;
        amount = Math.clamp(getLp(target) + amount, 0, getMaxLp());
        if (target instanceof ItemStack stack) {
            stack.set(HcComponents.LP, amount);
        } else if (target instanceof BlockEntity be) {
            CompoundTag nbt = be.getPersistentData();
            nbt.putLong("LP", amount);
            NcUtils.Block.updateBlockEntity(be);
        }
        return amount != getMaxLp();
    }

    default boolean canDaggerFulled(){
        return false;
    }

    default Object getRealTarget(){
        return null;
    }

    default ItemStack showedItem() {
        return ItemStack.EMPTY;
    }
}
