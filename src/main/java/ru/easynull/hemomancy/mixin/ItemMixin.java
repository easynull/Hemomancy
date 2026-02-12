package ru.easynull.hemomancy.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.registry.items.tool.DesecratedTool;

import java.util.List;

@Mixin(Item.class)
public final class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("RETURN"))
    private void addTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        if (stack.getItem() instanceof DesecratedTool) {
            if (stack.hasNbt() && stack.getNbt().contains("Owner")) {
                tooltip.add(Text.translatable("tooltip.hemomancy.desecrated.owner", stack.getNbt().getString("Owner")));
            }
        }
    }
}
