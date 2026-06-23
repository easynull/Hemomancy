package ru.easynull.hemomancy.registry.items.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface DesecratedTool {
    void onAbilityMine(ItemStack stack, Level level, BlockState state, BlockPos pos, ServerPlayer player);
}
