package com.easynull.hemomancy.registers.blocks;

import com.easynull.hemomancy.registers.blocks.type.AltarBE;
import com.mw.nullcore.core.NcUtils;
import com.mw.nullcore.core.blocks.EntitibleBlock;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public final class AltarBlock extends EntitibleBlock {
    public AltarBlock(Properties properties) {
        super(properties.lightLevel(state -> 8), AltarBE::new);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof ContainerBlockEntity container) {
            if (NcUtils.Item.insertItem(container, player, 0, Screen.hasControlDown() ? 1 : 64)) return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.box(0, 0, 0, 1f, 0.75f, 1f);
    }
}
