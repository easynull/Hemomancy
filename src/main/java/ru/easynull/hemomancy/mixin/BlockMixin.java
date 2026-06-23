package ru.easynull.hemomancy.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.api.ContainerBlockEntity;

@Mixin(BlockBehaviour.class)
public abstract class BlockMixin {
    @Inject(method = "onRemove", at = @At("HEAD"))
    private static void onDrops(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ContainerBlockEntity inv) {
                Containers.dropContents(level, pos, inv);
            }
        }
    }
}
