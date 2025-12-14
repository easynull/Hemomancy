package com.easynull.hemomancy.registers.blocks;

import com.easynull.hemomancy.registers.HcBlockEntities;
import com.easynull.hemomancy.registers.blocks.type.AlchemyTableBE;
import com.easynull.hemomancy.registers.items.OrbItem;
import com.mw.nullcore.core.NcUtils;
import com.mw.nullcore.core.blocks.EntitibleBlock;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class AlchemyTableBlock extends EntitibleBlock {
    public AlchemyTableBlock(Properties properties) {
        super(properties.lightLevel(state -> 8), AlchemyTableBE::new);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof ContainerBlockEntity container)) return InteractionResult.PASS;
        ItemStack held = player.getMainHandItem();
        boolean reverse = held.isEmpty();
        int size = container.getContainerSize();
        if (player.isShiftKeyDown()) {
            for (int i = 0; i < size; i++) {
                if (container.getItem(i).getItem() instanceof OrbItem && NcUtils.Item.insertItem(container, player, i, Screen.hasControlDown() ? 1 : 64)) {
                    return InteractionResult.CONSUME;
                }
            }
        }
        for (int iter = 0; iter < size; iter++) {
            int i = reverse ? size - 1 - iter : iter;
            if ((i == 0 && !(held.getItem() instanceof OrbItem) && container.getItem(0).isEmpty()) || (i == 1 && container.getItem(1).isEmpty())) continue;
            ItemStack slot = container.getItem(i);
            boolean canInteract = held.isEmpty() ? !slot.isEmpty() : slot.isEmpty() || ItemStack.isSameItemSameComponents(held, slot);
            if (canInteract && NcUtils.Item.insertItem(container, player, i, Screen.hasControlDown() ? 1 : 64)) return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.or(Shapes.box(0.065, 0, 0.065, 0.935, 0.25, 0.935), Shapes.box(0.255, 0.125, 0.255, 0.745, 0.565, 0.745), Shapes.box(0, 0.565, 0, 1, 0.935, 1));
    }
}