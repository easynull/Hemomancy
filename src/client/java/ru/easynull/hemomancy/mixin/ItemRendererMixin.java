package ru.easynull.hemomancy.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.easynull.hemomancy.registry.items.sigil.EnabledSigilItem;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Redirect(method = "getModel(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/ItemOverrides;resolve(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;"))
    private BakedModel redirectApplyOverrides(ItemOverrides instance, BakedModel model, ItemStack stack, ClientLevel level, LivingEntity entity, int seed) {
        if (stack.getItem() instanceof EnabledSigilItem sigil && EnabledSigilItem.isEnabled(stack)) {
            ResourceLocation baseId = BuiltInRegistries.ITEM.getKey(sigil);
            ResourceLocation enabledId = baseId.withSuffix("_enabled");

            ModelResourceLocation modelId = new ModelResourceLocation(enabledId, "inventory");

            BakedModel customModel = net.minecraft.client.Minecraft.getInstance().getModelManager().getModel(modelId);

            if (customModel != net.minecraft.client.Minecraft.getInstance().getModelManager().getMissingModel()) {
                return customModel;
            }

            return instance.resolve(customModel, stack, level, entity, seed);
        }

        return instance.resolve(model, stack, level, entity, seed);
    }
}
