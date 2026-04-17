package ru.easynull.hemomancy.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.api.InventoryBlockEntity;

@Mixin(AbstractBlock.class)
public abstract class BlockMixin {
    @Inject(method = "onStateReplaced", at = @At("HEAD"))
    private static void onDrops(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof InventoryBlockEntity inv) {
                ItemScatterer.spawn(world, pos, inv);
            }
        }
    }
}
