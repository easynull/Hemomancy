package ru.easynull.hemomancy.api.energy;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import ru.easynull.hemomancy.utils.HmCommonUtils;

public interface LpElement {
    default long getLp(Object target) {
        target = getRealTarget() == null ? target : getRealTarget();

        if (target instanceof ItemStack stack) {
            NbtCompound nbt = stack.getNbt();
            return nbt != null ? nbt.getLong("LP") : 0L;
        }
        else if (target instanceof BlockEntity be) {
            return be.createNbt().getLong("LP");
        }
        return 0L;
    }

    default long getMaxLp() {
        return 0L;
    }

    default boolean reduceLp(long amount, Object target) {
        if (getMaxLp() <= 0) return false;

        long current = getLp(target);
        long newAmount = (long) MathHelper.clamp(current + amount, 0L, getMaxLp());
        if (target instanceof ItemStack stack) {
            NbtCompound nbt = stack.getOrCreateNbt();
            nbt.putLong("LP", newAmount);
        } else if (target instanceof BlockEntity be) {
            NbtCompound nbt = be.createNbt();
            nbt.putLong("LP", newAmount);
            be.readNbt(nbt);
            HmCommonUtils.updateBlockEntity(be);
        }

        return newAmount != getMaxLp();
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
