package ru.easynull.hemomancy.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.easynull.hemomancy.registry.items.tool.DesecratedTool;

@Mixin(ServerPlayerGameMode.class)
public final class InteractManagerMixin {
    @Shadow
    @Final
    protected ServerPlayer player;

    @Shadow
    protected ServerLevel level;

    @Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private void onBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer player = this.player;
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof DesecratedTool des) {
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) return;
            des.onAbilityMine(stack, level, state, pos, player);
        }
    }
}
