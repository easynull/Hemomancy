package ru.easynull.hemomancy.registry.items.sigil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ru.easynull.hemomancy.utils.EnergyUtils;

public final class EnabledSigilItem extends SigilItem {
    private final int rollbackTicks, rollbackPayTicks;

    public EnabledSigilItem(Settings settings, Context action, int lpCost, int rollbackTicks, int rollbackPayTicks) {
        super(settings, action, lpCost);
        this.rollbackTicks = rollbackTicks;
        this.rollbackPayTicks = rollbackPayTicks;
    }

    public EnabledSigilItem(Settings settings, Context action, int lpCost, int rollbackPayTicks) {
        this(settings, action, lpCost, 20, rollbackPayTicks);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof PlayerEntity player) || !isEnabled(stack)) return;
        if (world.getTime() % rollbackTicks == 0) action.perform(new SigilContext(world, player.getBlockPos(), player.getHorizontalFacing(), player, stack, this));
        if (!world.isClient && consumeLp) {
            if (world.getTime() % rollbackPayTicks == 0) {
                EnergyUtils.extractLp(player, lpCost);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        boolean wasActive = isEnabled(stack);
        setEnabled(stack, !wasActive);
        return TypedActionResult.consume(stack);
    }

    public static boolean isEnabled(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.getBoolean("Enabled");
    }

    public static void setEnabled(ItemStack stack, boolean value) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putBoolean("Enabled", value);
    }
}