package ru.easynull.hemomancy.registry.items.tools;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.easynull.hemomancy.utils.EnergyUtils;
import ru.easynull.hemomancy.utils.HmUtils;

public final class DesecratedPickaxeItem extends PickaxeItem implements Desecrated {

    private final boolean awakened;

    public DesecratedPickaxeItem(Settings settings, boolean awakened) {
        super(ToolMaterials.NETHERITE, 2, -2.8f, settings);
        this.awakened = awakened;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!awakened || !state.isIn(BlockTags.PICKAXE_MINEABLE) || !(miner instanceof PlayerEntity player) || player.isSneaking()) {
            return super.postMine(stack, world, state, pos, miner);
        }

        HmUtils.forEachInCube(pos, 2, p -> {
            BlockPos above = p.up();
            BlockState aboveState = world.getBlockState(above);
            if (aboveState.isIn(BlockTags.PICKAXE_MINEABLE)) {
                if (!player.isCreative()) {
                    aboveState.getBlock().onBreak(world, above, aboveState, player);
                }
                world.breakBlock(above, false, player);
            }
        });

        EnergyUtils.extractLp(player, 100000);
        return super.postMine(stack, world, state, pos, miner);
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