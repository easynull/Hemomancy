package ru.easynull.hemomancy.registry.blocks;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.easynull.hemomancy.api.EntitibleBlock;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.blocks.type.BloodAltarBlockEntity;
import ru.easynull.hemomancy.utils.HmCommonUtils;

public final class BloodAltarBlock extends EntitibleBlock {
    public static final VoxelShape SHAPE = Shapes.or(box(0, 0, 0, 4, 12, 4), box(12, 0, 0, 16, 12, 4), box(0, 0, 12, 4, 12, 16), box(12, 0, 12, 16, 12, 16), box(1, 0.1f, 1, 15, 11, 15));
    public BloodAltarBlock(Properties properties) {
        super(properties, ()-> HmBlockEntities.BLOOD_ALTAR);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(pos) instanceof BloodAltarBlockEntity container) {
            Storage<FluidVariant> altarFluidStorage = FluidStorage.SIDED.find(level, pos, blockState, container, blockHitResult.getDirection());

            if (altarFluidStorage != null) {
                if (FluidStorageUtil.interactWithFluidStorage(altarFluidStorage, player, interactionHand)) {
                    return ItemInteractionResult.SUCCESS;
                }
            }
            if (HmCommonUtils.insertIntoPlayer(container, player, 0, 64)) return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}