package ru.easynull.hemomancy.api.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import ru.easynull.hemomancy.api.SyncBlockEntity;
import ru.easynull.hemomancy.registry.HmDataComponents;

public interface LpElement {
    default long getLp(Object target) {
        target = getRealTarget() == null ? target : getRealTarget();

        if (target instanceof ItemStack stack) {
            return stack.getOrDefault(HmDataComponents.LP, 0L);
        }
        else if (target instanceof BlockEntity be) {
            return be.saveWithoutMetadata(be.getLevel().registryAccess()).getLong("LP");
        }
        return 0L;
    }

    default long getMaxLp() {
        return 0L;
    }

    default boolean reduceLp(long amount, Object target) {
        if (getMaxLp() <= 0) return false;

        long current = getLp(target);
        long newAmount = Mth.clamp(current + amount, 0L, getMaxLp());
        if (target instanceof ItemStack stack) {
            stack.set(HmDataComponents.LP, newAmount);
        } else if (target instanceof BlockEntity be) {
            CompoundTag nbt = be.saveWithoutMetadata(be.getLevel().registryAccess());
            nbt.putLong("LP", newAmount);
            be.loadCustomOnly(nbt, be.getLevel().registryAccess());
            SyncBlockEntity.sync(be);
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
