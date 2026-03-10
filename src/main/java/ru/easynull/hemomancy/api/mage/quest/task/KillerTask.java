package ru.easynull.hemomancy.api.mage.quest.task;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class KillerTask extends Task {
    public KillerTask(Identifier entityId, int required) {
        super(Task.Type.KILLER, entityId, required);
    }

    @Override
    public Text getDescription() {
        return Text.translatable("task.hemomancy.killer", Registries.ENTITY_TYPE.get(target).getName(), required - progress);
    }

    @Override
    public boolean matches(Object eventTarget) {
        if (eventTarget instanceof Entity entity) {
            EntityType<?> type = Registries.ENTITY_TYPE.get(target);
            return entity.getType() == type;
        }
        return false;
    }
}
