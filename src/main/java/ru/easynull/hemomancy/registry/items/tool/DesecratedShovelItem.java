package ru.easynull.hemomancy.registry.items.tool;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.easynull.hemomancy.utils.EnergyUtils;
import ru.easynull.hemomancy.utils.HmUtils;

public final class DesecratedShovelItem extends ShovelItem implements DesecratedTool {
    private final boolean awakened;

    public DesecratedShovelItem(Settings settings, boolean awakened) {
        super(ToolMaterials.NETHERITE, 1.5f, -2.9f, settings);
        this.awakened = awakened;
    }

    @Override
    public void onAbilityMine(ItemStack stack, World world, BlockState state, BlockPos pos, ServerPlayerEntity player) {
        if (!awakened || !state.isIn(BlockTags.SHOVEL_MINEABLE) || player.isSneaking()) {
            return;
        }

        HmUtils.forEachInCube(pos, 3, p -> {
            BlockPos above = p.up();
            BlockState aboveState = world.getBlockState(above);
            if (aboveState.isAir()) return;
            if (aboveState.isIn(BlockTags.SHOVEL_MINEABLE)) {
                aboveState.getBlock().onBroken(world, p, aboveState);
                world.breakBlock(above, !player.isCreative(), player);
            }
        });

        EnergyUtils.extractLp(player, 100000);
    }
}