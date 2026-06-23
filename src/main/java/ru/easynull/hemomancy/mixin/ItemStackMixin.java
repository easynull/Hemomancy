package ru.easynull.hemomancy.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.registry.HmDataComponents;
import ru.easynull.hemomancy.registry.items.tool.DesecratedTool;

@Mixin(ItemStack.class)
public final class ItemStackMixin {
    @Inject(method = "inventoryTick", at = @At("HEAD"), cancellable = true)
    private void onTick(Level level, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if(!(entity instanceof Player player)) return;
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof DesecratedTool) {
            if(stack.has(HmDataComponents.OWNER) && !stack.get(HmDataComponents.OWNER).contains(player.getScoreboardName())){
                if (level.getGameTime() % 80 == 0) player.displayClientMessage(Component.translatable("message.hemomancy.not_owner"), true);
                player.hurt(entity.damageSources().magic(), 4f);
                ci.cancel();
            }
        }
    }
}
