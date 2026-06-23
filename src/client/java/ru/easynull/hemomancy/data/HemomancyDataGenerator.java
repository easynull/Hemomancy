package ru.easynull.hemomancy.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class HemomancyDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(HmRecipeProvider::new);
        HmBlockTagsProvider blockTagProvider = pack.addProvider(HmBlockTagsProvider::new);
        pack.addProvider((output, registries) -> new HmItemTagsProvider(output, registries, blockTagProvider));
    }
}
