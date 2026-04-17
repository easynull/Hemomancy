package ru.easynull.hemomancy.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.registry.items.tool.DesecratedTool;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow
    @Final
    public PlayerEntity player;

    @Inject(method = "setStack", at = @At("HEAD"))
    private void setStack(int slot, ItemStack stack, CallbackInfo ci) {
        if (stack.getItem() instanceof DesecratedTool) {
            if (!stack.hasNbt() || !stack.getNbt().contains("Owner")) {
                NbtCompound nbt = stack.getOrCreateNbt();
                nbt.putString("Owner", player.getEntityName());
            }
        }
    }
}
