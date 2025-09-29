package com.easynull.hemomancy.registers.blocks.type;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.core.Tierable;
import com.easynull.hemomancy.core.network.AlchemyProgressPacket;
import com.easynull.hemomancy.registers.HcBlockEntities;
import com.easynull.hemomancy.registers.items.OrbItem;
import com.easynull.hemomancy.registers.recipes.AlchemyRecipe;
import com.easynull.hemomancy.utils.RecipeUtils;
import com.mw.nullcore.Utils;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import com.mw.nullcore.core.blocks.type.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AlchemyBE extends ContainerBlockEntity implements Tickable, LpElement, Tierable {
    public long progress, needLP;
    public boolean crafting;

    public AlchemyBE(BlockPos pos, BlockState state) {
        super(HcBlockEntities.alchemy.get(), pos, state, 12, 64);
    }

    @Override
    public void tick() {
        Optional<AlchemyRecipe> recipeOpt = getRecipe();

        if (recipeOpt.isPresent()) {
            AlchemyRecipe recipe = recipeOpt.get();

            if (!crafting) {
                setProgress(0, true, recipe.lp());
            }
            ItemStack orb = getFirst();
            if (orb.getItem() instanceof OrbItem orbItem) {
                if(orbItem.getLp(orb) <= 10) return;
                orbItem.reducerLp(-10, getInventory().getItem(0));
                setProgress(progress += 10, true, recipe.lp());
                if (progress >= recipe.lp()) {
                    completeRecipe(recipe);
                }
            } else {
                resetCrafting();
            }
        } else {
            if (crafting) {
                resetCrafting();
            }
        }
    }

    private void completeRecipe(AlchemyRecipe recipe) {
        List<Ingredient> inputs = recipe.inputs();
        List<ItemStack> availableItems = new ArrayList<>();

        for (int i = 2; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty()) {
                availableItems.add(stack);
            }
        }

        List<Ingredient> remainingIngredients = new ArrayList<>(inputs);

        for (Ingredient ingredient : inputs) {
            for (int i = 2; i < getContainerSize(); i++) {
                ItemStack stack = getItem(i);
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        setItem(i, ItemStack.EMPTY);
                    }
                    remainingIngredients.remove(ingredient);
                    break;
                }
            }
        }
        ItemStack currentResult = getItem(1);
        ItemStack recipeResult = recipe.result().copy();
        if (!currentResult.isEmpty() && ItemStack.isSameItemSameComponents(currentResult, recipeResult)) {
            currentResult.grow(recipeResult.getCount());
        } else {
            setItem(1, recipeResult);
        }
        resetCrafting();
    }

    private void resetCrafting() {
        setProgress(0, false, 0);
    }

    public void setProgress(long progress, boolean crafting, long needLP) {
        if (!level.isClientSide() && this.level instanceof ServerLevel sLevel) {
            this.progress = progress;
            this.crafting = crafting;
            this.needLP = needLP;
            PacketDistributor.sendToPlayersTrackingChunk(sLevel, new ChunkPos(getBlockPos()), new AlchemyProgressPacket(getBlockPos(), this.progress, this.crafting, this.needLP));
            Utils.Block.updateBlockEntity(this);
        }
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
        if (getFirst().getItem() instanceof OrbItem) return getFirst();
        return LpElement.super.showedItem();
    }

    @Override
    public Object getRealTarget() {
        return getFirst();
    }

    @Override
    public byte getTier() {
        ItemStack orb = getFirst();
        return orb.getItem() instanceof OrbItem orbItem ? orbItem.getTier() : 0;
    }

    @Override
    public long getMaxLp() {
        ItemStack orb = getFirst();
        return orb.getItem() instanceof OrbItem orbItem ? orbItem.getMaxLp() : 0;
    }

    @Override
    public boolean canDaggerFulled() {
        return true;
    }
}