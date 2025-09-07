package com.easynull.hemomancy.registers.blocks;

import com.mw.nullcore.Utils;
import com.mw.nullcore.core.blocks.EntitibleBlock;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class PedestalBlock extends EntitibleBlock {
    public PedestalBlock(Properties properties, Supplier<BlockEntityType<?>> type) {
        super(properties, type);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof ContainerBlockEntity container) {
            if (Utils.Item.insertItem(container, player, 0, Screen.hasControlDown() ? 1 : 64)) return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
