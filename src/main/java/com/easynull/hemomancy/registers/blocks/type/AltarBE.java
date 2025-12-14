package com.easynull.hemomancy.registers.blocks.type;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.core.Wandable;
import com.easynull.hemomancy.core.Tierable;
import com.easynull.hemomancy.core.altar.Altar;
import com.easynull.hemomancy.registers.HcBlockEntities;
import com.easynull.hemomancy.registers.recipes.AltarRecipe;
import com.easynull.hemomancy.utils.EnergyUtils;
import com.easynull.hemomancy.utils.RecipeUtils;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import com.mw.nullcore.core.blocks.type.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class AltarBE extends ContainerBlockEntity implements Tickable, LpElement, Tierable, Wandable {
    final Altar altar;
    long lp, progress;
    boolean crafting;

    public AltarBE(BlockPos pos, BlockState state) {
        super(HcBlockEntities.bloodAltar.get(), pos, state, 1, 64);
        this.altar = new Altar(this);
    }

    @Override
    public void tick() {
        if (level.isClientSide()) return;
        altar.tick();
        crafting = false;
        ItemStack first = getItem(0);
        String mode = getMode();
        if (mode.equals(getModes()[0])) {
            Optional<AltarRecipe> recipeOpt = getRecipe();
            if (recipeOpt.isEmpty()) {
                progress = 0;
                return;
            }
            AltarRecipe recipe = recipeOpt.get();
            int max = calculateMaxCraftable(first, recipe);
            crafting = true;
            if (max <= 0 || first.getCount() * recipe.result().getCount() > 64) {
                progress = 0;
                crafting = false;
                return;
            }
            long totalLp = recipe.lp() * first.getCount();
            long lpPer = (long) (20 * altar.getSpeed());
            if (progress >= totalLp) {
                ItemStack output = recipe.result().copy();
                output.setCount(output.getCount() * max);
                setItem(0, output);
                progress = 0;
                crafting = false;
                return;
            }
            long lpTake = Math.min(getLp(this), lpPer);
            if (lpTake > 0) {
                progress += lpTake;
                reducerLp(-lpTake, this);
                if (level instanceof ServerLevel sl) sl.sendParticles(DustParticleOptions.REDSTONE, worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5, 1, 0.2, 0.0, 0.2, 0.0);
            } else {
                if (level instanceof ServerLevel sl) sl.sendParticles(ParticleTypes.SMOKE, worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5, 1, 0.2, 0.0, 0.2, 0.0);
            }
        } else {
            EnergyUtils.extractInFrom(first, this, (long) (altar.getCharging() * 25f), mode.equals(getModes()[2]));
        }
    }

    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return level.getGameTime() % 10 == 0 && !crafting;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Optional<AltarRecipe> recipeOpt = getRecipe();
        if (recipeOpt.isPresent()) {
            ItemStack result = recipeOpt.get().result();
            return getItem(0).getCount() * result.getCount() < 64;
        }
        return super.canPlaceItem(slot, stack);
    }

    private int calculateMaxCraftable(ItemStack input, AltarRecipe recipe) {
        ItemStack result = recipe.result();
        int inputLimit = input.getCount();
        int outputStackLimit = result.getMaxStackSize() / result.getCount();
        return Math.min(inputLimit, outputStackLimit);
    }

    public Optional<AltarRecipe> getRecipe() {
        return Optional.ofNullable(RecipeUtils.getAltarRecipe(new AltarRecipe.Input(this, getTier()), level));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        altar.load(tag);
        lp = tag.getLong("LP");
        progress = tag.getLong("progress");
        crafting = tag.getBoolean("crafting");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        altar.save(tag);
        tag.putLong("LP", lp);
        tag.putLong("progress", progress);
        tag.putBoolean("crafting", crafting);
    }

    @Override
    public long getMaxLp() {
        return altar.getCapacity();
    }

    @Override
    public byte getTier() {
        return altar.getTier();
    }

    @Override
    public String[] getModes() {
        return new String[]{"crafting", "dominant", "recessive"};
    }

    @Override
    public String getMode() {
        return altar.getMode();
    }

    @Override
    public void setMode(String mode) {
        altar.setMode(mode);
    }

    @Override
    public boolean canDaggerFulled() {
        return true;
    }
}