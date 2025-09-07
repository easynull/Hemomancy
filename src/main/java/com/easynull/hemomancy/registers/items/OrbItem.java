package com.easynull.hemomancy.registers.items;

import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.core.Tierable;
import com.easynull.hemomancy.registers.HcElements;
import com.easynull.hemomancy.utils.EnergyUtils;
import com.mw.nullcore.core.builders.GuiRenderBuilder;
import com.mw.nullcore.core.items.IconRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public final class OrbItem extends Item implements LpElement, Tierable, IconRenderer {
    final long maxLp;
    final byte tier;

    public OrbItem(Properties prop, int tier, long maxLp) {
        super(prop.stacksTo(1));
        this.tier = (byte) tier;
        this.maxLp = maxLp;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        RandomSource rand = RandomSource.create();
        int lp = rand.nextInt(150, 200);
        if (reducerLp(lp, player.getItemInHand(hand))) {
            EnergyUtils.damageLp(player, lp);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public byte getTier() {
        return tier;
    }

    @Override
    public long getMaxLp() {
        return maxLp;
    }

    @Override
    public long getLp(Object target) {
        if (HcElements.infinityOrb != null && ((ItemStack) target).getItem() == HcElements.infinityOrb.get()) {
            return getMaxLp();
        }
        return LpElement.super.getLp(target);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.hemomancy.orb.desc", (HcElements.infinityOrb != null && stack.getItem() == HcElements.infinityOrb.get()) ? "∞" : getMaxLp()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void renderIcon(GuiGraphics gg, Level level, ItemStack stack, int pX, int pY, float pTick) {
        if (HcElements.infinityOrb != null && stack.getItem() == HcElements.infinityOrb.get()) {
            gg.pose().pushPose();
            GuiRenderBuilder.builder().pose(gg.pose()).renderType(ResourceLocation.fromNamespaceAndPath("avaritia", "textures/item/halo.png")).moveBefore(pX + 8f, pY + 8f).color(0f, 0f, 0f).pulseScale(1f, 1.02f, 3f).buildOverlay(18f);
            gg.pose().popPose();
        }
    }
}
