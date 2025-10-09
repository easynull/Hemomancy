package com.easynull.hemomancy.registers.blocks.type;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.core.Tierable;
import com.easynull.hemomancy.core.network.AlchemyProgressPacket;
import com.easynull.hemomancy.registers.HcBlockEntities;
import com.easynull.hemomancy.registers.items.OrbItem;
import com.easynull.hemomancy.registers.recipes.AlchemyRecipe;
import com.easynull.hemomancy.registers.recipes.AltarRecipe;
import com.easynull.hemomancy.utils.RecipeUtils;
import com.mw.nullcore.Utils;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import com.mw.nullcore.core.blocks.type.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

public final class AlchemyTableBE extends ContainerBlockEntity implements Tickable, LpElement, Tierable {
    public long progress, needLP;
    public boolean crafting;

    public AlchemyTableBE(BlockPos pos, BlockState state) {
        super(HcBlockEntities.alchemyTable.get(), pos, state, 16, 64);
    }

    @Override
    public void tick() {
        Optional<AlchemyRecipe> recipeOpt = getRecipe();
        if (recipeOpt.isEmpty()) {
            if (crafting) resetCrafting();
            return;
        }
        AlchemyRecipe recipe = recipeOpt.get();
        if (!crafting) setProgress(0, true, recipe.lp());
        ItemStack orb = getItem(0);
        if (!(orb.getItem() instanceof OrbItem orbItem)) {
            resetCrafting();
            return;
        }
        long space = (long) (recipe.lp() * 0.007f);
        if (orbItem.getLp(orb) <= space) return;
        orbItem.reducerLp(-space, orb);
        setProgress(progress += space, true, recipe.lp());
        if (level instanceof ServerLevel sl) sl.sendParticles(DustParticleOptions.REDSTONE, worldPosition.getX() + 0.5, worldPosition.getY() + 1.2, worldPosition.getZ() + 0.5, 1, 0.2, 0.0, 0.2, 0.0);
        if (progress >= recipe.lp()) completeRecipe(recipe);
    }

    private void completeRecipe(AlchemyRecipe recipe) {
        List<Ingredient> inputs = recipe.inputs();
        int size = getContainerSize();
        for (Ingredient ing : inputs) {
            for (int slot = 2; slot < size; slot++) {
                ItemStack stack = getItem(slot);
                if (!stack.isEmpty() && ing.test(stack)) {
                    stack.shrink(1);
                    if (stack.isEmpty()) setItem(slot, ItemStack.EMPTY);
                    break;
                }
            }
        }
        ItemStack currentResult = getItem(1);
        ItemStack recipeResult = recipe.result().copy();
        resetCrafting();
        if (!currentResult.isEmpty() && ItemStack.isSameItemSameComponents(currentResult, recipeResult)) {
            currentResult.grow(recipeResult.getCount());
        } else {
            setItem(1, recipeResult);
        }
    }

    private void resetCrafting() {
        setProgress(0, false, 0);
    }

    public void setProgress(long progress, boolean crafting, long needLP) {
        if (level.isClientSide() || !(level instanceof ServerLevel sLevel)) return;
        this.progress = progress;
        this.crafting = crafting;
        this.needLP = needLP;
        PacketDistributor.sendToPlayersTrackingChunk(sLevel, new ChunkPos(worldPosition), new AlchemyProgressPacket(worldPosition, this.progress, this.crafting, this.needLP));
        Utils.Block.updateBlockEntity(this);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 1 || slot == 0) return false;
        Optional<AlchemyRecipe> recipeOpt = getRecipe();
        if (recipeOpt.isPresent()) {
            ItemStack result = recipeOpt.get().result();
            return getItem(1).getCount() * result.getCount() < 64;
        }
        return super.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return level.getGameTime() % 10 == 0 && !crafting && slot == 1;
    }

    public Optional<AlchemyRecipe> getRecipe() {
        return Optional.ofNullable(RecipeUtils.getAlchemyRecipe(new AlchemyRecipe.Input(this), level));
    }

    public boolean isCrafting() {
        return crafting;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        progress = tag.getLong("progress");
        crafting = tag.getBoolean("crafting");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("progress", progress);
        tag.putBoolean("crafting", crafting);
    }

    @Override
    public ItemStack showedItem() {
        ItemStack first = getItem(0);
        return first.getItem() instanceof OrbItem ? first : LpElement.super.showedItem();
    }

    @Override
    public Object getRealTarget() {
        return getItem(0);
    }

    @Override
    public byte getTier() {
        ItemStack orb = getItem(0);
        return orb.getItem() instanceof OrbItem orbItem ? orbItem.getTier() : 0;
    }

    @Override
    public long getMaxLp() {
        ItemStack orb = getItem(0);
        return orb.getItem() instanceof OrbItem orbItem ? orbItem.getMaxLp() : 0;
    }

    @Override
    public boolean canDaggerFulled() {
        return true;
    }
}