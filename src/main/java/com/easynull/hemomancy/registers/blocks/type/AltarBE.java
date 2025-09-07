package com.easynull.hemomancy.registers.blocks.type;

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
        altar.tick();
        if (getMode().equals(getModes()[0])) {
            getRecipe().ifPresent(recipe -> {
                ItemStack input = getFirst();
                int max = calculateMaxCraftable(input, recipe);

                if (max <= 0 || getFirst().getCount() * recipe.result().getCount() >= 64) {
                    progress = 0;
                    crafting = false;
                    return;
                }

                long totalLp = recipe.lp() * input.getCount();
                long lpPer = (long) (20 * altar.getSpeed());

                if (progress >= totalLp) {
                    ItemStack output = recipe.result().copy();
                    output.setCount(output.getCount() * max);
                    setFirst(output);
                    progress = 0;
                    crafting = false;
                    return;
                }

                long lpTake = Math.min(getLp(this), lpPer);

                if (lpTake > 0) {
                    progress += lpTake;
                    reducerLp(-lpTake, this);
                    if (level instanceof ServerLevel sl) sl.sendParticles(DustParticleOptions.REDSTONE, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 1.0f, getBlockPos().getZ() + 0.5f, 1, 0.2, 0.0, 0.2, 0.0);
                } else {
                    if (level instanceof ServerLevel sl) sl.sendParticles(ParticleTypes.SMOKE, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 1.0f, getBlockPos().getZ() + 0.5f, 1, 0.2, 0.0, 0.2, 0.0);
                }
            });
        } else {
            EnergyUtils.extractInFrom(getInventory().getItem(0), this, (long) (altar.getCharging() * 25f), getMode().equals(getModes()[2]));
        }
    }

    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return crafting && getMode().equals(getModes()[0]);
    }

    private int calculateMaxCraftable(ItemStack input, AltarRecipe recipe) {
        ItemStack result = recipe.result();
        int inputLimit = input.getCount();

        int outputStackLimit = result.getMaxStackSize() / result.getCount();
        return Math.min(inputLimit, outputStackLimit);
    }

    public Optional<AltarRecipe> getRecipe() {
        AltarRecipe.Input input = new AltarRecipe.Input(this, altar.getTier());
        AltarRecipe recipe = RecipeUtils.getAltarRecipe(input, level);
        return recipe == null ? Optional.empty() : Optional.of(recipe);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        altar.load(tag);
        lp = tag.getLong("lp");
        progress = tag.getLong("progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        altar.save(tag);
        tag.putLong("lp", lp);
        tag.putLong("progress", progress);
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
}
