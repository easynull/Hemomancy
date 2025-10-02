package com.easynull.hemomancy.registers.items;

import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.utils.EnergyUtils;
import com.mw.nullcore.Utils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.concurrent.atomic.AtomicLong;

public final class DaggerItem extends Item {
    public DaggerItem(Properties prop) {
        super(prop.stacksTo(1));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        RandomSource rand = RandomSource.create();
        AtomicLong lp = new AtomicLong(rand.nextInt(85, 100));
        AtomicLong damageLp = new AtomicLong(lp.get());
        Utils.Block.forEachCube(player.blockPosition(), 1, pos -> {
            if (level.getBlockEntity(pos) instanceof LpElement oth && oth.canDaggerFulled()) {
                if(player.isCreative() && player.isShiftKeyDown()) lp.set(oth.getMaxLp());
                oth.reducerLp(lp.get(), oth);
                damageLp.set(lp.get() + lp.get());
            }
        });
        EnergyUtils.damageLp(player, damageLp.get());
        return InteractionResult.FAIL;
    }
}
