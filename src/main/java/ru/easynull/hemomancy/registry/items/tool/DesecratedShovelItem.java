package ru.easynull.hemomancy.registry.items.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import ru.easynull.hemomancy.utils.EnergyUtils;
import ru.easynull.hemomancy.utils.HmCommonUtils;

public final class DesecratedShovelItem extends ShovelItem implements DesecratedTool {
    private final boolean awakened;

    public DesecratedShovelItem(Properties settings, boolean awakened) {
        super(Tiers.NETHERITE/*, 1.5f, -2.9f*/, settings.component(DataComponents.UNBREAKABLE, new Unbreakable(true)));
        this.awakened = awakened;
    }

    @Override
    public void onAbilityMine(ItemStack stack, Level level, BlockState state, BlockPos pos, ServerPlayer player) {
        if (!awakened || !state.is(BlockTags.MINEABLE_WITH_SHOVEL) || player.isShiftKeyDown()) {
            return;
        }

        HmCommonUtils.forEachInCube(pos, 3, p -> {
            BlockPos above = p.above();
            BlockState aboveState = level.getBlockState(above);
            if (aboveState.isAir()) return;
            if (aboveState.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                aboveState.getBlock().destroy(level, p, aboveState);
                level.destroyBlock(above, !player.isCreative(), player);
            }
        });

        EnergyUtils.extractLp(player, 100000);
    }
}