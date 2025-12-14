package com.easynull.hemomancy.registers.items.sigil;

import com.easynull.hemomancy.utils.EnergyUtils;
import com.mw.nullcore.core.NcUtils;
import com.mw.nullcore.registers.NcComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class EnSigilItem extends SigilItem {
    final int rollback;

    public EnSigilItem(Properties props, Context ctx, int lp, int rollback) {
        super(props.component(NcComponents.ENABLED, false), ctx, lp);
        this.rollback = rollback;
    }

    public EnSigilItem(Properties props, Context ctx, int lp) {
        this(props, ctx, lp, NcUtils.Mth.secondTick(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player) {
            ctx.action(new Consumer(level, player.blockPosition(), player.getDirection(), player, stack, this));
            if(level.getGameTime() % rollback == 0 && isActive(stack) && shouldLP){
                EnergyUtils.extractLp(player, lp);
            }
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        setActive(player.getItemInHand(hand), !isActive(player.getItemInHand(hand)));
        return InteractionResult.CONSUME;
    }

    public boolean isActive(ItemStack stack){
        return stack.get(NcComponents.ENABLED);
    }

    public void setActive(ItemStack stack, boolean active){
        stack.set(NcComponents.ENABLED, active);
    }
}
