package ru.easynull.hemomancy.registry.items.tools;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import ru.easynull.hemomancy.utils.EnergyUtils;
import ru.easynull.hemomancy.utils.HmUtils;

public final class DesecratedSwordItem extends SwordItem implements Desecrated {

    private final boolean awakened;

    public DesecratedSwordItem(Settings settings, boolean awakened) {
        super(ToolMaterials.NETHERITE, awakened ? 15 : 12, -2.3f, settings);
        this.awakened = awakened;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!awakened || !(attacker instanceof PlayerEntity player)) {
            return super.postHit(stack, target, attacker);
        }

        ServerWorld world = (ServerWorld) attacker.getWorld();
        BlockPos center = target.getBlockPos();

        HmUtils.getNearbyLivingEntities(world, center, 2).forEach(e -> {
            if (e == player) return;

            float baseDamage = getMaterial().getAttackDamage();
            float enchantedDamage = baseDamage + EnchantmentHelper.getAttackDamage(stack, e.getGroup());
            e.damage(player.getDamageSources().playerAttack(player), enchantedDamage);
            EnchantmentHelper.onTargetDamaged(e, player);

            if (!e.equals(target)) {
                world.spawnParticles(DustParticleEffect.DEFAULT, e.getX() + 0.5, e.getY() + 0.5, e.getZ() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
            }
        });
        EnergyUtils.extractLp(player, 50000);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return 0;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0;
    }
}