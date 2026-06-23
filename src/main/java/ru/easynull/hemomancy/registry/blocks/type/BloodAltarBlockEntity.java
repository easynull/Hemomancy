package ru.easynull.hemomancy.registry.blocks.type;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import ru.easynull.hemomancy.api.ContainerBlockEntity;
import ru.easynull.hemomancy.api.altar.AltarConstructor;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.api.energy.Wandable;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.HmFluids;
import ru.easynull.hemomancy.registry.HmRecipes;
import ru.easynull.hemomancy.registry.recipes.FusionRecipe;
import ru.easynull.hemomancy.utils.EnergyUtils;

import java.util.Optional;

public class BloodAltarBlockEntity extends ContainerBlockEntity implements BlockEntityTicker<BloodAltarBlockEntity>, LpElement, Tierable, Wandable {
    private final AltarConstructor constructor;
    private long progress;
    private boolean crafting;
    private String currentMode = "crafting";

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.of(HmFluids.BLOOD);
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return constructor.getCapacity() * 81;
        }

        @Override
        protected boolean canInsert(FluidVariant variant) {
            return variant.isOf(HmFluids.BLOOD);
        }

        @Override
        public long insert(FluidVariant insertedVariant, long maxAmount, TransactionContext transaction) {
            StoragePreconditions.notBlankNotNegative(insertedVariant, maxAmount);

            if ((insertedVariant.equals(variant) || variant.isBlank()) && canInsert(insertedVariant)) {
                long insertedAmount = Math.min(maxAmount, getCapacity(insertedVariant) - amount * 81);

                if (insertedAmount > 0) {
                    updateSnapshots(transaction);

                    if (variant.isBlank()) {
                        variant = insertedVariant;
                        amount = insertedAmount / 81;
                    } else {
                        amount += insertedAmount / 81;
                    }

                    return insertedAmount;
                }
            }

            return 0;
        }

        @Override
        public long extract(FluidVariant extractedVariant, long maxAmount, TransactionContext transaction) {
            StoragePreconditions.notBlankNotNegative(extractedVariant, maxAmount);

            if (extractedVariant.equals(variant) && canExtract(extractedVariant)) {
                long extractedAmount = Math.min(maxAmount, amount * 81);

                if (extractedAmount > 0) {
                    updateSnapshots(transaction);
                    amount -= extractedAmount / 81;

                    if (amount == 0) {
                        variant = getBlankVariant();
                    }

                    return extractedAmount;
                }
            }

            return 0;
        }
    };

    public BloodAltarBlockEntity(BlockPos pos, BlockState state) {
        super(HmBlockEntities.BLOOD_ALTAR, pos, state, 1, 64);
        this.constructor = new AltarConstructor(this);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, BloodAltarBlockEntity blockEntity) {
        if (level == null || level.isClientSide) return;

        constructor.onTick();

        ItemStack inputStack = getItem(0);
        String mode = getMode();

        if (mode.equals(getModes()[0])) { // "crafting"
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

            if (fluidStorage.amount >= thisTickProgress && fluidStorage.variant.isOf(HmFluids.BLOOD)) {
                fluidStorage.amount -= thisTickProgress;
                progress += thisTickProgress;

                ((ServerLevel) level).sendParticles(DustParticleOptions.REDSTONE,
                        getX() + 0.5, getY() + 1.0, getZ() + 0.5,
                        1, 0.2, 0.0, 0.2, 0.0);

                if (progress >= totalRequiredLp) {
                    progress = 0;

                    ItemStack result = recipe.result().copy();
                    result.setCount(totalOutput);

                    setItem(0, result);
                }
            } else {
                ((ServerLevel) level).sendParticles(ParticleTypes.SMOKE,
                        getX() + 0.5, getY() + 1.0, getZ() + 0.5,
                        1, 0.2, 0.0, 0.2, 0.0);
            }
        } else {
            EnergyUtils.extractInFrom(inputStack, this, (long) (constructor.getCharging() * 25f), mode.equals(getModes()[2]));
        }
    }


    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("FluidStorage")) {
            SingleVariantStorage.readNbt(fluidStorage, FluidVariant.CODEC, ()-> FluidVariant.of(HmFluids.BLOOD), tag.getCompound("FluidStorage"), registries);
        }

        constructor.load(tag);
        progress = tag.getLong("Progress");
        crafting = tag.getBoolean("Crafting");
        if (tag.contains("AltarMode")) {
            currentMode = tag.getString("AltarMode");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);

        constructor.save(nbt);

        CompoundTag fluidNbt = new CompoundTag();
        SingleVariantStorage.writeNbt(fluidStorage, FluidVariant.CODEC, fluidNbt, registries);
        nbt.put("FluidStorage", fluidNbt);

        nbt.putLong("Progress", progress);
        nbt.putBoolean("Crafting", crafting);
        nbt.putString("AltarMode", currentMode);
    }

    public Optional<FusionRecipe> getRecipe() {
        return level.getRecipeManager().getRecipeFor(HmRecipes.FUSION, new SingleRecipeInput(getFirst()), level).map(RecipeHolder::value);
    }

    public String getMode() {
        return this.currentMode;
    }

    public void setMode(String mode) {
        this.currentMode = mode;
        setChanged();
    }

    @Override
    public String[] getModes() {
        return new String[]{"crafting", "dominant", "recessive"};
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
    public long getLp(Object object) {
        return fluidStorage.amount;
    }

    @Override
    public boolean canDaggerFulled() {
        return true;
    }

    @Override
    public boolean reduceLp(long amount, Object target) {
        if (getMaxLp() <= 0) return false;

        long current = getLp(target);
        long newAmount = Mth.clamp(current + amount, 0L, getMaxLp());
        fluidStorage.amount = newAmount;
        sync(this);

        return newAmount != getMaxLp();
    }
}