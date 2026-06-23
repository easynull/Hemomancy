package ru.easynull.hemomancy.registry.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.utils.EnergyUtils;
import ru.easynull.hemomancy.utils.HmCommonUtils;

import java.util.concurrent.atomic.AtomicLong;

public final class DaggerItem extends SwordItem {

    public DaggerItem(Properties settings) {
        super(Tiers.STONE, settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        var rand = player.getRandom();
        AtomicLong lp = new AtomicLong(rand.nextInt(18, 56));
        AtomicLong damageLp = new AtomicLong(lp.get());

        HmCommonUtils.forEachInCube(player.blockPosition(), 1, pos -> {
            if (level.getBlockEntity(pos) instanceof LpElement element && element.canDaggerFulled()) {
                Hemomancy.LOGGER.info("{}", 4);
                if (player.isCreative() && player.isShiftKeyDown()) {
                    lp.set(element.getMaxLp());
                }
                element.reduceLp(lp.get(), level.getBlockEntity(pos));
                damageLp.set(lp.get() + lp.get());
            }
        });

        EnergyUtils.damageLp(player, damageLp.get());
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level level = target.level();
        var rand = target.getRandom();
        AtomicLong lp = new AtomicLong(rand.nextInt(10, 42));
        HmCommonUtils.forEachInCube(target.blockPosition(), 1, pos -> {
            if (level.getBlockEntity(pos) instanceof LpElement element && element.canDaggerFulled()) {
                element.reduceLp(lp.get(), level.getBlockEntity(pos));
            }
        });
        target.playSound(SoundEvents.ALLAY_THROW, 1.0f, 1.0f);
        return true;
    }
}