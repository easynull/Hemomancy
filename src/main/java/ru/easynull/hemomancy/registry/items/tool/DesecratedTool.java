package ru.easynull.hemomancy.registry.items.tool;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface DesecratedTool {
    void onAbilityMine(ItemStack stack, World world, BlockState state, BlockPos pos, ServerPlayerEntity player);
}
