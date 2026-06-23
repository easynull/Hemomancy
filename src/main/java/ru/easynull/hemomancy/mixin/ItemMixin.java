package ru.easynull.hemomancy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.registry.HmDataComponents;
import ru.easynull.hemomancy.registry.items.tool.DesecratedTool;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

@Mixin(Item.class)
public final class ItemMixin {
    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void addTooltip(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (stack.getItem() instanceof DesecratedTool) {
            if (stack.has(HmDataComponents.OWNER)) {
                tooltip.add(Component.translatable("tooltip.hemomancy.desecrated.owner", stack.get(HmDataComponents.OWNER)));
            }
        }
    }
}
