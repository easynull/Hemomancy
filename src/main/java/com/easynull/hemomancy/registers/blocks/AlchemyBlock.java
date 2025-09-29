package com.easynull.hemomancy.registers.blocks;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.registers.HcBlockEntities;
import com.easynull.hemomancy.registers.items.OrbItem;
import com.mw.nullcore.Utils;
import com.mw.nullcore.core.blocks.EntitibleBlock;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class AlchemyBlock extends EntitibleBlock {
    public AlchemyBlock(Properties properties) {
        super(properties.lightLevel(state -> 8), HcBlockEntities.alchemy::get);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof ContainerBlockEntity container) {
            ItemStack held = player.getMainHandItem();
            boolean reverse = held.isEmpty();
            for (int iter = 0; iter < container.getContainerSize(); iter++) {
                if(container.getItem(iter).getItem() instanceof OrbItem && player.isShiftKeyDown()){
                    Utils.Item.insertItem(container, player, iter, Screen.hasControlDown() ? 1 : 64);
                    return InteractionResult.CONSUME;
                }
                int i = reverse ? container.getContainerSize() - 1 - iter : iter;
                if ((i == 0 && !(held.getItem() instanceof OrbItem) && container.getFirst().isEmpty()) || (i == 1 && container.getItem(1).isEmpty())) continue;
                ItemStack slot = container.getItem(i);
                boolean canInteract = held.isEmpty() ? !slot.isEmpty() : slot.isEmpty() || ItemStack.isSameItemSameComponents(held, slot);

                if (canInteract && Utils.Item.insertItem(container, player, i, Screen.hasControlDown() ? 1 : 64)) return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}