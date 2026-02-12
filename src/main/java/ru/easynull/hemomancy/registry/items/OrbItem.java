package ru.easynull.hemomancy.registry.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ru.easynull.hemomancy.Hemomancy;
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

    public OrbItem(Settings settings, int tier, long maxLp, int bonus) {
        super(settings.maxCount(1));
        this.tier = (byte) tier;
        this.maxLp = maxLp;
        this.bonus = bonus;
    }

    public OrbItem(int tier, long maxLp, int bonus) {
        this(new Settings(), tier, maxLp, bonus);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        Random rand = new Random();
        long lp = rand.nextInt(100, 150);

        if (player.isSneaking() && player.isCreative()) {
            lp = getMaxLp();
        }

        if (reduceLp(lp * bonus, stack)) {
            EnergyUtils.damageLp(player, lp);
            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.pass(stack);
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
        if (target instanceof ItemStack stack && stack.isOf(HmItems.INFINITY_BLOOD_ORB)) {
            return getMaxLp();
        }
        return LpElement.super.getLp(target);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        String value = stack.isOf(HmItems.INFINITY_BLOOD_ORB) ? "∞" : String.valueOf(getMaxLp());
        tooltip.add(Text.translatable("tooltip.hemomancy.orb.desc", value).formatted(Formatting.GRAY));
    }
}