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
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Deprecated
public final class HmCommonUtils {
    public static boolean insertIntoPlayer(Inventory inventory, PlayerEntity player, int slot, int maxTransfer) {
        ItemStack slotStack = inventory.getStack(slot);
        ItemStack heldStack = player.getMainHandStack();
        int actualMax = Math.min(maxTransfer, 64);

        if (heldStack.isEmpty()) {
            if (slotStack.isEmpty()) {
                return false;
            }

            int transferAmount = Math.min(slotStack.getCount(), actualMax);
            player.setStackInHand(Hand.MAIN_HAND, slotStack.copyWithCount(transferAmount));
            slotStack.decrement(transferAmount);
            inventory.setStack(slot, slotStack.isEmpty() ? ItemStack.EMPTY : slotStack);
            inventory.markDirty();
            return true;
        }

        if (slotStack.isEmpty()) {
            int transferAmount = Math.min(heldStack.getCount(), actualMax);
            ItemStack insert = heldStack.copyWithCount(transferAmount);
            inventory.setStack(slot, insert);
            heldStack.decrement(transferAmount);
            inventory.markDirty();
            return true;
        }

        if (ItemStack.areItemsEqual(slotStack, heldStack) && ItemStack.areEqual(slotStack, heldStack)) {
            int spaceAvailable = Math.min(64, slotStack.getMaxCount()) - slotStack.getCount();
            int transferAmount = Math.min(Math.min(heldStack.getCount(), spaceAvailable), actualMax);

            if (transferAmount <= 0) {
                return false;
            }

            slotStack.increment(transferAmount);
            heldStack.decrement(transferAmount);
            inventory.setStack(slot, slotStack);
            inventory.markDirty();
            return true;
        }
        inventory.setStack(slot, heldStack.copy());
        player.setStackInHand(Hand.MAIN_HAND, slotStack.copy());
        inventory.markDirty();
        return true;
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
        World world = be.getWorld();
        if (world != null && !world.isClient) {
            be.markDirty();
            world.updateListeners(be.getPos(), be.getCachedState(), be.getCachedState(), 3);
        }
    }

    public static List<LivingEntity> getNearbyLivingEntities(World world, BlockPos center, double radius) {
        return world.getEntitiesByClass(LivingEntity.class, Box.of(center.toCenterPos(), radius * 2, radius * 2, radius * 2), e -> true);
    }

    public static <T extends Entity> void attractEntities(World world, BlockPos center, float radius, float balanced, Class<T> entityClass, Predicate<T> canAttract) {
        Vec3d playerPos = center.toCenterPos();
        List<T> entities = world.getEntitiesByClass(entityClass, Box.of(center.toCenterPos(), radius * 2, radius * 2, radius * 2), e -> true);

        for (T e : entities) {
            if (!canAttract.test(e)) continue;
            Vec3d entityPos = e.getPos();
            double dist = playerPos.distanceTo(entityPos);
            if (dist <= 1.0f) continue;

            float strange = (float) (dist / (dist * balanced));
            double force = Math.min(strange / (dist * dist), 0.3f);
            Vec3d dir = playerPos.subtract(entityPos).normalize();
            e.setVelocity(e.getVelocity().add(dir.multiply(force)));
        }
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

    @SafeVarargs
    public static <T> List<T> getElementsClasses(Registry<T> registry, Class<? extends T>... classes) {
        return registry.stream()
                .filter(entry -> Arrays.stream(classes).anyMatch(cls -> cls.isInstance(entry)))
                .toList();
    }

    public static boolean breakTree(ServerWorld world, BlockPos startPos, PlayerEntity player) {
        Set<BlockPos> treeLogs = floodFillLogs(world, startPos);

        if (!isValidTree(world, treeLogs)) {
            return false;
        }

        for (BlockPos pos : treeLogs) {
            world.breakBlock(pos, !player.isCreative(), player);
        }

        return true;
    }

    private static Set<BlockPos> floodFillLogs(ServerWorld world, BlockPos startPos) {
        Set<BlockPos> logs = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(startPos);
        logs.add(startPos);

        int maxSize = 200;
        while (!queue.isEmpty() && logs.size() < maxSize) {
            BlockPos current = queue.poll();

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;

                        BlockPos neighbor = current.add(dx, dy, dz);
                        BlockState neighborState = world.getBlockState(neighbor);

                        if (neighborState.isIn(BlockTags.LOGS) && logs.add(neighbor)) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
        return logs;
    }

    private static boolean isValidTree(ServerWorld world, Set<BlockPos> logs) {
        if (logs.size() < 4 || logs.size() > 150) {
            return false;
        }

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (BlockPos pos : logs) {
            minY = Math.min(minY, pos.getY());
            maxY = Math.max(maxY, pos.getY());
        }

        int height = maxY - minY + 1;
        if (height < 3) {
            return false;
        }

        int topThreshold = maxY - (int)(height * 0.4);
        int leavesFound = 0;
        int topLogsChecked = 0;

        for (BlockPos pos : logs) {
            if (pos.getY() >= topThreshold) {
                topLogsChecked++;
                if (hasNearbyLeaves(world, pos)) {
                    leavesFound++;
                }
            }
        }
        return topLogsChecked > 0 && (leavesFound * 100 / topLogsChecked) >= 30;
    }

    private static boolean hasNearbyLeaves(ServerWorld world, BlockPos logPos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 2; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;

                    BlockPos neighbor = logPos.add(dx, dy, dz);
                    BlockState state = world.getBlockState(neighbor);
                    if (state.isIn(BlockTags.LEAVES)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static BlockPos rotatePos(BlockPos pos, BlockRotation rotation) {
        int x = pos.getX();
        int z = pos.getZ();
        int newX = x;
        int newZ = z;

        switch (rotation) {
            case CLOCKWISE_90:
                newX = z;
                newZ = -x;
                break;
            case CLOCKWISE_180:
                newX = -x;
                newZ = -z;
                break;
            case COUNTERCLOCKWISE_90:
                newX = -z;
                newZ = x;
                break;
            default:
                break;
        }

        return new BlockPos(newX, pos.getY(), newZ);
    }
}
