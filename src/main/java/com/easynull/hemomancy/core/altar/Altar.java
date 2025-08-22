package com.easynull.hemomancy.core.altar;

import com.easynull.hemomancy.registers.HcElements;
import com.easynull.hemomancy.registers.blocks.RuneBlock;
import com.easynull.hemomancy.registers.blocks.type.AltarBE;
import com.mw.nullcore.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Altar {
    private Map<RuneBlock.Type, Integer> runes = new HashMap<>();
    final AltarBE altar;
    byte tier;
    long capacity;
    int sacrifices, chargingRadius;
    float speed, charging, resCapacity;

    public Altar(AltarBE altar) {
        this.altar = altar;
    }

    public boolean validStructure(byte tier) {
        var structure = Tier.getTiers().get(tier);
        var pos = altar.getBlockPos();
        boolean isValid = true;
        Level level = altar.getLevel();
        for (Tier.Component component : structure) {
            BlockPos cPos = pos.offset(component.pos);
            BlockState state = level.getBlockState(cPos);
            if (component.block != null) {
                if (component.block == HcElements.rune.get()) {
                    if (!(state.getBlock() instanceof RuneBlock)) {
                        isValid = false;
                    }
                } else if (!component.block.defaultBlockState().is(state.getBlock())) {
                    isValid = false;
                }
            } else if (state.isAir() || Utils.Block.isFluid(state)) {
                isValid = false;
            }
        }
        return isValid;
    }

    public void upgradeAltar() {
        var pos = altar.getBlockPos();
        Level level = altar.getLevel();
        byte validTier = 1;

        for (byte tier : Tier.getTiers().keySet()) {
            if (validStructure(tier)) validTier = tier;
        }
        setTier(validTier);
        runes.clear();

        for (Tier.Component component : getComponents()) {
            BlockPos cPos = pos.offset(component.pos);
            BlockState state = level.getBlockState(cPos);
            if (component.block == HcElements.rune.get() && state.getBlock() instanceof RuneBlock rune) {
                if (component.isUpgrade() && rune.getType() != RuneBlock.Type.none) {
                    for (RuneBlock.Type type : rune.getTypes()) {
                        addUpgrade(type, rune.getMultp());
                    }
                }
            }
        }

        if (getTier() == 1) {
            setSpeed(0.1f);
            setSacrifices(0);
            setResCapacity(1f);
            setCharging(0);
            setChargingRadius(0);
        } else {
            setSpeed(getUpgrade(RuneBlock.Type.speed) * 0.09f);
            setSacrifices((int) (getUpgrade(RuneBlock.Type.sacrifices) * 0.09f));
            setResCapacity(1f + getUpgrade(RuneBlock.Type.resonantCapacity) * 0.09f);
            setCharging(getUpgrade(RuneBlock.Type.relations) * 0.09f);
            setChargingRadius((int) (getUpgrade(RuneBlock.Type.relations) * 0.27f));
        }
        setCapacity((int) ((getTier() * 5000 + (getUpgrade(RuneBlock.Type.capacity) * 1500)) * resCapacity));
    }

    public void tick() {
        upgradeAltar();
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

    public int getChargingRadius() {
        return chargingRadius;
    }

    public void setTier(byte tier) {
        this.tier = tier;
        Utils.Block.updateBlockEntity(altar);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        Utils.Block.updateBlockEntity(altar);
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
        Utils.Block.updateBlockEntity(altar);
    }

    public void setResCapacity(float resCapacity) {
        this.resCapacity = resCapacity;
        Utils.Block.updateBlockEntity(altar);
    }

    public void setSacrifices(int sacrifices) {
        this.sacrifices = sacrifices;
        Utils.Block.updateBlockEntity(altar);
    }

    public void setCharging(float charging) {
        this.charging = charging;
        Utils.Block.updateBlockEntity(altar);
    }

    public void setChargingRadius(int chargingRadius) {
        this.chargingRadius = chargingRadius;
        Utils.Block.updateBlockEntity(altar);
    }

    private int getUpgrade(RuneBlock.Type type) {
        if (runes == null) return 0;
        return runes.getOrDefault(type, 0);
    }

    private void addUpgrade(RuneBlock.Type type, int count) {
        if (runes.containsKey(type)) runes.put(type, runes.get(type) + count);
        else runes.put(type, count);
    }

    public void load(CompoundTag tag) {
        this.tier = tag.getByte("tier");
        this.capacity = tag.getLong("capacity");
        this.resCapacity = tag.getFloat("resonantCapacity");
        this.sacrifices = tag.getInt("sacrifices");
        this.chargingRadius = tag.getInt("chargingRadius");
        this.speed = tag.getFloat("speed");
        this.charging = tag.getFloat("charging");
    }

    public void save(CompoundTag tag) {
        tag.putByte("tier", tier);
        tag.putLong("capacity", capacity);
        tag.putFloat("resonantCapacity", resCapacity);
        tag.putInt("sacrifices", sacrifices);
        tag.putInt("chargingRadius", chargingRadius);
        tag.putFloat("speed", speed);
        tag.putFloat("charging", charging);
    }
}
