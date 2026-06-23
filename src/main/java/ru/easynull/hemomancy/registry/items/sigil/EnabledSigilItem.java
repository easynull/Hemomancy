package ru.easynull.hemomancy.registry.items.sigil;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import ru.easynull.hemomancy.registry.HmDataComponents;
import ru.easynull.hemomancy.utils.EnergyUtils;

public final class EnabledSigilItem extends SigilItem {
    private final int rollbackTicks, rollbackPayTicks;

    public EnabledSigilItem(Properties settings, Context action, int lpCost, int rollbackTicks, int rollbackPayTicks) {
        super(settings.component(HmDataComponents.ENABLED, false), action, lpCost);
        this.rollbackTicks = rollbackTicks;
        this.rollbackPayTicks = rollbackPayTicks;
    }

    public EnabledSigilItem(Properties settings, Context action, int lpCost, int rollbackPayTicks) {
        this(settings, action, lpCost, 20, rollbackPayTicks);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof Player player) || !isEnabled(stack)) return;
        if (level.getGameTime() % rollbackTicks == 0) action.perform(new SigilContext(level, player.blockPosition(), player.getDirection(), player, stack, this));
        if (!level.isClientSide && consumeLp) {
            if (level.getGameTime() % rollbackPayTicks == 0) {
                EnergyUtils.extractLp(player, lpCost);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean wasActive = isEnabled(stack);
        setEnabled(stack, !wasActive);
        return InteractionResultHolder.consume(stack);
    }

    public static boolean isEnabled(ItemStack stack) {
        return stack.get(HmDataComponents.ENABLED);
    }

    public static void setEnabled(ItemStack stack, boolean value) {
        stack.update(HmDataComponents.ENABLED, false, b -> value);
    }
}