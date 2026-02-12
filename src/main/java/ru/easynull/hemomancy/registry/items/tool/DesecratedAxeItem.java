package ru.easynull.hemomancy.registry.items.tool;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.easynull.hemomancy.utils.HmUtils;

public final class DesecratedAxeItem extends AxeItem implements DesecratedTool {
    private final boolean awakened;

    public DesecratedAxeItem(Settings settings, boolean awakened) {
        super(ToolMaterials.NETHERITE, awakened ? 16 : 14, -2.9f, settings);
        this.awakened = awakened;
    }

    @Override
    public void onAbilityMine(ItemStack stack, World world, BlockState state, BlockPos pos, ServerPlayerEntity player) {
        if (!awakened || !state.isIn(BlockTags.AXE_MINEABLE) || player.isSneaking()) {
            return;
        }

        if(!HmUtils.breakTree((ServerWorld) world, pos, player)) {
            HmUtils.forEachInCube(pos, 3, p -> {
                BlockPos above = p.up();
                BlockState aboveState = world.getBlockState(above);
                if (aboveState.isAir()) return;
                if (aboveState.isIn(BlockTags.AXE_MINEABLE)) {
                    world.breakBlock(above, !player.isCreative(), player);
                }
            });
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld sWorld)) return super.useOnBlock(context);
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof Fertilizable fertilizable) {
            if (fertilizable.isFertilizable(world, pos, state, false)) {
                sWorld.spawnParticles(DustParticleEffect.DEFAULT, pos.getX() + 0.5f, pos.getY() + 0.35f, pos.getZ() + 0.5f, 5, 0.2f, 0, 0.2f, 0);
                if (fertilizable.canGrow(world, world.random, pos, state)) {
                    fertilizable.grow(sWorld, world.random, pos, state);
                    return ActionResult.CONSUME;
                }
            }
        }
        return super.useOnBlock(context);
    }
}
