package com.easynull.hemomancy.registers.items;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.core.Tierable;
import com.easynull.hemomancy.core.Wandable;
import com.easynull.hemomancy.registers.HcConfig;
import com.easynull.hemomancy.utils.EnergyUtils;
import com.mw.nullcore.core.NcUtils;
import com.mw.nullcore.core.items.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public final class ControllerItem extends Item implements OverlayRenderer {
    private long lp;
    private long maxLp;
    private ItemLike current;
    private byte tier;

    public ControllerItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!(entity instanceof Player player) || !level.isClientSide()) return;
        resetState();
        HitResult hit = player.pick(5.0f, 0.0f, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) hit).getBlockPos();
            if (level.getBlockEntity(pos) instanceof LpElement le && le.getLp(level.getBlockEntity(pos)) > 0) {
                updateFromBlockEntity(le, level.getBlockEntity(pos));
                return;
            }
        }
        updateFromInventory(player);
    }

    private void resetState() {
        lp = 0;
        maxLp = 0;
        current = null;
        tier = 0;
    }

    private void updateFromBlockEntity(LpElement le, Object target) {
        lp = le.getLp(target);
        maxLp = le.getMaxLp();
        current = le.showedItem().isEmpty() ? ((BlockEntity) target).getBlockState().getBlock() : le.showedItem().getItem();
        if (le instanceof Tierable te) {
            tier = te.getTier();
        }
    }

    private void updateFromInventory(Player player) {
        ItemStack highestTier = EnergyUtils.getHighestTier(player);
        if (highestTier.getItem() instanceof LpElement le) {
            updateFromItem(le, highestTier);
            if (le instanceof Tierable te) {
                tier = te.getTier();
                return;
            }
        }
        for (ItemStack inv : player.getInventory().items) {
            if (inv.getItem() instanceof LpElement le) {
                updateFromItem(le, inv);
            }
        }
    }

    private void updateFromItem(LpElement le, ItemStack stack) {
        lp = le.getLp(stack);
        maxLp = le.getMaxLp();
        current = stack.getItem();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (level.isClientSide() || !(level.getBlockEntity(pos) instanceof Wandable wand)) {
            return InteractionResult.FAIL;
        }

        List<String> modes = List.of(wand.getModes());
        if (modes.isEmpty()) return InteractionResult.FAIL;

        String currentMode = wand.getMode();
        String nextMode = modes.get((modes.indexOf(currentMode) + 1) % modes.size());

        wand.setMode(nextMode);

        if (player != null) {
            player.displayClientMessage(Component.translatable("message.hemomancy.mode.change", Component.translatable("mode." + nextMode)), true);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void renderOverlay(Player player, ItemStack stack, GuiGraphics gg, int x, int y, float pTick) {
        if (lp == 0) return;
        final ResourceLocation bar = Hemomancy.textures("gui/bloodbar");
        int xCord = x + 3;
        int yCord = y + 3;

        int progress = (int) (lp * 27.0 / maxLp);
        int barHeight = (int) (progress * 1.5);

        NcUtils.Render.drawTexture(gg, bar, xCord, yCord, 0, 0, 21, 48, 48, 48);
        NcUtils.Render.drawTexture(gg, bar, xCord + 6, yCord + 40 - barHeight, 21, 33 - barHeight, 12, barHeight, 48, 48);

        if (current != null) gg.renderItem(new ItemStack(current), xCord + 26, yCord + 7);
        if (tier > 0) NcUtils.Render.drawText(gg, Component.translatable("tooltip.hemomancy.tier", tier), xCord + 23, yCord + 27, 0xFFFFFFFF, false);
        if (isGlobal()) gg.drawCenteredString(Minecraft.getInstance().font, NcUtils.Text.formatNum(lp), xCord + 14, yCord + 50, 0xFFFF0000);
    }

    @Override
    public boolean isGlobal() {
        return HcConfig.advancedController.get();
    }
}