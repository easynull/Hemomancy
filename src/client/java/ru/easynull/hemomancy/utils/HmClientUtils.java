package ru.easynull.hemomancy.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import ru.easynull.hemomancy.HemomancyClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public final class HmClientUtils {
    private static final Map<TagKey<Item>, List<Item>> CACHE = new HashMap<>();

    public static Item getCyclingItem(World world, TagKey<Item> tag, int intervalTicks) {
        var blocks = CACHE.computeIfAbsent(tag, t -> Registries.ITEM.getOrCreateEntryList(t).stream().map(RegistryEntry::value).toList());
        if (blocks.isEmpty()) return Items.BEDROCK;
        int index = (int) ((world.getTime() / intervalTicks) % blocks.size());
        return blocks.get(index);
    }

    public static Object iconParse(String id, int timePer) {
        int tick = HemomancyClient.tickClient;
        if (id == null || id.isBlank()) {
            return ItemStack.EMPTY;
        }
        id = id.trim();

        if (id.startsWith("#")) {
            String tagString = id.substring(1);
            Identifier tagId = Identifier.tryParse(tagString);
            if (tagId == null) {
                return ItemStack.EMPTY;
            }

            TagKey<Item> tag = TagKey.of(Registries.ITEM.getKey(), tagId);
            var optEntries = Registries.ITEM.getEntryList(tag);
            if (optEntries.isEmpty() || optEntries.get().getTagKey().isEmpty() || optEntries.get().getTagKey().stream().findAny().isEmpty()) {
                return ItemStack.EMPTY;
            }

            var entries = optEntries.get();
            int size = entries.size();

            if (size == 0) {
                return ItemStack.EMPTY;
            }

            int index;
            if (timePer <= 0) {
                index = tick % size;
            } else {
                index = (int) (((long) tick / timePer) % size);
            }
            if (index < 0) index = 0;
            Item item = entries.get(index).value();
            return new ItemStack(item);
        } else {
            Identifier iconId = Identifier.tryParse(id);
            if (iconId == null) {
                return ItemStack.EMPTY;
            }

            Item item = Registries.ITEM.get(iconId);
            if (item != Items.AIR) {
                return new ItemStack(item);
            } else {
                return iconId;
            }
        }
    }
}
