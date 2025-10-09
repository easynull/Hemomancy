package com.easynull.hemomancy.registers.items.armortools;

import com.easynull.hemomancy.registers.HcMaterials;
import com.easynull.hemomancy.utils.EnergyUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public final class DesecratedAxe extends AxeItem implements Desecrated {
    final boolean awakened;
    public DesecratedAxe(Properties properties, boolean awakened) {
        super(HcMaterials.desecratedTool, awakened ? 16 : 14, -2.9f, properties);
        this.awakened = awakened;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (awakened && state.is(BlockTags.LOGS) && !Screen.hasShiftDown()) {
            Queue<BlockPos> queue = new LinkedList<>();
            Set<BlockPos> visited = new HashSet<>();
            queue.add(pos);
            visited.add(pos);

            while (!queue.isEmpty()) {
                BlockPos current = queue.poll();
                BlockState currentState = level.getBlockState(current);

                if (currentState.is(BlockTags.LOGS) || currentState.is(BlockTags.LEAVES)) {
                    if (!((Player) miningEntity).isCreative()) currentState.getBlock().playerDestroy(level, (Player) miningEntity, current, currentState, null, stack);
                    level.destroyBlock(current, false);
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                if (x == 0 && y == 0 && z == 0) continue;
                                BlockPos neighbor = current.offset(x, y, z);
                                if (!visited.contains(neighbor)) {
                                    visited.add(neighbor);
                                    queue.add(neighbor);
                                }
                            }
                        }
                    }
                }
            }
            EnergyUtils.extractLp((Player) miningEntity, 100000);
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    public int getDamage(ItemStack stack) {
        return 0;
    }
}
