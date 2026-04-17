package ru.easynull.hemomancy.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.registry.items.tool.DesecratedTool;

@Mixin(HandledScreen.class)
public final class HandledScreenMixin {
    @Shadow
    @Nullable
    private Slot focusedSlot;

    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At(value = "HEAD"), cancellable = true)
    private void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (focusedSlot == null || !focusedSlot.hasStack()) return;
        ItemStack stack = focusedSlot.getStack();
        var player = MinecraftClient.getInstance().player;
        if(stack.getItem() instanceof DesecratedTool && stack.hasNbt() && stack.getNbt().contains("Owner") && !stack.getNbt().getString("Owner").contains(player.getEntityName())){
            player.sendMessage(Text.translatable("message.hemomancy.not_owner"));
            ci.cancel();
        }
    }
}
