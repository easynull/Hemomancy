package ru.easynull.hemomancy.registry.blocks;

import net.minecraft.world.item.Item;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.registry.HmBlocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;

public final class RuneBlock extends Block implements Tierable {
    public static final Map<Type, Number> RUNE_VALUES = new HashMap<>();

    private final List<Type> types;
    private final byte tier;

    public RuneBlock(Properties settings, int tier, Number buff, Type... types) {
        super(settings);
        this.tier = (byte) tier;
        this.types = List.of(types);

        if (getPrimaryType() != Type.NONE && buff != null) {
            RUNE_VALUES.putIfAbsent(getPrimaryType(), buff);
        }
    }

    public RuneBlock(Properties settings, Number buff, Type... types) {
        this(settings, 1, buff, types);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (stack.getItem() == HmBlocks.BLANK_RUNE.asItem()) {
            return;
        }

        for (Type type : types) {
            Number value = RUNE_VALUES.get(type);
            if (value == null) continue;

            String formattedValue;
            if (value instanceof Float f) {
                formattedValue = String.format("%.1f%%", 10f * f);
            } else {
                formattedValue = String.valueOf(value.intValue());
            }

            Component text = Component.translatable("tooltip.hemomancy.rune", formattedValue, Component.translatable("rune.hemomancy." + type.name().toLowerCase())).setStyle(Style.EMPTY.withColor(type.color));

            tooltip.add(text);
        }
    }

    public Type getPrimaryType() {
        return types.isEmpty() ? Type.NONE : types.get(0);
    }

    public Type[] getTypes() {
        return types.toArray(new Type[0]);
    }

    @Override
    public byte getTier() { return tier; }

    public enum Type {
        NONE(0xFFFFFFFF),
        SPEED(0x98C9D4FF),
        SACRIFICES(0xFFFCC500),
        CAPACITY(0xFFFF5000),
        RESONANT_CAPACITY(0xA6F9D1FF),
        RELATIONS(0xFFFF00FF);

        public final int color;

        Type(int color) {
            this.color = color;
        }
    }
}