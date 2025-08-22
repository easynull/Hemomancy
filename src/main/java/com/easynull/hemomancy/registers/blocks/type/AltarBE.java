package com.easynull.hemomancy.registers.blocks.type;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.core.altar.Altar;
import com.easynull.hemomancy.registers.HcBlockEntities;
import com.easynull.hemomancy.registers.HcRecipes;
import com.easynull.hemomancy.registers.recipes.AltarRecipe;
import com.mw.nullcore.core.blocks.type.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class AltarBE extends ContainerBlockEntity implements Tickable, LpElement {
    final Altar altar;
    long lp;

    public AltarBE(BlockPos pos, BlockState state) {
        super(HcBlockEntities.bloodAltar.get(), pos, state, 1, 64);
        this.altar = new Altar(this);
    }

    @Override
    public void tick() {
        altar.tick();
        if(level instanceof ServerLevel slevel) {
            AltarRecipe.AltarInput input = new AltarRecipe.AltarInput(this, altar.getTier());
            ItemStack result = getRecipe(slevel, input).map(RecipeHolder::value).map(r -> r.assemble(input, level.registryAccess())).orElse(ItemStack.EMPTY);
        }
    }

    private Optional<RecipeHolder<AltarRecipe>> getRecipe(ServerLevel level, AltarRecipe.AltarInput input) {
        return level.recipeAccess().getRecipeFor(HcRecipes.altar.get(), input, level);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        lp = tag.getLong("lp");
        altar.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("lp", lp);
        altar.save(tag);
    }

    @Override
    public long getMaxLp() {
        return altar.getCapacity();
    }
}
