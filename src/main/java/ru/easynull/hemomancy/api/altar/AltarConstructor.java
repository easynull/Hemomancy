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
import ru.easynull.hemomancy.registry.blocks.type.BloodAltarBlockEntity;
import ru.easynull.hemomancy.utils.HmCommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AltarConstructor {
    private final Map<RuneBlock.Type, Integer> runes = new HashMap<>();
    private final BloodAltarBlockEntity altar;

    private byte tier;
    private long capacity;
    private int sacrifices;
    private float speed;
    private float charging;
    private float resCapacity;
    private String mode;

    public AltarConstructor(BloodAltarBlockEntity altar) {
        this.altar = altar;
        this.mode = altar.getModes()[0];
    }

    public boolean isValidMonument(byte checkTier) {
        var structure = TierManager.getTiers().get(checkTier);
        if (structure == null) return false;

        BlockPos altarPos = altar.getPos();
        World world = altar.getWorld();
        if (world == null) return false;

        boolean valid = true;

        for (TierManager.Component component : structure) {
            BlockPos componentPos = altarPos.add(component.pos.getX(), component.pos.getY(), component.pos.getZ());
            BlockState state = world.getBlockState(componentPos);

            if (component.state != null) {
                if (component.state.getBlock() == HmBlocks.BLANK_RUNE) {
                    if (!(state.getBlock() instanceof RuneBlock)) {
                        valid = false;
                    }
                } else {
                    if (!component.stateble) {
                        if (state.getBlock() != component.state.getBlock()) {
                            valid = false;
                        }
                    } else {
                        if (!component.state.equals(state)) {
                            valid = false;
                        }
                    }
                }
            } else {
                if (state.isAir() || HmCommonUtils.isFluid(state)) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    public void upgradeAltar() {
        World world = altar.getWorld();
        if (world == null || world.isClient || world.getTime() % 40 != 0) return;

        BlockPos pos = altar.getPos();
        byte highestValidTier = 1;
        for (byte t : TierManager.getTiers().keySet()) {
            if (isValidMonument(t)) {
                highestValidTier = t;
            }
        }

        if (tier != highestValidTier) {
            setTier(highestValidTier);
        }

        runes.clear();

        List<TierManager.Component> components = getComponents();
        for (TierManager.Component component : components) {
            BlockPos cPos = pos.add(component.pos);
            BlockState state = world.getBlockState(cPos);

            if(component.state != null) {
                if (component.state.isOf(HmBlocks.BLANK_RUNE) && state.getBlock() instanceof RuneBlock rune) {
                    if (component.isUniversal() && rune.getPrimaryType() != RuneBlock.Type.NONE) {
                        for (RuneBlock.Type type : rune.getTypes()) {
                            addUpgrade(type, rune.getTier());
                        }
                    }
                }
            }
        }

        float newSpeed = 0.5f + getUpgrade(RuneBlock.Type.SPEED) * (float)RuneBlock.RUNE_VALUES.get(RuneBlock.Type.SPEED);
        int newSacrifices = (int) (getUpgrade(RuneBlock.Type.SACRIFICES) * (float)RuneBlock.RUNE_VALUES.get(RuneBlock.Type.SACRIFICES));
        float newResCapacity = 1f + getUpgrade(RuneBlock.Type.RESONANT_CAPACITY) * (float)RuneBlock.RUNE_VALUES.get(RuneBlock.Type.RESONANT_CAPACITY);
        float newCharging = 1f + getUpgrade(RuneBlock.Type.RELATIONS) * (float)RuneBlock.RUNE_VALUES.get(RuneBlock.Type.RELATIONS);

        long newCapacity = (long) ((highestValidTier * 5000L + ((long) getUpgrade(RuneBlock.Type.CAPACITY) * (int)RuneBlock.RUNE_VALUES.get(RuneBlock.Type.CAPACITY))) * newResCapacity);

        if (tier == 1) {
            newSpeed = 0.5f;
            newSacrifices = 0;
            newResCapacity = 1f;
            newCharging = 1f;
            newCapacity = 5000L;
        }

        setSpeed(newSpeed);
        setSacrifices(newSacrifices);
        setResCapacity(newResCapacity);
        setCharging(newCharging);
        setCapacity(newCapacity);

        long currentLp = altar.getLp(altar);
        if (currentLp > newCapacity) {
            altar.reduceLp(newCapacity, altar);
        }
    }

    public void onTick() {
        upgradeAltar();

        if (sacrifices > 0) {
            World world = altar.getWorld();
            if (world == null || world.isClient) return;

            List<LivingEntity> nearby = HmCommonUtils.getNearbyLivingEntities(world, altar.getPos(), 3.0);

            for (LivingEntity entity : nearby) {
                if (!entity.isAlive()) {
                    float healthMultiplier = (entity instanceof PlayerEntity) ? 3f : 1f;
                    long lpGain = (long) (entity.getMaxHealth() * sacrifices * healthMultiplier);

                    altar.reduceLp(lpGain, altar);

                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(DustParticleEffect.DEFAULT, entity.getX() + 0.5, entity.getY() + 0.5, entity.getZ() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
                    }
                }
            }
        }
    }

    public List<TierManager.Component> getComponents() {
        return TierManager.getTiers().get(tier);
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
        if (tier != newTier) {
            tier = newTier;
            HmCommonUtils.syncBlockEntity(altar);
        }
    }

    public void setSpeed(float newSpeed) {
        if (speed != newSpeed) {
            speed = newSpeed;
            HmCommonUtils.syncBlockEntity(altar);
        }
    }

    public void setCapacity(long newCapacity) {
        if (capacity != newCapacity) {
            capacity = newCapacity;
            HmCommonUtils.syncBlockEntity(altar);
        }
    }

    public void setResCapacity(float newResCapacity) {
        if (resCapacity != newResCapacity) {
            resCapacity = newResCapacity;
            HmCommonUtils.syncBlockEntity(altar);
        }
    }

    public void setSacrifices(int newSacrifices) {
        if (sacrifices != newSacrifices) {
            sacrifices = newSacrifices;
            HmCommonUtils.syncBlockEntity(altar);
        }
    }

    public void setCharging(float newCharging) {
        if (charging != newCharging) {
            charging = newCharging;
            HmCommonUtils.syncBlockEntity(altar);
        }
    }

    public void setMode(String newMode) {
        if (!mode.equals(newMode)) {
            mode = newMode;
            HmCommonUtils.syncBlockEntity(altar);
        }
    }

    private int getUpgrade(RuneBlock.Type type) {
        return runes.getOrDefault(type, 0);
    }

    private void addUpgrade(RuneBlock.Type type, int count) {
        runes.merge(type, count, Integer::sum);
    }

    public void load(NbtCompound tag) {
        tier = tag.getByte("Tier");
        capacity = tag.getLong("Capacity");
        resCapacity = tag.getFloat("ResonantCapacity");
        sacrifices = tag.getInt("Sacrifices");
        speed = tag.getFloat("Speed");
        charging = tag.getFloat("Charging");
        mode = tag.getString("Mode");
    }

    public void save(NbtCompound tag) {
        tag.putByte("Tier", tier);
        tag.putLong("Capacity", capacity);
        tag.putFloat("ResonantCapacity", resCapacity);
        tag.putInt("Sacrifices", sacrifices);
        tag.putFloat("Speed", speed);
        tag.putFloat("Charging", charging);
        tag.putString("Mode", mode);
    }
}