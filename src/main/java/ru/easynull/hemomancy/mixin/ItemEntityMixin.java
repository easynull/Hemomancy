package ru.easynull.hemomancy.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.registry.items.tool.DesecratedTool;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract ItemStack getStack();

    @Inject(method = "onPlayerCollision", at = @At(value = "HEAD"), cancellable = true)
    private void onCollision(PlayerEntity player, CallbackInfo ci) {
        ItemStack stack = getStack();
        if (stack.getItem() instanceof DesecratedTool && stack.hasNbt() && stack.getNbt().contains("Owner") && !stack.getNbt().getString("Owner").contains(player.getEntityName())) {
            if (player.getWorld().getTime() % 80 == 0) player.sendMessage(Text.translatable("message.hemomancy.not_owner"), true);
            ci.cancel();
        }
    }
}
