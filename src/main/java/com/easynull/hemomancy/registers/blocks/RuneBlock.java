package com.easynull.hemomancy.registers.blocks;

import com.easynull.hemomancy.registers.HcElements;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RuneBlock extends Block {
    private static final Map<Type, Number> runes = new HashMap<>();
    final List<Type> type;
    final Number tier;

    public RuneBlock(Properties prop, Number tier, Type... type) {
        super(prop);
        this.tier = tier;
        this.type = List.of(type);
        if (this.type.getFirst() != Type.none && !runes.containsKey(this.type.getFirst())) runes.put(this.type.getFirst(), tier);
    }

    public RuneBlock(Properties prop, Type... type) {
        this(prop, 1f, type);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (stack.getItem() == HcElements.rune.asItem()) return;
        for(Type tp : type) {
            Number n = runes.get(tp);
            tooltip.add(Component.translatable("tooltip.hemomancy.rune", n instanceof Float ? 10f * getMultp() + "%" : n.intValue() * getMultp() * 1500, Component.translatable("rune.hemomancy." + tp.name())).withColor(tp.color));
        }
    }

    public Type getType() {
        return type.getFirst();
    }

    public Type[] getTypes() {
        return type.toArray(new Type[0]);
    }

    public int getMultp() {
        return tier.intValue();
    }

    public enum Type {
        none(0xFFFFFFFF),
        speed(0xFF08F3CC),
        sacrifices(0xFFFCC500),
        capacity(0xFFFF5000),
        resonantCapacity(0xFFFF5000),
        relations(0xFFFF00FF);

        public final int color;
        Type(int color){
            this.color = color;
        }
    }
}
