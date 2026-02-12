package ru.easynull.hemomancy.registry.blocks.type;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import ru.easynull.hemomancy.api.InventoryBE;
import ru.easynull.hemomancy.api.Tickable;
import ru.easynull.hemomancy.api.altar.AltarConstructor;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.api.energy.Wandable;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.HmRecipes;
import ru.easynull.hemomancy.registry.recipes.FusionRecipe;
import ru.easynull.hemomancy.utils.EnergyUtils;

import java.util.Optional;

public final class BloodAltarBE extends InventoryBE implements Tickable, LpElement, Tierable, Wandable {
    private final AltarConstructor constructor;
    private long lp, progress;
    private boolean crafting;

    public BloodAltarBE(BlockPos pos, BlockState state) {
        super(HmBlockEntities.BLOOD_ALTAR, pos, state, 1, 64);
        this.constructor = new AltarConstructor(this);
    }

    @Override
    public void onTick() {
        if (world == null || world.isClient) return;

        constructor.onTick();

        ItemStack inputStack = getStack(0);
        String mode = getMode();

        if (mode.equals(getModes()[0])) { // crafting
            if (inputStack.isEmpty()) {
                crafting = false;
                progress = 0;
                return;
            }

            Optional<FusionRecipe> opt = getRecipe();
            if (opt.isEmpty() || opt.get().tier() > getTier()) {
                crafting = false;
                progress = 0;
                return;
            }

            FusionRecipe recipe = opt.get();

            int inputCount = inputStack.getCount();
            int outputCountPerCraft = recipe.result().getCount();
            int totalOutput = inputCount * outputCountPerCraft;

            if (totalOutput > 64) {
                crafting = false;
                progress = 0;
                return;
            }

            long requiredLpPerItem = recipe.lp();
            long totalRequiredLp = (long) inputCount * requiredLpPerItem;

            crafting = true;

            long thisTickProgress = (long) (20 * constructor.getSpeed());

            if (lp >= thisTickProgress) {
                reduceLp(-thisTickProgress, this);
                progress += thisTickProgress;
                ((ServerWorld)world).spawnParticles(DustParticleEffect.DEFAULT, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 1, 0.2, 0.0, 0.2, 0.0);
                if (progress >= totalRequiredLp) {
                    progress = 0;

                    ItemStack result = recipe.result().copy();
                    result.setCount(totalOutput);

                    setStack(0, result);
                }
            } else {
                ((ServerWorld)world).spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 1, 0.2, 0.0, 0.2, 0.0);
            }
        } else {
            EnergyUtils.extractInFrom(inputStack, this, (long) (constructor.getCharging() * 25f), mode.equals(getModes()[2]));
        }
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return world.getTime() % 10 == 0 && !crafting;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        Optional<FusionRecipe> recipeOpt = getRecipe();
        if (recipeOpt.isPresent()) {
            ItemStack result = recipeOpt.get().result();
            return getStack(1).getCount() * result.getCount() < 64;
        }
        return super.canInsert(slot, stack, dir);
    }

    public Optional<FusionRecipe> getRecipe() {
        return world.getRecipeManager().getFirstMatch(HmRecipes.FUSION, getInventory(), world);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        lp = nbt.getLong("LP");
        constructor.load(nbt);
        progress = nbt.getLong("Progress");
        crafting = nbt.getBoolean("Crafting");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        constructor.save(nbt);
        nbt.putLong("LP", lp);
        nbt.putLong("Progress", progress);
        nbt.putBoolean("Crafting", crafting);
    }

    @Override
    public long getMaxLp() {
        return constructor.getCapacity();
    }

    @Override
    public byte getTier() {
        return constructor.getTier();
    }

    @Override
    public String[] getModes() {
        return new String[]{"crafting", "dominant", "recessive"};
    }

    @Override
    public String getMode() {
        return constructor.getMode();
    }

    @Override
    public void setMode(String mode) {
        constructor.setMode(mode);
    }

    @Override
    public boolean canDaggerFulled() {
        return true;
    }
}