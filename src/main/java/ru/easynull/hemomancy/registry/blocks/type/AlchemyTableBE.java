package ru.easynull.hemomancy.registry.blocks.type;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.InventoryBE;
import ru.easynull.hemomancy.api.Tickable;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.net.UpdateAlchemyS2CPacket;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.HmRecipes;
import ru.easynull.hemomancy.registry.items.OrbItem;
import ru.easynull.hemomancy.registry.recipes.AlchemyRecipe;
import ru.easynull.hemomancy.utils.HmUtils;

import java.util.List;
import java.util.Optional;

public final class AlchemyTableBE extends InventoryBE implements Tickable, LpElement, Tierable {
    public long progress, needLP;
    public boolean crafting;

    public AlchemyTableBE(BlockPos pos, BlockState state) {
        super(HmBlockEntities.ALCHEMY_TABLE, pos, state, 16, 64);
    }

    @Override
    public void onTick() {
        if (world == null || world.isClient) return;
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
        for (int slot = 2; slot < size(); slot++){
            removeStack(slot);
        }
        setStack(1, recipe.result());
    }

    private void resetCrafting() {
        setProgress(0, false, 0);
    }

    public void setProgress(long progress, boolean crafting, long needLP) {
        this.progress = progress;
        this.crafting = crafting;
        this.needLP = needLP;
        HmUtils.updateBlockEntity(this);
        ((ServerWorld)world).getChunkManager().threadedAnvilChunkStorage
                .getPlayersWatchingChunk(new ChunkPos(getPos()), false)
                .forEach(player -> ServerPlayNetworking.send(player, new UpdateAlchemyS2CPacket(getPos(), progress, needLP, crafting)));
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
        nbt.putLong("Progress", progress);
        nbt.putLong("NeedLP", needLP);
        nbt.putBoolean("Crafting", crafting);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getLong("Progress");
        needLP = nbt.getLong("NeedLP");
        crafting = nbt.getBoolean("Crafting");
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