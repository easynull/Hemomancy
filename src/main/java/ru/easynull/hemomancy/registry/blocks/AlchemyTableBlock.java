package ru.easynull.hemomancy.registry.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
import ru.easynull.hemomancy.registry.blocks.type.AlchemyTableBE;
import ru.easynull.hemomancy.registry.items.OrbItem;
import ru.easynull.hemomancy.utils.HmUtils;

public final class AlchemyTableBlock extends EntitibleBlock {

    public AlchemyTableBlock(Settings properties) {
        super(properties.luminance(state -> 8), ()-> HmBlockEntities.ALCHEMY_TABLE);
    }

    @Override
    public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (level.isClient) return ActionResult.PASS;

        if (!(level.getBlockEntity(pos) instanceof SidedBE container)) {
            return ActionResult.PASS;
        }

        ItemStack held = player.getStackInHand(hand);
        boolean reverse = held.isEmpty();
        int size = container.size();

        if (player.isSneaking()) {
            for (int i = 0; i < size; i++) {
                if (container.getStack(i).getItem() instanceof OrbItem && HmUtils.insertIntoPlayer(container, player, i, 64)) {
                    return ActionResult.CONSUME;
                }
            }
        }

        for (int iter = 0; iter < size; iter++) {
            int i = reverse ? size - 1 - iter : iter;

            if ((i == 0 && !(held.getItem() instanceof OrbItem) && container.getStack(0).isEmpty()) || (i == 1 && container.getStack(1).isEmpty())) continue;

            ItemStack slot = container.getStack(i);
            boolean canInteract = held.isEmpty() ? !slot.isEmpty() : slot.isEmpty() || ItemStack.areEqual(held, slot) && ItemStack.areItemsEqual(held, slot);

            if (canInteract && HmUtils.insertIntoPlayer(container, player, i, 64)) {
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.065, 0,     0.065, 0.935, 0.25,  0.935),
                VoxelShapes.cuboid(0.255, 0.125, 0.255, 0.745, 0.565, 0.745),
                VoxelShapes.cuboid(0,     0.565, 0,     1,     0.935, 1)
        );
    }
}