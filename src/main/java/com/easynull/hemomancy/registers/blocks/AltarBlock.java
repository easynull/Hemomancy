package com.easynull.hemomancy.registers.blocks;

import com.easynull.hemomancy.registers.HcBlockEntities;
import com.easynull.hemomancy.registers.blocks.type.AltarBE;
import com.easynull.hemomancy.utils.EnergyUtils;
import com.mw.nullcore.core.blocks.EasyEntityBlock;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class AltarBlock extends EasyEntityBlock {
    public AltarBlock(Properties prop) {
        super(prop, HcBlockEntities.bloodAltar::get);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof AltarBE altar) {
            if (EnergyUtils.insertItem(altar, player, 0, Screen.hasControlDown() ? 1 : 64)) return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
