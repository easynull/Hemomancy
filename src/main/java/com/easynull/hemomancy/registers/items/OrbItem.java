package com.easynull.hemomancy.registers.items;

import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.utils.EnergyUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class OrbItem extends Item implements LpElement {
    final long maxLp;
    final byte tier;

    public OrbItem(Properties prop, int tier, long maxLp) {
        super(prop.stacksTo(1));
        this.tier = (byte) tier;
        this.maxLp = maxLp;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        RandomSource rand = RandomSource.create();
        int lp = rand.nextInt(150, 200);
        if(reducerLp(lp, player.getItemInHand(hand))) {
            EnergyUtils.damageLp(player, lp);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    public byte getTier() {
        return tier;
    }

    public long getMaxLp() {
        return maxLp;
    }
}
