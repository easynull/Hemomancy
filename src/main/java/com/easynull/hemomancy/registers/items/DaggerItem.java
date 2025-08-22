package com.easynull.hemomancy.registers.items;

import com.easynull.hemomancy.registers.blocks.type.AltarBE;
import com.easynull.hemomancy.utils.EnergyUtils;
import com.mw.nullcore.Utils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public final class DaggerItem extends Item {
    public DaggerItem(Properties prop) {
        super(prop.stacksTo(1));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        RandomSource rand = RandomSource.create();
        int lp = rand.nextInt(150, 200);
        Utils.Block.forEachCube(player.blockPosition(), 2, pos -> {
            if(level.getBlockEntity(pos) instanceof AltarBE altar){
                altar.reducerLp(lp, altar);
            }
        });
        EnergyUtils.damageLp(player, lp);
        return InteractionResult.CONSUME;
    }
}
