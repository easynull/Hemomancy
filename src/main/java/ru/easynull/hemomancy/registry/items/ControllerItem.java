package ru.easynull.hemomancy.registry.items;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.api.energy.Wandable;
import ru.easynull.hemomancy.utils.EnergyUtils;

import java.util.List;

public final class ControllerItem extends Item {
    private long lp;
    private long maxLp;
    private Item currentItem;
    private byte tier;

    public ControllerItem(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof PlayerEntity player) || world.isClient) {
            return;
        }
        resetState();

        HitResult hit = player.raycast(5.0, 0.0f, false);

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) hit).getBlockPos();
            if (world.getBlockEntity(pos) instanceof LpElement element) {
                long elementLp = element.getLp(world.getBlockEntity(pos));
                if (elementLp > 0) {
                    updateFromBlockEntity(element, world.getBlockEntity(pos));
                    return;
                }
            }
        }

        updateFromInventory(player);
    }

    private void resetState() {
        lp = 0;
        maxLp = 0;
        currentItem = null;
        tier = 0;
    }

    private void updateFromBlockEntity(LpElement element, Object target) {
        lp = element.getLp(target);
        maxLp = element.getMaxLp();

        ItemStack showed = (ItemStack) element.getRealTarget();
        if (showed.isEmpty()) {
            currentItem = ((BlockEntity) target).getCachedState().getBlock().asItem();
        } else {
            currentItem = showed.getItem();
        }

        if (element instanceof Tierable tierable) {
            tier = tierable.getTier();
        }
    }

    private void updateFromInventory(PlayerEntity player) {
        ItemStack highestTierOrb = EnergyUtils.getHighestTier(player);
        if (!highestTierOrb.isEmpty() && highestTierOrb.getItem() instanceof LpElement element) {
            updateFromItem(element, highestTierOrb);
            if (element instanceof Tierable tierable) {
                tier = tierable.getTier();
                return;
            }
        }

        for (ItemStack invStack : player.getInventory().main) {
            if (invStack.getItem() instanceof LpElement element) {
                updateFromItem(element, invStack);
            }
        }
    }

    private void updateFromItem(LpElement element, ItemStack stack) {
        lp = element.getLp(stack);
        maxLp = element.getMaxLp();
        currentItem = stack.getItem();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        if (world.isClient || !(world.getBlockEntity(pos) instanceof Wandable wandable)) {
            return ActionResult.FAIL;
        }

        List<String> modes = List.of(wandable.getModes());
        if (modes.isEmpty()) {
            return ActionResult.FAIL;
        }

        String currentMode = wandable.getMode();
        int nextIndex = (modes.indexOf(currentMode) + 1) % modes.size();
        String nextMode = modes.get(nextIndex);

        wandable.setMode(nextMode);

        if (player != null) {
            player.sendMessage(Text.translatable("message.hemomancy.mode.change", Text.translatable("mode." + nextMode)), true);
        }

        return ActionResult.CONSUME;
    }

    public long getCurrentLp() {
        return lp;
    }

    public long getCurrentMaxLp() {
        return maxLp;
    }

    public byte getCurrentTier() {
        return tier;
    }

    public Item getCurrentDisplayedItem() {
        return currentItem;
    }
}