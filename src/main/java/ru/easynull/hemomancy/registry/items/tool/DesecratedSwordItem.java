package ru.easynull.hemomancy.registry.items.tool;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.easynull.hemomancy.utils.EnergyUtils;
import ru.easynull.hemomancy.utils.HmUtils;

public final class DesecratedSwordItem extends SwordItem implements DesecratedTool {
    private final boolean awakened;

    public DesecratedSwordItem(Settings settings, boolean awakened) {
        super(ToolMaterials.NETHERITE, awakened ? 15 : 12, -2.3f, settings);
        this.awakened = awakened;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (awakened) {
            ItemStack stack = user.getStackInHand(hand);
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        } else {
            return super.use(world, user, hand);
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!awakened || !(attacker instanceof PlayerEntity player)) {
            return super.postHit(stack, target, attacker);
        }
        ServerWorld world = (ServerWorld) attacker.getWorld();
        BlockPos center = target.getBlockPos();

        HmUtils.getNearbyLivingEntities(world, center, 2).forEach(e -> {
            if (e == player || e == target) return;

            player.attack(e);
            world.spawnParticles(DustParticleEffect.DEFAULT, e.getX() + 0.5, e.getY() + 0.5, e.getZ() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
        });

        EnergyUtils.extractLp(player, 50000);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            float baseDamage = getMaterial().getAttackDamage();
            HmUtils.attractEntities(world, user.getBlockPos(), 14f, 0.5f, LivingEntity.class, e -> {
                if (e.getType().isIn(ConventionalEntityTypeTags.BOSSES)) return false;
                if (e.getPos().distanceTo(player.getPos()) < 1.2f) e.damage(player.getDamageSources().playerAttack(player), baseDamage);
                return true;
            });
            EnergyUtils.extractLp(player, 5000);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onAbilityMine(ItemStack stack, World world, BlockState state, BlockPos pos, ServerPlayerEntity player) {}
}