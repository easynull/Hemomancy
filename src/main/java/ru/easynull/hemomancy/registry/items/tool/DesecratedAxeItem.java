package ru.easynull.hemomancy.registry.items.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import ru.easynull.hemomancy.utils.HmCommonUtils;

public final class DesecratedAxeItem extends AxeItem implements DesecratedTool {
    private final boolean awakened;

    public DesecratedAxeItem(Properties settings, boolean awakened) {
        super(Tiers.NETHERITE /*,awakened ? 16 : 14, -2.9f*/, settings.component(DataComponents.UNBREAKABLE, new Unbreakable(true)));
        this.awakened = awakened;
    }

    @Override
    public void onAbilityMine(ItemStack stack, Level level, BlockState state, BlockPos pos, ServerPlayer player) {
        if (!awakened || !state.is(BlockTags.MINEABLE_WITH_AXE) || player.isShiftKeyDown()) {
            return;
        }

        if(!HmCommonUtils.breakTree((ServerLevel) level, pos, player)) {
            HmCommonUtils.forEachInCube(pos, 3, p -> {
                BlockPos above = p.above();
                BlockState aboveState = level.getBlockState(above);
                if (aboveState.isAir()) return;
                if (aboveState.is(BlockTags.MINEABLE_WITH_AXE)) {
                    level.destroyBlock(above, !player.isCreative(), player);
                }
            });
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel sWorld)) return super.useOn(context);
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof BonemealableBlock fertilizable) {
            if (fertilizable.isValidBonemealTarget(level, pos, state)) {
                sWorld.sendParticles(DustParticleOptions.REDSTONE, pos.getX() + 0.5f, pos.getY() + 0.35f, pos.getZ() + 0.5f, 5, 0.2f, 0, 0.2f, 0);
                if (fertilizable.isBonemealSuccess(level, level.random, pos, state)) {
                    fertilizable.performBonemeal(sWorld, level.random, pos, state);
                    return InteractionResult.CONSUME;
                }
            }
        }
        return super.useOn(context);
    }
}
