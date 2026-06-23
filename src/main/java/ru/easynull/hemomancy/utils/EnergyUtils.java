package ru.easynull.hemomancy.utils;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.api.energy.Tierable;

public final class EnergyUtils {
    public static float calculateDamage(LivingEntity entity, long lp) {
        if (lp <= 0) return 0f;
        float diminishing = 0.8f;
        float damage = (float) (0.5f * Math.log1p(lp));
        damage *= (float) Math.pow(damage, -diminishing * 0.1);
        return Math.min(damage, entity.getMaxHealth());
    }

    public static void damageLp(LivingEntity entity, long lp) {
        if (lp <= 0) return;
        float dm = calculateDamage(entity, lp);
        entity.hurt(entity.damageSources().generic(), dm);
        entity.playSound(SoundEvents.ALLAY_THROW, 1.0f, 1.0f);
    }

    public static void extractLp(Player player, long lp) {
        if (lp <= 0) return;
        long remainingLp = lp;
        for (ItemStack stack : player.getInventory().items) {
            if (remainingLp <= 0) break;
            if (stack.getItem() instanceof LpElement le) {
                long currentLp = le.getLp(stack);
                long toExtract = Math.min(currentLp, remainingLp);
                if (toExtract > 0) {
                    le.reduceLp(-toExtract, stack);
                    remainingLp -= toExtract;
                }
            }
        }
        if (remainingLp > 0) damageLp(player, remainingLp);
    }

    public static void extractInFrom(Object target, Object source, long amount, boolean inTarget) {
        if (amount <= 0) return;
        LpElement sc = getLpElement(source);
        LpElement tg = getLpElement(target);
        if (sc == null || tg == null) return;
        long sourceLp = sc.getLp(source), targetLp = tg.getLp(target);
        long sourceMax = sc.getMaxLp(), targetMax = tg.getMaxLp();
        long transfer = inTarget ? Math.min(amount, Math.min(sourceLp, targetMax - targetLp)) : Math.min(amount, Math.min(targetLp, sourceMax - sourceLp));
        if (transfer > 0) {
            (inTarget ? sc : tg).reduceLp(-transfer, inTarget ? source : target);
            (inTarget ? tg : sc).reduceLp(transfer, inTarget ? target : source);
        }
    }

    public static LpElement getLpElement(Object obj) {
        if (obj instanceof ItemStack stack && stack.getItem() instanceof LpElement element) return element;
        return obj instanceof LpElement element ? element : null;
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