package ru.easynull.hemomancy.registry.blocks;

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
import ru.easynull.hemomancy.registry.blocks.type.AlchemyTableBlockEntity;
import ru.easynull.hemomancy.registry.items.OrbItem;
import ru.easynull.hemomancy.utils.HmCommonUtils;

public final class AlchemyTableBlock extends EntitibleBlock {
    private static final VoxelShape SHAPE = Shapes.or(Shapes.box(0.065, 0, 0.065, 0.935, 0.25, 0.935), Shapes.box(0.255, 0.125, 0.255, 0.745, 0.565, 0.745), Shapes.box(0, 0.565, 0, 1, 0.935, 1));

    public AlchemyTableBlock(Properties properties) {
        super(properties.lightLevel(state -> 8), () -> HmBlockEntities.ALCHEMY_TABLE);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(pos) instanceof AlchemyTableBlockEntity container && !container.isCrafting()) {
            ItemStack handStack = player.getItemInHand(hand);
            if(!container.getItem(1).isEmpty()){
                player.addItem(container.getItem(1).copy());
                container.removeItemNoUpdate(1);
                return ItemInteractionResult.SUCCESS;
            }
            if (handStack.getItem() instanceof OrbItem && HmCommonUtils.insertIntoPlayer(container, player, 0, 64)) return ItemInteractionResult.SUCCESS;
            if(player.isShiftKeyDown() && !container.getFirst().isEmpty()){
                player.addItem(container.getFirst().copy());
                container.removeItemNoUpdate(0);
                return ItemInteractionResult.SUCCESS;
            }
            for(int slot = 2; slot < container.getContainerSize(); slot++){
                ItemStack slotStack = container.getItem(slot);

                if (handStack.isEmpty()) {
                    if (!slotStack.isEmpty()) {
                        player.setItemInHand(hand, slotStack.copy());
                        container.setItem(slot, ItemStack.EMPTY);
                        container.setChanged();
                        return ItemInteractionResult.SUCCESS;
                    }
                } else {
                    if (slotStack.isEmpty()) {
                        container.setItem(slot, handStack.split(handStack.getCount()));
                        container.setChanged();
                        return ItemInteractionResult.SUCCESS;
                    } else if (ItemStack.isSameItem(handStack, slotStack) && ItemStack.isSameItemSameComponents(handStack, slotStack)) {
                        int maxTransfer = Math.min(slotStack.getMaxStackSize() - slotStack.getCount(), handStack.getCount());
                        if (maxTransfer > 0) {
                            slotStack.grow(maxTransfer);
                            handStack.shrink(maxTransfer);
                            container.setChanged();
                            return ItemInteractionResult.SUCCESS;
                        }
                    }
                }
            }
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