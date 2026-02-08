package ru.easynull.hemomancy.registry.blocks.type;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import ru.easynull.hemomancy.api.SidedBE;
import ru.easynull.hemomancy.api.Tickable;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.HmRecipes;
import ru.easynull.hemomancy.registry.items.OrbItem;
import ru.easynull.hemomancy.registry.recipes.AlchemyRecipe;
import ru.easynull.hemomancy.utils.HmUtils;

import java.util.List;
import java.util.Optional;

public final class AlchemyTableBE extends SidedBE implements Tickable, LpElement, Tierable {
    public long progress, needLP;
    public boolean crafting;

    public AlchemyTableBE(BlockPos pos, BlockState state) {
        super(HmBlockEntities.ALCHEMY_TABLE, pos, state, 16, 64);
    }

    @Override
    public void onTick() {
        Optional<AlchemyRecipe> recipeOpt = getRecipe();
        if (recipeOpt.isEmpty()) {
            if (crafting) resetCrafting();
            return;
        }
        AlchemyRecipe recipe = recipeOpt.get();
        if (!crafting) setProgress(0, true, recipe.lp());
        ItemStack orb = getStack(0);
        if (!(orb.getItem() instanceof OrbItem orbItem)) {
            resetCrafting();
            return;
        }
        long space = (long) (recipe.lp() * 0.007f);
        if (orbItem.getLp(orb) <= space) return;
        orbItem.reduceLp(-space, orb);
        setProgress(progress += space, true, recipe.lp());
        if (world instanceof ServerWorld sl) sl.spawnParticles(DustParticleEffect.DEFAULT, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 1, 0.2, 0.0, 0.2, 0.0);
        if (progress >= recipe.lp()) completeRecipe(recipe);
    }

    private void completeRecipe(AlchemyRecipe recipe) {
        List<Ingredient> inputs = recipe.inputs();
        int size = size();
        for (Ingredient ing : inputs) {
            for (int slot = 2; slot < size; slot++) {
                ItemStack stack = getStack(slot);
                if (!stack.isEmpty() && ing.test(stack)) {
                    stack.decrement(1);
                    if (stack.isEmpty()) setStack(slot, ItemStack.EMPTY);
                    break;
                }
            }
        }
        ItemStack currentResult = getStack(1);
        ItemStack recipeResult = recipe.result().copy();
        resetCrafting();
        if (!currentResult.isEmpty() && ItemStack.areItemsEqual(currentResult, recipeResult)) {
            currentResult.increment(recipeResult.getCount());
        } else {
            setStack(1, recipeResult);
        }
    }

    private void resetCrafting() {
        setProgress(0, false, 0);
    }

    public void setProgress(long progress, boolean crafting, long needLP) {
        this.progress = progress;
        this.crafting = crafting;
        this.needLP = needLP;
        HmUtils.updateBlockEntity(this);
    }

    @Override
    public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
        return world.getTime() % 10 == 0 && !crafting && slot == 1;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        if(slot == 1 || slot == 0) return false;
        Optional<AlchemyRecipe> recipeOpt = getRecipe();
        if (recipeOpt.isPresent()) {
            ItemStack result = recipeOpt.get().result();
            return getStack(1).getCount() * result.getCount() < 64;
        }
        return super.canInsert(slot, stack, dir);
    }

    public Optional<AlchemyRecipe> getRecipe() {
        return world.getRecipeManager().getFirstMatch(HmRecipes.ALCHEMY, getInventory(), world);
    }

    public boolean isCrafting() {
        return crafting;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putLong("progress", progress);
        nbt.putBoolean("crafting", crafting);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getLong("progress");
        crafting = nbt.getBoolean("crafting");
    }

    @Override
    public ItemStack showedItem() {
        ItemStack first = getStack(0);
        return first.getItem() instanceof OrbItem ? first : LpElement.super.showedItem();
    }

    @Override
    public Object getRealTarget() {
        return getStack(0);
    }

    @Override
    public byte getTier() {
        ItemStack orb = getStack(0);
        return orb.getItem() instanceof OrbItem orbItem ? orbItem.getTier() : 0;
    }

    @Override
    public long getMaxLp() {
        ItemStack orb = getStack(0);
        return orb.getItem() instanceof OrbItem orbItem ? orbItem.getMaxLp() : 0;
    }

    @Override
    public boolean canDaggerFulled() {
        return true;
    }
}