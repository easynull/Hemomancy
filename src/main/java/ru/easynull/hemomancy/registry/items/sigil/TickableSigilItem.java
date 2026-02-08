package ru.easynull.hemomancy.registry.items.sigil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ru.easynull.hemomancy.utils.EnergyUtils;

public final class TickableSigilItem extends SigilItem {
    private final int rollbackTicks;

    public TickableSigilItem(Settings settings, Context action, int lpCost, int rollbackTicks) {
        super(settings, action, lpCost);
        this.rollbackTicks = rollbackTicks;
    }

    public TickableSigilItem(Settings settings, Context action, int lpCost) {
        this(settings, action, lpCost, 20);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof PlayerEntity player)) return;
        action.perform(new SigilContext(world, player.getBlockPos(), player.getHorizontalFacing(), player, stack, this));
        if (!world.isClient && isActive(stack) && consumeLp) {
            if (world.getTime() % rollbackTicks == 0) {
                EnergyUtils.extractLp(player, lpCost);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        boolean wasActive = isActive(stack);
        setActive(stack, !wasActive);
        return TypedActionResult.consume(stack);
    }

    @Override
    public boolean isActive(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.getBoolean("Enabled");
    }

    public void setActive(ItemStack stack, boolean active) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putBoolean("Enabled", active);
    }
}