package com.easynull.hemomancy.registers.items.sigil;

import com.easynull.hemomancy.utils.EnergyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.Set;

public class SigilItem extends Item {
    final Context context;
    final int lp;
    final boolean validAir;
    boolean shouldLP = true;
    final Set<Component> additional;

    public SigilItem(Properties props, Context context, int lp, boolean air, Component... additionalTooltip) {
        super(props.stacksTo(1));
        this.context = context;
        this.lp = lp;
        this.validAir = air;
        this.additional = Set.of(additionalTooltip);
    }

    public SigilItem(Properties props, Context context, int lp, Component... additional) {
        this(props, context, lp, false, additional);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        HitResult hit = player.pick(5.0f, 0.0f, false);
        BlockPos pos = ((BlockHitResult) hit).getBlockPos();
        if (!validAir && level.getBlockState(pos).isAir()) return InteractionResult.FAIL;
        context.use(new Consumer(level, pos, player, player.getItemInHand(hand), this));
        if (shouldLP) EnergyUtils.extractLp(player, lp);
        else shouldLP = true;
        return InteractionResult.CONSUME;
    }

    public void shouldLP(boolean should) {
        this.shouldLP = should;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.hemomancy." + this.getDescriptionId().split("\\.")[2] + ".desc").withStyle(ChatFormatting.GRAY));
        if (!additional.isEmpty()) tooltip.addAll(additional);
    }

    public record Consumer(Level level, BlockPos pos, Player player, ItemStack stack, SigilItem item) {}

    @FunctionalInterface
    public interface Context {
        void use(Consumer context);
    }
}
