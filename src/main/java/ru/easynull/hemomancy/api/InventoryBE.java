package ru.easynull.hemomancy.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import ru.easynull.hemomancy.utils.HmCommonUtils;

public abstract class InventoryBE extends BlockEntity implements SidedInventory {
    public final SimpleInventory inventory;
    public final int maxInSlot;

    public InventoryBE(BlockEntityType<?> type, BlockPos pos, BlockState state, SimpleInventory inventory, int maxInSlot) {
        super(type, pos, state);
        this.inventory = inventory;
        this.maxInSlot = maxInSlot;
    }

    public InventoryBE(BlockEntityType<?> type, BlockPos pos, BlockState state, int slots, int maxInSlot) {
        this(type, pos, state, new SimpleInventory(slots), maxInSlot);
    }

    public InventoryBE(BlockEntityType<?> type, BlockPos pos, BlockState state, int slots) {
        this(type, pos, state, slots, 1);
    }

    public InventoryBE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        this(type, pos, state, 1, 1);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory.stacks);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory.stacks);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public ItemStack getFirst() {
        return inventory.getStack(0);
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = inventory.removeStack(slot, amount);
        HmCommonUtils.updateBlockEntity(this);
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        var stack = inventory.removeStack(slot);
        HmCommonUtils.updateBlockEntity(this);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setStack(slot, stack);
        HmCommonUtils.updateBlockEntity(this);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    public void setFirst(ItemStack stack) {
        setStack(0, stack);
    }

    @Override
    public void clear() {
        inventory.clear();
        HmCommonUtils.updateBlockEntity(this);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}
