package ru.easynull.hemomancy.registry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.world.World;
import ru.easynull.hemomancy.api.energy.LpElement;
import ru.easynull.hemomancy.utils.EnergyUtils;
import ru.easynull.hemomancy.utils.HmCommonUtils;

import java.util.concurrent.atomic.AtomicLong;

public final class DaggerItem extends SwordItem {

    public DaggerItem(Settings settings) {
        super(ToolMaterials.STONE, 2, 2, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        var rand = player.getRandom();
        AtomicLong lp = new AtomicLong(rand.nextBetweenExclusive(18, 56));
        AtomicLong damageLp = new AtomicLong(lp.get());

        HmCommonUtils.forEachInCube(player.getBlockPos(), 1, pos -> {
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

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = target.getWorld();
        var rand = target.getRandom();
        AtomicLong lp = new AtomicLong(rand.nextBetweenExclusive(10, 42));
        HmCommonUtils.forEachInCube(target.getBlockPos(), 1, pos -> {
            if (world.getBlockEntity(pos) instanceof LpElement element && element.canDaggerFulled()) {
                element.reduceLp(lp.get(), world.getBlockEntity(pos));
            }
        });
        target.playSound(SoundEvents.ENTITY_ALLAY_ITEM_THROWN, 1.0f, 1.0f);
        return true;
    }
}