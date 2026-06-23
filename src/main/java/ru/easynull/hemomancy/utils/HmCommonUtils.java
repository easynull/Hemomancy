package ru.easynull.hemomancy.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Deprecated
public final class HmCommonUtils {
    public static boolean insertIntoPlayer(Container inventory, Player player, int slot, int maxTransfer) {
        ItemStack slotStack = inventory.getItem(slot);
        ItemStack heldStack = player.getMainHandItem();
        int actualMax = Math.min(maxTransfer, 64);

        if (heldStack.isEmpty()) {
            if (slotStack.isEmpty()) {
                return false;
            }

            int transferAmount = Math.min(slotStack.getCount(), actualMax);
            player.setItemInHand(InteractionHand.MAIN_HAND, slotStack.copyWithCount(transferAmount));
            slotStack.shrink(transferAmount);
            inventory.setItem(slot, slotStack.isEmpty() ? ItemStack.EMPTY : slotStack);
            inventory.setChanged();
            return true;
        }

        if (slotStack.isEmpty()) {
            int transferAmount = Math.min(heldStack.getCount(), actualMax);
            ItemStack insert = heldStack.copyWithCount(transferAmount);
            inventory.setItem(slot, insert);
            heldStack.shrink(transferAmount);
            inventory.setChanged();
            return true;
        }

        if (ItemStack.isSameItem(slotStack, heldStack) && ItemStack.matches(slotStack, heldStack)) {
            int spaceAvailable = Math.min(64, slotStack.getMaxStackSize()) - slotStack.getCount();
            int transferAmount = Math.min(Math.min(heldStack.getCount(), spaceAvailable), actualMax);

            if (transferAmount <= 0) {
                return false;
            }

            slotStack.grow(transferAmount);
            heldStack.shrink(transferAmount);
            inventory.setItem(slot, slotStack);
            inventory.setChanged();
            return true;
        }
        inventory.setItem(slot, heldStack.copy());
        player.setItemInHand(InteractionHand.MAIN_HAND, slotStack.copy());
        inventory.setChanged();
        return true;
    }

    public static void forEachInCube(BlockPos center, int radius, Consumer<BlockPos> action) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    action.accept(center.offset(x, y, z));
                }
            }
        }
    }

    public static List<LivingEntity> getNearbyLivingEntities(Level level, BlockPos center, double radius) {
        return level.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(center.getCenter(), radius * 2, radius * 2, radius * 2), e -> true);
    }

    public static <T extends Entity> void attractEntities(Level level, BlockPos center, float radius, float balanced, Class<T> entityClass, Predicate<T> canAttract) {
        Vec3 playerPos = center.getCenter();
        List<T> entities = level.getEntitiesOfClass(entityClass, AABB.ofSize(center.getCenter(), radius * 2, radius * 2, radius * 2), e -> true);

        for (T e : entities) {
            if (!canAttract.test(e)) continue;
            Vec3 entityPos = e.position();
            double dist = playerPos.distanceTo(entityPos);
            if (dist <= 1.0f) continue;

            float strange = (float) (dist / (dist * balanced));
            double force = Math.min(strange / (dist * dist), 0.3f);
            Vec3 dir = playerPos.subtract(entityPos).normalize();
            e.setDeltaMovement(e.getDeltaMovement().add(dir.scale(force)));
        }
    }

    public static boolean isFluid(BlockState state) {
        return !state.getFluidState().isEmpty();
    }

    public static ListTag fromList(List<Component> texts, HolderLookup.Provider provider) {
        ListTag nbtList = new ListTag();
        for (Component text : texts) {
            nbtList.add(StringTag.valueOf(Component.Serializer.toJson(text, provider)));
        }
        return nbtList;
    }

    @SafeVarargs
    public static <T> List<T> getElementsClasses(Registry<T> registry, Class<? extends T>... classes) {
        return registry.stream()
                .filter(entry -> Arrays.stream(classes).anyMatch(cls -> cls.isInstance(entry)))
                .toList();
    }

    public static boolean breakTree(ServerLevel level, BlockPos startPos, Player player) {
        Set<BlockPos> treeLogs = floodFillLogs(level, startPos);

        if (!isValidTree(level, treeLogs)) {
            return false;
        }

        for (BlockPos pos : treeLogs) {
            level.destroyBlock(pos, !player.isCreative(), player);
        }

        return true;
    }

    private static Set<BlockPos> floodFillLogs(ServerLevel level, BlockPos startPos) {
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

                        BlockPos neighbor = current.offset(dx, dy, dz);
                        BlockState neighborState = level.getBlockState(neighbor);

                        if (neighborState.is(BlockTags.LOGS) && logs.add(neighbor)) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
        return logs;
    }

    private static boolean isValidTree(ServerLevel level, Set<BlockPos> logs) {
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
                if (hasNearbyLeaves(level, pos)) {
                    leavesFound++;
                }
            }
        }
        return topLogsChecked > 0 && (leavesFound * 100 / topLogsChecked) >= 30;
    }

    private static boolean hasNearbyLeaves(ServerLevel level, BlockPos logPos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 2; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;

                    BlockPos neighbor = logPos.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(neighbor);
                    if (state.is(BlockTags.LEAVES)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static BlockPos rotatePos(BlockPos pos, Rotation rotation) {
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
