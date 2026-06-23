package ru.easynull.hemomancy.registry.blocks.type;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import ru.easynull.hemomancy.api.ContainerBlockEntity;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.HmRecipes;
import ru.easynull.hemomancy.registry.items.OrbItem;
import ru.easynull.hemomancy.registry.recipes.AlchemyRecipe;

import java.util.Optional;

public final class AlchemyTableBlockEntity extends ContainerBlockEntity implements BlockEntityTicker<AlchemyTableBlockEntity>, LpElement, Tierable {
    public long progress, needLP;
    public boolean crafting;

    public AlchemyTableBlockEntity(BlockPos pos, BlockState state) {
        super(HmBlockEntities.ALCHEMY_TABLE, pos, state, 16, 64);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, AlchemyTableBlockEntity blockEntity) {
        if (level == null || level.isClientSide) return;
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
        orbItem.reduceLp(-space, orb);
        setProgress(progress += space, true, recipe.lp());
        if (level instanceof ServerLevel sl) sl.sendParticles(DustParticleOptions.REDSTONE, getX() + 0.5, getY() + 1.2, getZ() + 0.5, 1, 0.2, 0.0, 0.2, 0.0);
        if (progress >= recipe.lp()) completeRecipe(recipe);
    }

    private void completeRecipe(AlchemyRecipe recipe) {
        for (int slot = 2; slot < getContainerSize(); slot++){
            removeItemNoUpdate(slot);
        }
        setItem(1, recipe.result());
    }

    private void resetCrafting() {
        setProgress(0, false, 0);
    }

    public void setProgress(long progress, boolean crafting, long needLP) {
        this.progress = progress;
        this.crafting = crafting;
        this.needLP = needLP;
        sync(this);
//        HmCommonUtils.syncBlockEntity(this);
//        ((ServerLevel)level).getChunkSource().chunkMap
//                .getPlayers(new ChunkPos(getBlockPos()), false)
//                .forEach(player -> ServerPlayNetworking.send(player, new UpdateAlchemyS2CPacket(getBlockPos(), progress, needLP, crafting)));
    }

    @Override
    public boolean canTakeItem(Container hopperInventory, int slot, ItemStack stack) {
        return level.getGameTime() % 10 == 0 && !crafting && slot == 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        if(slot == 1 || slot == 0) return false;
        Optional<AlchemyRecipe> recipeOpt = getRecipe();
        if (recipeOpt.isPresent()) {
            ItemStack result = recipeOpt.get().result();
            return getItem(1).getCount() * result.getCount() < 64;
        }
        return super.canPlaceItem(slot, itemStack);
    }


    public Optional<AlchemyRecipe> getRecipe() {
        return level.getRecipeManager().getRecipeFor(HmRecipes.ALCHEMY, new AlchemyRecipe.Input(getContainer().getItems()), level).map(RecipeHolder::value);
    }

    public boolean isCrafting() {
        return crafting;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putLong("Progress", progress);
        nbt.putLong("NeedLP", needLP);
        nbt.putBoolean("Crafting", crafting);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        progress = nbt.getLong("Progress");
        needLP = nbt.getLong("NeedLP");
        crafting = nbt.getBoolean("Crafting");
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