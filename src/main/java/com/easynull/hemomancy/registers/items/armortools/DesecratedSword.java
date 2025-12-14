package com.easynull.hemomancy.registers.items.armortools;

import com.easynull.hemomancy.registers.HcMaterials;
import com.easynull.hemomancy.utils.EnergyUtils;
import com.mw.nullcore.core.NcUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public final class DesecratedSword extends SwordItem implements Desecrated {
    final boolean awakened;

    public DesecratedSword(Properties properties, boolean awakened) {
        super(HcMaterials.desecratedTool, awakened ? 15 : 12, -2.3f, properties);
        this.awakened = awakened;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (awakened) {
            if(player.level() instanceof ServerLevel level) {
                NcUtils.Level.getEntities(player.level(), entity.blockPosition(), 2).forEach(e -> {
                    if (e == player || !(e instanceof LivingEntity le)) return;
                    float baseDamage = stack.get(DataComponents.TOOL).damagePerBlock();
                    float enchantedDamage = baseDamage + EnchantmentHelper.getDamageProtection(level, le, player.damageSources().playerAttack(player));
                    le.hurt(player.damageSources().playerAttack(player), enchantedDamage);
                    EnchantmentHelper.doPostAttackEffects(level, le, player.damageSources().playerAttack(player));
                    if (!le.is(entity)) NcUtils.Particle.forParticleSpawn(player.level(), DustParticleOptions.REDSTONE, (float) (le.getX() + 0.5), (float) (le.getY() + 0.5), (float) (le.getZ() + 0.5), 1f, 1f, 1f, 2);
                });
            }
            EnergyUtils.extractLp(player, 50000);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }
}
