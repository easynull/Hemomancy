package ru.easynull.hemomancy.api.ritual.trigger;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface RitualTrigger {
    boolean canStart(ServerLevel level, BlockPos pos);
    boolean inProgress(Level level, BlockPos pos);
    void onCompleted(Level level, BlockPos pos);
}
