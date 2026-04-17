package ru.easynull.hemomancy.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.easynull.hemomancy.registry.items.sigil.EnabledSigilItem;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Unique
    private static MinecraftClient client = MinecraftClient.getInstance();

    @Redirect(method = "getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ModelOverrideList;apply(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;"))
    private BakedModel redirectApplyOverrides(ModelOverrideList instance, BakedModel model, ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
        if (stack.getItem() instanceof EnabledSigilItem sigil && EnabledSigilItem.isEnabled(stack)) {
            Identifier baseId = Registries.ITEM.getId(sigil);
            Identifier enabledId = baseId.withSuffixedPath("_enabled");

            ModelIdentifier modelId = new ModelIdentifier(enabledId, "inventory");

            BakedModel customModel = client.getBakedModelManager().getModel(modelId);

            if (customModel != client.getBakedModelManager().getMissingModel()) {
                return customModel;
            }

            return instance.apply(customModel, stack, world, entity, seed);
        }

        return instance.apply(model, stack, world, entity, seed);
    }
}
