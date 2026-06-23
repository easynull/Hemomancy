package ru.easynull.hemomancy.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.blocks.RuneBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class HmBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public static final TagKey<Block> RUNES = TagKey.create(Registries.BLOCK, Hemomancy.path("runes"));

    public HmBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        tag(ConventionalBlockTags.STORAGE_BLOCKS).add(HmBlocks.CRIMSON_ORNAMENT.builtInRegistryHolder().key(), HmBlocks.TRANSCENDENTAL_CRYSTAL.builtInRegistryHolder().key());
        List<ResourceKey<Block>> runes = new ArrayList<>();
        for (var key : BuiltInRegistries.BLOCK.keySet()) {
            if (!key.getNamespace().contains(Hemomancy.ID)) continue;
            Block block = BuiltInRegistries.BLOCK.get(key);
            if (block instanceof RuneBlock){
                if(block == HmBlocks.CHIMERIC_RUNE) continue;
                runes.add(block.builtInRegistryHolder().key());
            }
        }
        tag(RUNES).addAll(runes);
    }
}
