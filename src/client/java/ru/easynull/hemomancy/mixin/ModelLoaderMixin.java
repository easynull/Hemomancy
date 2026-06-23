package ru.easynull.hemomancy.mixin;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.registry.items.sigil.EnabledSigilItem;
import ru.easynull.hemomancy.utils.HmCommonUtils;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelLoaderMixin {

    @Shadow
    protected abstract void loadSpecialItemModelAndDependencies(ModelResourceLocation modelResourceLocation);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Collection;forEach(Ljava/util/function/Consumer;)V"))
    private void onInitModels(BlockColors blockColors, ProfilerFiller profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci) {
        for (var sigil : HmCommonUtils.getElementsClasses(BuiltInRegistries.ITEM, EnabledSigilItem.class)) {
            ResourceLocation id = sigil.builtInRegistryHolder().key().location().withSuffix("_enabled");
            loadSpecialItemModelAndDependencies(new ModelResourceLocation(id, "inventory"));
        }
    }
}
