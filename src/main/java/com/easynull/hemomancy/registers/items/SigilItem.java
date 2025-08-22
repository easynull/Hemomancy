package com.easynull.hemomancy.registers.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public final class SigilItem extends Item {
    final Context context;
    final int lp;

    public SigilItem(Properties props, Context context, int lp) {
        super(props.stacksTo(1));
        this.context = context;
        this.lp = lp;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        HitResult hit = player.pick(5.0f, 0.0f, false);
        BlockPos pos = ((BlockHitResult) hit).getBlockPos();
        context.use(new Consumer(level, pos, player, hand));
        return InteractionResult.CONSUME;
    }

    public record Consumer(Level level, BlockPos pos, Player player, InteractionHand hand) {}

    @FunctionalInterface
    public interface Context {
        void use(Consumer context);
    }
}
