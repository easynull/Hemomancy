package ru.easynull.hemomancy.api.mage.quest.task;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public final class KillerTask extends Task {
    public KillerTask(ResourceLocation entityId, int required) {
        super(Task.Type.KILLER, entityId, required);
    }

    @Override
    public Component getDescription() {
        return Component.translatable("task.hemomancy.killer", BuiltInRegistries.ENTITY_TYPE.get(target).getDescription(), required - progress);
    }

    @Override
    public boolean matches(Object eventTarget) {
        if (eventTarget instanceof Entity entity) {
            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(target);
            return entity.getType() == type;
        }
        return false;
    }
}
