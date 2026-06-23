package ru.easynull.hemomancy.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ContainerBlockEntity extends SyncBlockEntity implements Container {
    public SimpleContainer container;
    public final int maxInSlot;

    public ContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, SimpleContainer container, int maxInSlot) {
        super(type, pos, state);
        this.container = container;
        this.maxInSlot = maxInSlot;
    }

    public ContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int slots, int maxInSlot) {
        this(type, pos, state, new SimpleContainer(slots), maxInSlot);
    }

    public ContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int slots) {
        this(type, pos, state, slots, 1);
    }

    public ContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        this(type, pos, state, 1, 1);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.container.clearContent();
        ContainerHelper.loadAllItems(tag, this.container.getItems(), registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.container.getItems(), registries);
    }

    public ItemStack getFirst() {
        return this.container.getItem(0);
    }

    public SimpleContainer getContainer() {
        return this.container;
    }

    @Override
    public int getContainerSize() {
        return this.container.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.container.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.container.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = this.container.removeItem(slot, amount);
        sync(this);
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = this.container.removeItemNoUpdate(slot);
        sync(this);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.container.setItem(slot, stack);
        sync(this);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public void setFirst(ItemStack stack) {
        setItem(0, stack);
    }

    @Override
    public void clearContent() {
        this.container.clearContent();
        setChanged();
    }
}