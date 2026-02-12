package ru.easynull.hemomancy.registry.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.StairShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.utils.EnergyUtils;
import ru.easynull.hemomancy.utils.HmUtils;

import java.util.concurrent.atomic.AtomicLong;

import static ru.easynull.hemomancy.utils.HmUtils.rotatePos;

public final class DaggerItem extends Item {

    public DaggerItem(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        var rand = player.getRandom();
        AtomicLong lp = new AtomicLong(rand.nextBetweenExclusive(85, 100));
        AtomicLong damageLp = new AtomicLong(lp.get());

        HmUtils.forEachInCube(player.getBlockPos(), 1, pos -> {
            if (world.getBlockEntity(pos) instanceof LpElement element && element.canDaggerFulled()) {
                if (player.isCreative() && player.isSneaking()) {
                    lp.set(element.getMaxLp());
                }
                element.reduceLp(lp.get(), world.getBlockEntity(pos));
                damageLp.set(lp.get() + lp.get());
            }
        });

        EnergyUtils.damageLp(player, damageLp.get());
        return TypedActionResult.consume(stack);
    }
}