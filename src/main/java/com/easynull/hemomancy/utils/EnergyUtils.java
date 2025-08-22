package com.easynull.hemomancy.utils;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public final class EnergyUtils {
    public static float calculateDamage(LivingEntity entity, long lp) {
        float diminishing = 0.8f;
        float damage = (float) (0.5f * Math.log1p(lp / 10.0));
        damage *= (float) Math.pow(damage, -diminishing * 0.1);
        return Math.min(damage, entity.getMaxHealth());
    }

    public static void damageLp(LivingEntity entity, long lp) {
        if (lp == 0) return;
        float dm = calculateDamage(entity, lp);
        entity.hurt(entity.damageSources().generic(), dm);
        entity.playSound(SoundEvents.ALLAY_THROW);
    }
}
