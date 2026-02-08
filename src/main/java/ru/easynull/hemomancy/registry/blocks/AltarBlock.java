package ru.easynull.hemomancy.registry.blocks;

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
import ru.easynull.hemomancy.api.SidedBE;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.utils.HmUtils;

public final class AltarBlock extends EntitibleBlock {

    public AltarBlock(Settings properties) {
        super(properties.luminance(state -> 8), ()-> HmBlockEntities.BLOOD_ALTAR);
    }

    @Override
    public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof SidedBE container) {
            if (HmUtils.insertIntoPlayer(container, player, 0, 64))
                return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 1f, 0.75f, 1f);
    }
}