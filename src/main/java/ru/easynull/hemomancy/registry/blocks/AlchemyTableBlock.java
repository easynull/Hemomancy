package ru.easynull.hemomancy.registry.blocks;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
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
import ru.easynull.hemomancy.api.InventoryBlockEntity;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.blocks.type.AlchemyTableBlockEntity;
import ru.easynull.hemomancy.registry.items.OrbItem;
import ru.easynull.hemomancy.utils.HmCommonUtils;

public final class AlchemyTableBlock extends EntitibleBlock {
    private static final VoxelShape SHAPE = VoxelShapes.union(VoxelShapes.cuboid(0.065, 0, 0.065, 0.935, 0.25, 0.935), VoxelShapes.cuboid(0.255, 0.125, 0.255, 0.745, 0.565, 0.745), VoxelShapes.cuboid(0, 0.565, 0, 1, 0.935, 1));

    public AlchemyTableBlock(Settings properties) {
        super(properties.luminance(state -> 8), () -> HmBlockEntities.ALCHEMY_TABLE);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof AlchemyTableBlockEntity container && !container.isCrafting()) {
            ItemStack handStack = player.getStackInHand(hand);
            if(!container.getStack(1).isEmpty()){
                player.giveItemStack(container.getStack(1).copy());
                container.removeStack(1);
                return ActionResult.SUCCESS;
            }
            if (handStack.getItem() instanceof OrbItem && HmCommonUtils.insertIntoPlayer(container, player, 0, 64)) return ActionResult.SUCCESS;
            if(player.isSneaking() && !container.getFirst().isEmpty()){
                player.giveItemStack(container.getFirst().copy());
                container.removeStack(0);
                return ActionResult.SUCCESS;
            }
            for(int slot = 2; slot < container.size(); slot++){
                ItemStack slotStack = container.getStack(slot);

                if (handStack.isEmpty()) {
                    if (!slotStack.isEmpty()) {
                        player.setStackInHand(hand, slotStack.copy());
                        container.setStack(slot, ItemStack.EMPTY);
                        container.markDirty();
                        return ActionResult.SUCCESS;
                    }
                } else {
                    if (slotStack.isEmpty()) {
                        container.setStack(slot, handStack.split(handStack.getCount()));
                        container.markDirty();
                        return ActionResult.SUCCESS;
                    } else if (ItemStack.areItemsEqual(handStack, slotStack) && ItemStack.canCombine(handStack, slotStack)) {
                        int maxTransfer = Math.min(slotStack.getMaxCount() - slotStack.getCount(), handStack.getCount());
                        if (maxTransfer > 0) {
                            slotStack.increment(maxTransfer);
                            handStack.decrement(maxTransfer);
                            container.markDirty();
                            return ActionResult.SUCCESS;
                        }
                    }
                }
            }
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