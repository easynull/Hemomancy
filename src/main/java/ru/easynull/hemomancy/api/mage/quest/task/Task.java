package ru.easynull.hemomancy.api.mage.quest.task;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class Task {
    protected final Type type;
    protected final ResourceLocation target;
    protected final int required;
    protected int progress;

    protected Task(Type type, ResourceLocation target, int required) {
        this.type = type;
        this.target = target;
        this.required = required;
        this.progress = 0;
    }

    public Type getType() { return type; }
    public ResourceLocation getTarget() { return target; }
    public int getRequired() { return required; }
    public int getProgress() { return progress; }
    public abstract Component getDescription();
    public void setProgress(int progress) { this.progress = Math.min(progress, required); }
    public boolean isCompleted() { return progress >= required; }

    public abstract boolean matches(Object eventTarget);

    public void increment(int amount) {
        progress = Math.min(progress + amount, required);
    }

    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("Type", type.name());
        nbt.putString("Target", target.toString());
        nbt.putInt("Required", required);
        nbt.putInt("Progress", progress);
        return nbt;
    }

    public static Task fromNbt(CompoundTag nbt) {
        Type type = Type.valueOf(nbt.getString("Type"));
        ResourceLocation target = ResourceLocation.tryParse(nbt.getString("Target"));
        int required = nbt.getInt("Required");
        int progress = nbt.getInt("Progress");
        Task task = switch (type) {
            case COLLECTOR -> new CollectorTask(target, required);
            case KILLER -> new KillerTask(target, required);
        };
        task.setProgress(progress);
        return task;
    }

    public enum Type{
        COLLECTOR,
        KILLER
    }
}
