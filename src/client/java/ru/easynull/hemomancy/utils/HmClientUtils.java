package ru.easynull.hemomancy.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//For fast porting
@Deprecated
public final class HmClientUtils {
    private static final Map<TagKey<?>, List<Item>> CACHE = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static Item cyclingItem(Level level, TagKey<?> tag, int intervalTicks) {
        List<Item> elements = CACHE.computeIfAbsent(tag, t -> {
            if (t.isFor(Registries.ITEM)) {
                return BuiltInRegistries.ITEM.getOrCreateTag((TagKey<Item>) t).stream().map(Holder::value).toList();
            } else if (t.isFor(Registries.BLOCK)) {
                return BuiltInRegistries.BLOCK.getOrCreateTag((TagKey<Block>) t).stream().map(Holder::value).map(Block::asItem).filter(item -> item != Items.AIR).toList();
            }
            return List.of();
        });

        if (elements.isEmpty()) {
            return Items.BARRIER;
        }

        int index = (int) ((level.getGameTime() / intervalTicks) % elements.size());
        return elements.get(index);
    }

//    public static Object iconParse(String id, int timePer) {
//        int tick = HemomancyClient.tickClient;
//        if (id == null || id.isBlank()) {
//            return ItemStack.EMPTY;
//        }
//        id = id.trim();
//
//        if (id.startsWith("#")) {
//            String tagString = id.substring(1);
//            ResourceLocation tagId = ResourceLocation.tryParse(tagString);
//            if (tagId == null) {
//                return ItemStack.EMPTY;
//            }
//
//            TagKey<Item> tag = TagKey.create(BuiltInRegistries.ITEM.key(), tagId);
//            var optEntries = BuiltInRegistries.ITEM.getTag(tag);
//            if (optEntries.isEmpty() || optEntries.get().unwrapKey().isEmpty() || optEntries.get().unwrapKey().stream().findAny().isEmpty()) {
//                return ItemStack.EMPTY;
//            }
//
//            var entries = optEntries.get();
//            int size = entries.size();
//
//            if (size == 0) {
//                return ItemStack.EMPTY;
//            }
//
//            int index;
//            if (timePer <= 0) {
//                index = tick % size;
//            } else {
//                index = (int) (((long) tick / timePer) % size);
//            }
//            if (index < 0) index = 0;
//            Item item = entries.get(index).value();
//            return new ItemStack(item);
//        } else {
//            ResourceLocation iconId = ResourceLocation.tryParse(id);
//            if (iconId == null) {
//                return ItemStack.EMPTY;
//            }
//
//            Item item = BuiltInRegistries.ITEM.get(iconId);
//            if (item != Items.AIR) {
//                return new ItemStack(item);
//            } else {
//                return iconId;
//            }
//        }
//    }
}
