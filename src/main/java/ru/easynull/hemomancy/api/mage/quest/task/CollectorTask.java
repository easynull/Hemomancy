package ru.easynull.hemomancy.api.mage.quest.task;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public final class CollectorTask extends Task {
    public CollectorTask(ResourceLocation itemId, int required) {
        super(Type.COLLECTOR, itemId, required);
    }

    @Override
    public Component getDescription() {
        return Component.translatable("task.hemomancy.collector", BuiltInRegistries.ITEM.get(target).getDescription(), required);
    }

    @Override
    public boolean matches(Object eventTarget) {
        return false;
    }

    public boolean tryComplete(Player player) {
        Item item = BuiltInRegistries.ITEM.get(target);
        int count = player.getInventory().countItem(item);
        if (count >= required) {
            player.getInventory().removeItem(player.getInventory().findSlotMatchingItem(item.getDefaultInstance()), required);
            return true;
        }
        return false;
    }
}