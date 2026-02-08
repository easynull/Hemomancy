package ru.easynull.hemomancy.registry.items.tools;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.easynull.hemomancy.utils.EnergyUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public final class DesecratedAxeItem extends AxeItem implements Desecrated {

    private final boolean awakened;

    public DesecratedAxeItem(Settings settings, boolean awakened) {
        super(ToolMaterials.NETHERITE, awakened ? 16 : 14, -2.9f, settings);
        this.awakened = awakened;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!awakened || !state.isIn(BlockTags.LOGS) || !(miner instanceof PlayerEntity player) || player.isSneaking()) {
            return super.postMine(stack, world, state, pos, miner);
        }

        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(pos);
        visited.add(pos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            BlockState currentState = world.getBlockState(current);

            if (currentState.isIn(BlockTags.LOGS) || currentState.isIn(BlockTags.LEAVES)) {
                if (!player.isCreative()) {
                    currentState.getBlock().onBreak(world, current, currentState, player);
                }
                world.breakBlock(current, false, player);

                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            if (x == 0 && y == 0 && z == 0) continue;
                            BlockPos neighbor = current.add(x, y, z);
                            if (!visited.contains(neighbor)) {
                                visited.add(neighbor);
                                queue.add(neighbor);
                            }
                        }
                    }
                }
            }
        }
        EnergyUtils.extractLp(player, 100000);
        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return 0;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0;
    }
}
