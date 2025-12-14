package com.easynull.hemomancy.registers.items.armortools;

import com.easynull.hemomancy.registers.HcMaterials;
import com.easynull.hemomancy.utils.EnergyUtils;
import com.mw.nullcore.core.NcUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class DesecratedPickaxe extends PickaxeItem implements Desecrated {
    final boolean awakened;
    public DesecratedPickaxe(Properties properties, boolean awakened) {
        super(HcMaterials.desecratedTool, 2, -2.8f, properties);
        this.awakened = awakened;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (awakened && level.getBlockState(pos).is(BlockTags.MINEABLE_WITH_PICKAXE) && !Screen.hasShiftDown()) {
            NcUtils.Block.forEachCube(pos, 2, p -> {
                BlockState adState = level.getBlockState(p.above());
                if (adState.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                    if (!((Player) miningEntity).isCreative()) adState.getBlock().playerDestroy(level, (Player) miningEntity, p.above(), adState, level.getBlockEntity(p.above()), stack);
                    level.destroyBlock(p.above(), false);
                }
            });
            EnergyUtils.extractLp((Player) miningEntity, 100000);
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }
}
