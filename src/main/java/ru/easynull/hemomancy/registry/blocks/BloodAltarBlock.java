package ru.easynull.hemomancy.registry.blocks;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import ru.easynull.hemomancy.api.EntitibleBlock;
import ru.easynull.hemomancy.api.InventoryBE;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.utils.HmCommonUtils;

public final class BloodAltarBlock extends EntitibleBlock {
    public static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(0, 0, 0, 4, 12, 4), createCuboidShape(12, 0, 0, 16, 12, 4), createCuboidShape(0, 0, 12, 4, 12, 16), createCuboidShape(12, 0, 12, 16, 12, 16), createCuboidShape(1, 0.1f, 1, 15, 11, 15));
    public BloodAltarBlock(Settings properties) {
        super(properties, ()-> HmBlockEntities.BLOOD_ALTAR);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (world.getBlockEntity(pos) instanceof InventoryBE container) {
            if (HmCommonUtils.insertIntoPlayer(container, player, 0, 64)) return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}