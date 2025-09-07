package com.easynull.hemomancy.registers.items;

import com.easynull.hemomancy.Hemomancy;
import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.core.Tierable;
import com.easynull.hemomancy.core.Wandable;
import com.easynull.hemomancy.registers.HcComponents;
import com.easynull.hemomancy.registers.HcConfig;
import com.mojang.math.Axis;
import com.mw.nullcore.Utils;
import com.mw.nullcore.client.render.Transform;
import com.mw.nullcore.core.items.OverlayRenderer;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public final class ControllerItem extends Item implements OverlayRenderer {
    long lp;
    long maxLp;
    ItemLike current;
    byte tier;

    public ControllerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player) {
            lp = 0;
            maxLp = 0;
            current = null;
            tier = 0;
            HitResult hit = player.pick(5.0f, 0.0f, false);
            BlockPos pos = ((BlockHitResult) hit).getBlockPos();
            if (level.getBlockEntity(pos) instanceof LpElement le) {
                lp = le.getLp(level.getBlockEntity(pos));
                maxLp = le.getMaxLp();
                current = level.getBlockEntity(pos).getBlockState().getBlock();
                if(le instanceof Tierable te){
                    tier = te.getTier();
                }
            } else {
                player.inventoryMenu.getItems().forEach(inv -> {
                    if (inv.getItem() instanceof LpElement le) {
                        lp = le.getLp(inv);
                        maxLp = le.getMaxLp();
                        current = inv.getItem();
                        if(inv.getItem() instanceof Tierable te){
                            tier = te.getTier();
                        }
                    }
                });
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof Wandable wand) {
            List<String> modes = List.of(wand.getModes());
            if (modes.isEmpty()) return InteractionResult.FAIL;

            String currentMode = wand.getMode();

            int currentIndex = modes.indexOf(currentMode);
            int nextIndex = (currentIndex + 1) % modes.size();
            String nextMode = modes.get(nextIndex);

            wand.setMode(nextMode);

            if (player != null) {
                player.displayClientMessage(Component.literal("Режим изменен на: " + nextMode), true);
            }

            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void renderOverlay(Player player, ItemStack stack, GuiGraphics gg, int x, int y, float pTick) {
        if (lp == 0) return;
        final ResourceLocation bar = Hemomancy.toTextures("gui/bloodbar");
        int xCord = 3 + x;
        int yCord = 3 + y;

        int progress = (int) Utils.Mth.calculateProgress(lp, maxLp, 0, 27);
        Utils.Render.drawTexture(gg, bar, xCord, yCord, 0, 0, 21, 48, 48, 48);
        Utils.Render.drawTexture(gg, bar, xCord + 6, yCord + 40 - (int) (progress * 1.5), 21, 33 - (int) (progress * 1.5), 12, (int) (progress * 1.5), 48, 48);
        if (current != null) gg.renderItem(new ItemStack(current), xCord + 26, yCord + 7);
        if (tier > 0) Utils.Render.drawText(gg, Component.translatable("tooltip.hemomancy.tier", tier), xCord + 23, yCord + 27, 0xFFFFFFFF, false);
        if (isGlobal()) Utils.Render.drawText(gg, Utils.Text.formatNum(lp), xCord + 4, yCord + 20, 0xFFFF0000, true);
    }

    @Override
    public boolean isGlobal() {
        return HcConfig.advancedController.get();
    }
}
