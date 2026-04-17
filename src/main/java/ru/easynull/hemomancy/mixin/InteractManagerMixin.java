package ru.easynull.hemomancy.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.easynull.hemomancy.registry.items.tool.DesecratedTool;

@Mixin(ServerPlayerInteractionManager.class)
public final class InteractManagerMixin {
    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @Shadow
    protected ServerWorld world;

    @Inject(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"))
    private void onBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = this.player;
        ItemStack stack = player.getMainHandStack();
        if (stack.getItem() instanceof DesecratedTool des) {
            BlockState state = world.getBlockState(pos);
            if (state.isAir()) return;
            des.onAbilityMine(stack, world, state, pos, player);
        }
    }
}
