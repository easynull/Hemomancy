package ru.easynull.hemomancy.registry.items.tool;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import ru.easynull.hemomancy.utils.EnergyUtils;
import ru.easynull.hemomancy.utils.HmCommonUtils;

public final class DesecratedSwordItem extends SwordItem implements DesecratedTool {
    private final boolean awakened;

    public DesecratedSwordItem(Properties settings, boolean awakened) {
        super(Tiers.NETHERITE/*, awakened ? 15 : 12, -2.3f*/, settings.component(DataComponents.UNBREAKABLE, new Unbreakable(true)));
        this.awakened = awakened;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        if (awakened) {
            ItemStack stack = user.getItemInHand(hand);
            user.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        } else {
            return super.use(level, user, hand);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!awakened || !(attacker instanceof Player player)) {
            return super.hurtEnemy(stack, target, attacker);
        }
        ServerLevel level = (ServerLevel) attacker.level();
        BlockPos center = target.blockPosition();

        HmCommonUtils.getNearbyLivingEntities(level, center, 2).forEach(e -> {
            if (e == player || e == target) return;

            player.attack(e);
            level.sendParticles(DustParticleOptions.REDSTONE, e.getX() + 0.5, e.getY() + 0.5, e.getZ() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
        });

        EnergyUtils.extractLp(player, 50000);
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof Player player) {
            float baseDamage = getTier().getAttackDamageBonus();
            HmCommonUtils.attractEntities(level, user.blockPosition(), 14f, 0.5f, LivingEntity.class, e -> {
                if (e.getType().is(ConventionalEntityTypeTags.BOSSES)) return false;
                if (e.position().distanceTo(player.position()) < 1.2f) e.hurt(player.damageSources().playerAttack(player), baseDamage);
                return true;
            });
            EnergyUtils.extractLp(player, 5000);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void onAbilityMine(ItemStack stack, Level level, BlockState state, BlockPos pos, ServerPlayer player) {}
}