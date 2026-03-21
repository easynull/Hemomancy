package ru.easynull.hemomancy.api.ritual.trigger;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface RitualTrigger {
    boolean canStart(ServerWorld world, BlockPos pos);
    boolean inProgress(World world, BlockPos pos);
    void onCompleted(World world, BlockPos pos);
}
