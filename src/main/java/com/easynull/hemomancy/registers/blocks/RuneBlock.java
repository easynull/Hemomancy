package com.easynull.hemomancy.registers.blocks;

import com.easynull.hemomancy.core.Tierable;
import com.easynull.hemomancy.registers.HcElements;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.IndexedEnum;
import net.neoforged.fml.common.asm.enumextension.NamedEnum;
import net.neoforged.fml.common.asm.enumextension.NetworkedEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RuneBlock extends Block implements Tierable {
    private static final Map<Type, Number> runes = new HashMap<>();
    final List<Type> type;
    final byte tier;
    final Number buff;

    public RuneBlock(Properties prop, int tier, Number buff, Type... type) {
        super(prop);
        this.tier = (byte) tier;
        this.buff = buff;
        this.type = List.of(type);
        if (getType() != Type.none) runes.putIfAbsent(getType(), buff);
    }

    public RuneBlock(Properties prop, Number buff, Type... type) {
        this(prop, (byte) 1, buff, type);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (stack.getItem() == HcElements.blankRune.asItem()) return;
        for(Type tp : type) {
            Number n = runes.get(tp);
            tooltip.add(Component.translatable("tooltip.hemomancy.rune", n instanceof Float ? 10f * n.floatValue() + "%" : n.intValue() * 1500, Component.translatable("rune.hemomancy." + tp.name())).withColor(tp.color));
        }
    }

    public Type getType() {
        return type.getFirst();
    }

    public Type[] getTypes() {
        return type.toArray(new Type[0]);
    }

    @Override
    public byte getTier() {
        return tier;
    }

    @NetworkedEnum(NetworkedEnum.NetworkCheck.BIDIRECTIONAL)
    @IndexedEnum
    @NamedEnum(1)
    public enum Type {
        none(0xFFFFFFFF),
        speed(0x98C9D4FF),
        sacrifices(0xFFFCC500),
        capacity(0xFFFF5000),
        resonantCapacity(0xA6F9D1FF),
        relations(0xFFFF00FF);

        public final int color;
        Type(int color){
            this.color = color;
        }
    }
}
