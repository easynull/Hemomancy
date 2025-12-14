package com.easynull.hemomancy.registers.items;

import com.easynull.hemomancy.core.LpElement;
import com.easynull.hemomancy.core.Tierable;
import com.easynull.hemomancy.registers.HcElements;
import com.easynull.hemomancy.utils.EnergyUtils;
import com.mw.nullcore.client.particle.screen.ScreenParticleHolder;
import com.mw.nullcore.core.builders.GuiRenderBuilder;
import com.mw.nullcore.core.items.Renderable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
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
import net.neoforged.fml.ModList;

import java.util.List;

public final class OrbItem extends Item implements LpElement, Tierable, Renderable {
    final long maxLp;
    final byte tier;
    final int bonus;

    public OrbItem(Properties prop, int tier, long maxLp, int bonus) {
        super(prop.stacksTo(1));
        this.tier = (byte) tier;
        this.maxLp = maxLp;
        this.bonus = bonus;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        RandomSource rand = RandomSource.create();
        long lp = rand.nextInt(100, 150);
        if (player.isShiftKeyDown() && player.isCreative()) lp = getMaxLp();
        if (reducerLp(lp * bonus, player.getItemInHand(hand))) {
            EnergyUtils.damageLp(player, lp);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public ItemStack getCraftingRemainder(ItemStack stack) {
        return stack.copy();
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
    public void renderEarly(ScreenParticleHolder target, GuiGraphics gg, ClientLevel level, float pTick, ItemStack stack, int x, int y) {
        if (HcElements.infinityOrb != null && stack.getItem() == HcElements.infinityOrb.get() && !ModList.get().isLoaded("iris")) {
            gg.pose().pushPose();
            GuiRenderBuilder.builder().pose(gg.pose()).renderType(ResourceLocation.fromNamespaceAndPath("avaritia", "textures/item/halo.png")).move(50, x + 8f, y + 8f).color(0f, 0f, 0f).buildOverlay(18f);
            gg.pose().popPose();
        }
    }

    @Override
    public boolean isParticleRenderable() {
        return false;
    }
}
