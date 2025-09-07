package com.easynull.hemomancy.registers.blocks;

import com.easynull.hemomancy.registers.HcBlockEntities;
import com.mw.nullcore.Utils;
import com.mw.nullcore.core.blocks.EntitibleBlock;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public final class AlchemyBlock extends EntitibleBlock {
    public AlchemyBlock(Properties properties) {
        super(properties, HcBlockEntities.alchemy::get);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof ContainerBlockEntity container) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (stack.getCount() < stack.getMaxStackSize()) {
                    if (Utils.Item.insertItem(container, player, i, Screen.hasControlDown() ? 1 : 64)) return InteractionResult.SUCCESS;
                } else if(player.getMainHandItem().isEmpty()){
                    if (Utils.Item.insertItem(container, player, i, 64)) return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}