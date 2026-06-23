package ru.easynull.hemomancy.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.HmItems;
import ru.easynull.hemomancy.registry.blocks.RuneBlock;
import ru.easynull.hemomancy.registry.items.OrbItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class HmItemTagsProvider extends FabricTagProvider.ItemTagProvider {
    public static final TagKey<Item> ORBS = TagKey.create(Registries.ITEM, Hemomancy.path("orbs"));
    public static final TagKey<Item> RUNES = TagKey.create(Registries.ITEM, Hemomancy.path("runes"));

    public HmItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable BlockTagProvider blockTagProvider) {
        super(output, completableFuture, blockTagProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        List<ResourceKey<Item>> orbs = new ArrayList<>();
        List<ResourceKey<Item>> runes = new ArrayList<>();
        for (var key : BuiltInRegistries.ITEM.keySet()) {
            if (!key.getNamespace().contains(Hemomancy.ID)) continue;
            Item item = BuiltInRegistries.ITEM.get(key);
            if(item instanceof OrbItem orb) {
                if(orb == HmItems.INEXHAUSTIBLE_BLOOD_ORB) continue;
                orbs.add(item.builtInRegistryHolder().key());
            } else if (item instanceof BlockItem bi && bi.getBlock() instanceof RuneBlock block){
                if(block == HmBlocks.CHIMERIC_RUNE) continue;
                runes.add(item.builtInRegistryHolder().key());
            }
        }
        tag(ORBS).addAll(orbs);
        tag(RUNES).addAll(runes);
    }
}
