package ru.easynull.hemomancy.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.easynull.hemomancy.registry.items.tool.DesecratedTool;

@Mixin(ItemStack.class)
public final class ItemStackMixin {
    @Inject(method = "isDamageable", at = @At("RETURN"), cancellable = true)
    private void isDamageable(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof DesecratedTool) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"), cancellable = true)
    private void onTick(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if(!(entity instanceof PlayerEntity player)) return;
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof DesecratedTool) {
            if(stack.hasNbt() && stack.getNbt().contains("Owner") && !stack.getNbt().getString("Owner").contains(player.getEntityName())){
                if (world.getTime() % 80 == 0) player.sendMessage(Text.translatable("message.hemomancy.not_owner"), true);
                player.damage(entity.getDamageSources().magic(), 4f);
                ci.cancel();
            }
        }
    }
}
