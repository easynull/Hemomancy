package ru.easynull.hemomancy.api.altar;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.blocks.RuneBlock;
import ru.easynull.hemomancy.registry.blocks.type.AltarBE;
import ru.easynull.hemomancy.utils.HmUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Altar {
    private final Map<RuneBlock.Type, Integer> runes = new HashMap<>();
    private final AltarBE altar;

    private byte tier;
    private long capacity;
    private int sacrifices;
    private float speed;
    private float charging;
    private float resCapacity;
    private String mode;

    public Altar(AltarBE altar) {
        this.altar = altar;
        this.mode = altar.getModes()[0];
    }

    public boolean isValidMonument(byte checkTier) {
        var structure = Tier.getTiers().get(checkTier);
        if (structure == null) return false;

        BlockPos altarPos = altar.getPos();
        World world = altar.getWorld();
        if (world == null) return false;

        boolean valid = true;

        for (Tier.Component component : structure) {
            BlockPos componentPos = altarPos.add(component.pos.getX(), component.pos.getY(), component.pos.getZ());
            BlockState state = world.getBlockState(componentPos);

            if (component.block != null) {
                if (component.block == HmBlocks.BLANK_RUNE) {
                    if (!(state.getBlock() instanceof RuneBlock)) {
                        valid = false;
                    }
                } else if (!component.block.getDefaultState().isOf(state.getBlock())) {
                    valid = false;
                }
            } else {
                if (state.isAir() || HmUtils.isFluid(state)) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    public void onUpgrade() {
        World world = altar.getWorld();
        if (world == null || world.isClient) return;

        if (world.getTime() % 20 != 0) return;

        BlockPos pos = altar.getPos();

        byte highestValidTier = 1;
        for (byte t : Tier.getTiers().keySet()) {
            if (isValidMonument(t)) {
                highestValidTier = t;
            }
        }

        setTier(highestValidTier);
        runes.clear();

        List<Tier.Component> components = getComponents();
        for (Tier.Component component : components) {
            BlockPos cPos = pos.add(component.pos);
            BlockState state = world.getBlockState(cPos);

            if (component.block == HmBlocks.BLANK_RUNE && state.getBlock() instanceof RuneBlock rune) {
                if (component.isUpgrade() && rune.getPrimaryType() != RuneBlock.Type.NONE) {
                    for (RuneBlock.Type type : rune.getTypes()) {
                        addUpgrade(type, rune.getTier());
                    }
                }
            }
        }

        // базовые значения для tier 1
        if (getTier() == 1) {
            setSpeed(0.5f);
            setSacrifices(0);
            setResCapacity(1f);
            setCharging(1f);
        } else {
            // модификаторы от рун
            setSpeed(0.5f + getUpgrade(RuneBlock.Type.SPEED) * 0.08f);
            setSacrifices((int) (getUpgrade(RuneBlock.Type.SACRIFICES) * 0.6f));
            setResCapacity(1f + getUpgrade(RuneBlock.Type.RESONANT_CAPACITY) * 0.1f);
            setCharging(1f + getUpgrade(RuneBlock.Type.RELATIONS) * 0.15f);
        }

        long newCapacity = (long) ((getTier() * 5000L + (getUpgrade(RuneBlock.Type.CAPACITY) * 1500L)) * getResCapacity());
        setCapacity(newCapacity);

        // если LP превышает новую ёмкость — обрезаем
        long currentLp = altar.getLp(altar);
        if (currentLp > getCapacity()) {
            altar.reduceLp(getCapacity(), altar);
        }
    }

    public void onTick() {
        onUpgrade();
        if (getSacrifices() > 0) {
            World world = altar.getWorld();
            if (world == null || world.isClient) return;

            List<LivingEntity> nearby = HmUtils.getNearbyLivingEntities(world, altar.getPos(), 3.0);

            for (LivingEntity entity : nearby) {
                if (!entity.isAlive()) {
                    float healthMultiplier = (entity instanceof PlayerEntity) ? 3f : 1f;
                    long lpGain = (long) (entity.getMaxHealth() * getSacrifices() * healthMultiplier);

                    altar.reduceLp(lpGain, altar);

                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(DustParticleEffect.DEFAULT, entity.getX() + 0.5, entity.getY() + 0.5, entity.getZ() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
                    }
                }
            }
        }
    }

    public List<Tier.Component> getComponents() {
        return Tier.getTiers().get(getTier());
    }

    public byte getTier() {
        return tier;
    }

    public float getSpeed() {
        return speed;
    }

    public long getCapacity() {
        return capacity;
    }

    public float getResCapacity() {
        return resCapacity;
    }

    public int getSacrifices() {
        return sacrifices;
    }

    public float getCharging() {
        return charging;
    }

    public String getMode() {
        return mode;
    }

    public void setTier(byte newTier) {
        this.tier = newTier;
        altar.markDirty();
        if (!altar.getWorld().isClient) {
            altar.getWorld().updateListeners(altar.getPos(), altar.getCachedState(), altar.getCachedState(), 3);
        }
    }

    public void setSpeed(float newSpeed) {
        this.speed = newSpeed;
        altar.markDirty();
        if (!altar.getWorld().isClient) {
            altar.getWorld().updateListeners(altar.getPos(), altar.getCachedState(), altar.getCachedState(), 3);
        }
    }

    public void setCapacity(long newCapacity) {
        this.capacity = newCapacity;
        altar.markDirty();
        if (!altar.getWorld().isClient) {
            altar.getWorld().updateListeners(altar.getPos(), altar.getCachedState(), altar.getCachedState(), 3);
        }
    }

    public void setResCapacity(float newResCapacity) {
        this.resCapacity = newResCapacity;
        altar.markDirty();
        if (!altar.getWorld().isClient) {
            altar.getWorld().updateListeners(altar.getPos(), altar.getCachedState(), altar.getCachedState(), 3);
        }
    }

    public void setSacrifices(int newSacrifices) {
        this.sacrifices = newSacrifices;
        altar.markDirty();
        if (!altar.getWorld().isClient) {
            altar.getWorld().updateListeners(altar.getPos(), altar.getCachedState(), altar.getCachedState(), 3);
        }
    }

    public void setCharging(float newCharging) {
        this.charging = newCharging;
        altar.markDirty();
        if (!altar.getWorld().isClient) {
            altar.getWorld().updateListeners(altar.getPos(), altar.getCachedState(), altar.getCachedState(), 3);
        }
    }

    public void setMode(String newMode) {
        this.mode = newMode;
        altar.markDirty();
        if (!altar.getWorld().isClient) {
            altar.getWorld().updateListeners(altar.getPos(), altar.getCachedState(), altar.getCachedState(), 3);
        }
    }

    private int getUpgrade(RuneBlock.Type type) {
        return runes.getOrDefault(type, 0);
    }

    private void addUpgrade(RuneBlock.Type type, int count) {
        runes.merge(type, count, Integer::sum);
    }

    public void load(NbtCompound tag) {
        this.tier = tag.getByte("tier");
        this.capacity = tag.getLong("capacity");
        this.resCapacity = tag.getFloat("resonantCapacity");
        this.sacrifices = tag.getInt("sacrifices");
        this.speed = tag.getFloat("speed");
        this.charging = tag.getFloat("charging");
        this.mode = tag.getString("mode");
    }

    public void save(NbtCompound tag) {
        tag.putByte("tier", tier);
        tag.putLong("capacity", capacity);
        tag.putFloat("resonantCapacity", resCapacity);
        tag.putInt("sacrifices", sacrifices);
        tag.putFloat("speed", speed);
        tag.putFloat("charging", charging);
        tag.putString("mode", mode);
    }
}