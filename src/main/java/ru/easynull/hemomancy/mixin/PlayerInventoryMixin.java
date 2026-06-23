package ru.easynull.hemomancy.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.registry.HmDataComponents;
import ru.easynull.hemomancy.registry.items.tool.DesecratedTool;

@Mixin(Inventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow
    @Final
    public Player player;

    @Inject(method = "setItem", at = @At("HEAD"))
    private void setStack(int slot, ItemStack stack, CallbackInfo ci) {
        if (stack.getItem() instanceof DesecratedTool) {
            if (!stack.has(HmDataComponents.OWNER)) {
                stack.set(HmDataComponents.OWNER, player.getScoreboardName());
            }
        }
    }
}
