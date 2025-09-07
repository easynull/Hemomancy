package com.easynull.hemomancy.registers.items.sigil;

import com.easynull.hemomancy.registers.HcComponents;
import com.easynull.hemomancy.utils.EnergyUtils;
import com.mw.nullcore.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class TickSigilItem extends SigilItem {
    final int rollback;

    public TickSigilItem(Properties props, Context context, int lp, int rollback, Component... additionalTooltip) {
        super(props, context, lp, additionalTooltip);
        this.rollback = rollback;
    }

    public TickSigilItem(Properties props, Context context, int lp, Component... additionalTooltip) {
        this(props, context, lp, Utils.Mth.secondTick(1), additionalTooltip);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player) {
            context.use(new Consumer(level, player.blockPosition(), player, stack, this));
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
        return stack.getOrDefault(HcComponents.active, false);
    }

    public void setActive(ItemStack stack, boolean active){
        stack.set(HcComponents.active, active);
    }
}
