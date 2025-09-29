package com.easynull.hemomancy.registers.items.sigil;

import com.easynull.hemomancy.utils.EnergyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class SigilItem extends Item {
    final Context ctx;
    final int lp;
    final boolean validAir;
    boolean shouldLP = true;

    public SigilItem(Properties props, Context ctx, int lp, boolean air) {
        super(props.stacksTo(1));
        this.ctx = ctx;
        this.lp = lp;
        this.validAir = air;
    }

    public SigilItem(Properties props, Context ctx, int lp) {
        this(props, ctx, lp, false);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        BlockHitResult hit = (BlockHitResult) player.pick(5.0f, 0.0f, false);
        BlockPos pos = hit.getBlockPos();
        if (!validAir && level.getBlockState(pos).isAir()) return InteractionResult.FAIL;
        ctx.action(new Consumer(level, pos, hit.getDirection(), player, player.getItemInHand(hand), this));
        if (shouldLP) EnergyUtils.extractLp(player, lp);
        else shouldLP = true;
        return InteractionResult.CONSUME;
    }

    public void shouldLP(boolean should) {
        this.shouldLP = should;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.hemomancy." + this.getDescriptionId().split("\\.")[2] + ".desc").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true)));
    }

    public record Consumer(Level level, BlockPos pos, Direction direction, Player player, ItemStack stack, SigilItem item) {}

    @FunctionalInterface
    public interface Context {
        void action(Consumer context);
    }
}
