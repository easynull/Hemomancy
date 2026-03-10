package ru.easynull.hemomancy.api.mage.quest.task;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class CollectorTask extends Task {
    public CollectorTask(Identifier itemId, int required) {
        super(Type.COLLECTOR, itemId, required);
    }

    @Override
    public Text getDescription() {
        return Text.translatable("task.hemomancy.collector", Registries.ITEM.get(target).getName(), required);
    }

    @Override
    public boolean matches(Object eventTarget) {
        return false;
    }

    public boolean tryComplete(PlayerEntity player) {
        Item item = Registries.ITEM.get(target);
        int count = player.getInventory().count(item);
        if (count >= required) {
            player.getInventory().removeStack(player.getInventory().getSlotWithStack(item.getDefaultStack()), required);
            return true;
        }
        return false;
    }
}