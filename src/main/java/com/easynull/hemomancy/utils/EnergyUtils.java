package com.easynull.hemomancy.utils;

import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.core.Tierable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class EnergyUtils {
    public static float calculateDamage(LivingEntity entity, long lp) {
        float diminishing = 0.8f;
        float damage = (float) (0.5f * Math.log1p(lp / 10.0));
        damage *= (float) Math.pow(damage, -diminishing * 0.1);
        return Math.min(damage, entity.getMaxHealth());
    }

    public static boolean damageLp(LivingEntity entity, long lp) {
        if (lp == 0) return false;
        float dm = calculateDamage(entity, lp);
        entity.hurt(entity.damageSources().generic(), dm);
        entity.playSound(SoundEvents.ALLAY_THROW);
        return true;
    }

    public static void extractLp(Player player, long lp) {
        long remainingLp = lp;

        for (ItemStack stack : player.getInventory().items) {
            if (remainingLp <= 0) break;

            if (stack.getItem() instanceof LpElement le) {
                long currentLp = le.getLp(stack);
                long toExtract = Math.min(currentLp, remainingLp);

                if (toExtract > 0) {
                    le.reducerLp(-toExtract, stack);
                    remainingLp -= toExtract;
                }
            }
        }
        if (remainingLp > 0) {
            damageLp(player, remainingLp);
        }
    }

    public static void extractInFrom(Object target, Object source, long amount, boolean inTarget) {
        if (!((source instanceof ItemStack s ? s.getItem() : source) instanceof LpElement sc)) return;
        if (!((target instanceof ItemStack s ? s.getItem() : target) instanceof LpElement tg)) return;
        tg.reducerLp(inTarget ? amount : -amount, target);
        sc.reducerLp(inTarget ? -amount : amount, source);
    }

    public static ItemStack getHighestTier(Player player) {
        ItemStack tierStack = ItemStack.EMPTY;
        int tier = 0;

        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof Tierable tierable) {
                int currentTier = tierable.getTier();

                if (currentTier > tier) {
                    tier = currentTier;
                    tierStack = stack;
                }
            }
        }
        return tierStack;
    }
}
