package ru.easynull.hemomancy.registry.blocks.type;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import ru.easynull.hemomancy.api.SidedBE;
import ru.easynull.hemomancy.api.Tickable;
import ru.easynull.hemomancy.api.altar.Altar;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.api.energy.Wandable;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.HmRecipes;
import ru.easynull.hemomancy.registry.recipes.AltarRecipe;
import ru.easynull.hemomancy.utils.EnergyUtils;

import java.util.Optional;

public final class AltarBE extends SidedBE implements Tickable, LpElement, Tierable, Wandable {
    final Altar altar;
    long lp, progress;
    boolean crafting;

    public AltarBE(BlockPos pos, BlockState state) {
        super(HmBlockEntities.BLOOD_ALTAR, pos, state, 1, 64);
        this.altar = new Altar(this);
    }

    @Override
    public void onTick() {
        World world = this.world;
        if (world == null || world.isClient) return;

        altar.onTick();

        crafting = false;
        ItemStack first = getStack(0);
        String mode = getMode();

        if (mode.equals(getModes()[0])) {  // crafting
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
                setStack(0, output);
                progress = 0;
                crafting = false;
                return;
            }

            long lpTake = Math.min(getLp(this), lpPer);
            if (lpTake > 0) {
                progress += lpTake;
                reduceLp(-lpTake, this);
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(DustParticleEffect.DEFAULT, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 1, 0.2, 0.0, 0.2, 0.0);
                }
            } else {
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 1, 0.2, 0.0, 0.2, 0.0);
                }
            }
        } else {
            EnergyUtils.extractInFrom(first, this, (long) (altar.getCharging() * 25f), mode.equals(getModes()[2]));
        }
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return world.getTime() % 10 == 0 && !crafting;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        Optional<AltarRecipe> recipeOpt = getRecipe();
        if (recipeOpt.isPresent()) {
            ItemStack result = recipeOpt.get().result();
            return getStack(0).getCount() * result.getCount() < 64;
        }
        return super.canInsert(slot, stack, dir);
    }

    private int calculateMaxCraftable(ItemStack input, AltarRecipe recipe) {
        ItemStack result = recipe.result();
        int inputLimit = input.getCount();
        int outputStackLimit = result.getMaxCount() / result.getCount();
        return Math.min(inputLimit, outputStackLimit);
    }

    public Optional<AltarRecipe> getRecipe() {
        return world.getRecipeManager().getFirstMatch(HmRecipes.ALTAR, new AltarRecipe.TirableInventory(1, getTier()), world);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        altar.load(nbt);
        lp = nbt.getLong("LP");
        progress = nbt.getLong("progress");
        crafting = nbt.getBoolean("crafting");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        altar.save(nbt);
        nbt.putLong("LP", lp);
        nbt.putLong("progress", progress);
        nbt.putBoolean("crafting", crafting);
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