package ru.easynull.hemomancy.registry.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.registry.HmItems;
import ru.easynull.hemomancy.utils.EnergyUtils;

import java.util.List;
import java.util.Random;

public final class OrbItem extends Item implements LpElement, Tierable {
    private final long maxLp;
    private final byte tier;
    private final int bonus;

    public OrbItem(Properties settings, int tier, long maxLp, int bonus) {
        super(settings.stacksTo(1));
        this.tier = (byte) tier;
        this.maxLp = maxLp;
        this.bonus = bonus;
    }

    public OrbItem(int tier, long maxLp, int bonus) {
        this(new Properties(), tier, maxLp, bonus);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        Random rand = new Random();
        long lp = rand.nextInt(100, 150);

        if (player.isShiftKeyDown() && player.isCreative()) {
            lp = getMaxLp();
        }

        if (reduceLp(lp * bonus, stack)) {
            EnergyUtils.damageLp(player, lp);
            return InteractionResultHolder.consume(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return stack.copy();
    }

    @Override
    public byte getTier() {
        return tier;
    }

    @Override
    public long getMaxLp() {
        return maxLp;
    }

    @Override
    public long getLp(Object target) {
        if (target instanceof ItemStack stack && stack.is(HmItems.INEXHAUSTIBLE_BLOOD_ORB)) {
            return getMaxLp();
        }
        return LpElement.super.getLp(target);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        String value = stack.is(HmItems.INEXHAUSTIBLE_BLOOD_ORB) ? "∞" : String.valueOf(getMaxLp());
        tooltip.add(Component.translatable("tooltip.hemomancy.orb.desc", value).withStyle(ChatFormatting.GRAY));
    }
}