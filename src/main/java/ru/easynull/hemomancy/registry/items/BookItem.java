package ru.easynull.hemomancy.registry.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.api.energy.Wandable;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.proxy.MainProxy;
import ru.easynull.hemomancy.registry.HmDataComponents;
import ru.easynull.hemomancy.utils.EnergyUtils;

import java.util.List;

public final class BookItem extends Item {
    private long lp;
    private long maxLp;
    private Item currentItem;
    private byte tier;

    public BookItem(Properties settings) {
        super(settings.component(HmDataComponents.EXTENDED, false).stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        level.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS);
        ItemStack stack = player.getItemInHand(hand);
        if (player.getAttached(MagePlayer.DATA) == null) MagePlayer.of(player).setLevel(0);
        if (!level.isClientSide()) return InteractionResultHolder.pass(stack);
        MainProxy.PROXY.openBook(stack);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof Player player) || level.isClientSide()) {
            return;
        }
        resetState();

        HitResult hit = player.pick(5.0, 0.0f, false);

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) hit).getBlockPos();
            if (level.getBlockEntity(pos) instanceof LpElement element) {
                long elementLp = element.getLp(level.getBlockEntity(pos));
                if (elementLp > 0) {
                    updateFromBlockEntity(element, level.getBlockEntity(pos));
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
        if (showed == null || showed.isEmpty()) {
            currentItem = ((BlockEntity) target).getBlockState().getBlock().asItem();
        } else {
            currentItem = showed.getItem();
        }

        if (element instanceof Tierable tierable) {
            tier = tierable.getTier();
        }
    }

    private void updateFromInventory(Player player) {
        ItemStack highestTierOrb = EnergyUtils.getHighestTier(player);
        if (!highestTierOrb.isEmpty() && highestTierOrb.getItem() instanceof LpElement element) {
            updateFromItem(element, highestTierOrb);
            if (element instanceof Tierable tierable) {
                tier = tierable.getTier();
                return;
            }
        }

        for (ItemStack invStack : player.getInventory().items) {
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
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (!(level.getBlockEntity(pos) instanceof Wandable wandable)) {
            return InteractionResult.PASS;
        }

        List<String> modes = List.of(wandable.getModes());
        if (modes.isEmpty()) {
            return InteractionResult.PASS;
        }

        String currentMode = wandable.getMode();
        int nextIndex = (modes.indexOf(currentMode) + 1) % modes.size();
        String nextMode = modes.get(nextIndex);

        wandable.setMode(nextMode);

        if (player != null) {
            player.displayClientMessage(Component.translatable("message.hemomancy.mode.change", Component.translatable("mode." + nextMode)), true);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (stack.get(HmDataComponents.EXTENDED)) tooltip.add(Component.translatable("tooltip.hemomancy.book").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
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