package ru.easynull.hemomancy.mixin;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.registry.items.sigil.EnabledSigilItem;
import ru.easynull.hemomancy.utils.HmUtils;

import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow
    protected abstract void addModel(ModelIdentifier modelId);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Collection;forEach(Ljava/util/function/Consumer;)V"))
    private void onInitModels(BlockColors blockColors, Profiler profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci) {
        for (var sigil : HmUtils.getElementsClasses(Registries.ITEM, EnabledSigilItem.class)) {
            Identifier id = sigil.getRegistryEntry().registryKey().getValue().withSuffixedPath("_enabled");
            addModel(new ModelIdentifier(id, "inventory"));
        }
    }
}
