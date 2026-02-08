package ru.easynull.hemomancy.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@Deprecated()
public final class HmUtils {
    public static boolean insertIntoPlayer(Inventory container, PlayerEntity player, int slotIndex, int amount) {
        if (slotIndex < 0 || slotIndex >= container.size()) return false;

        ItemStack source = container.getStack(slotIndex);
        if (source.isEmpty()) return false;

        amount = Math.min(amount, source.getCount());

        ItemStack toInsert = source.copyWithCount(amount);

        boolean success = player.getInventory().insertStack(toInsert);

        if (success) {
            source.decrement(amount);
            container.setStack(slotIndex, source);
            container.markDirty();
            return true;
        }

        if (toInsert.getCount() < amount) {
            int inserted = amount - toInsert.getCount();
            source.decrement(inserted);
            container.setStack(slotIndex, source);
            container.markDirty();
            return inserted > 0;
        }

        return false;
    }

    public static void forEachInCube(BlockPos center, int radius, Consumer<BlockPos> action) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    action.accept(center.add(x, y, z));
                }
            }
        }
    }

    public static void updateBlockEntity(BlockEntity be) {
        be.markDirty();
        if (be.hasWorld()) {
            var state = be.getCachedState();
            be.getWorld().updateListeners(be.getPos(), state, state, 3);
        }
    }

    public static List<LivingEntity> getNearbyLivingEntities(World world, BlockPos center, double radius) {
        return world.getEntitiesByClass(LivingEntity.class, Box.of(center.toCenterPos(), radius * 2, radius * 2, radius * 2), e -> true);
    }

    public static boolean isFluid(BlockState state) {
        return !state.getFluidState().isEmpty();
    }

    public static NbtList fromList(List<Text> texts) {
        NbtList nbtList = new NbtList();
        for (Text text : texts) {
            nbtList.add(NbtString.of(Text.Serializer.toJson(text)));
        }
        return nbtList;
    }
}
