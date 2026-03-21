package ru.easynull.hemomancy.registry.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import ru.easynull.hemomancy.api.energy.Tierable;
import ru.easynull.hemomancy.registry.HmBlocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RuneBlock extends Block implements Tierable {
    public static final Map<Type, Number> RUNE_VALUES = new HashMap<>();

    private final List<Type> types;
    private final byte tier;

    public RuneBlock(Settings settings, int tier, Number buff, Type... types) {
        super(settings);
        this.tier = (byte) tier;
        this.types = List.of(types);

        if (getPrimaryType() != Type.NONE && buff != null) {
            RUNE_VALUES.putIfAbsent(getPrimaryType(), buff);
        }
    }

    public RuneBlock(Settings settings, Number buff, Type... types) {
        this(settings, 1, buff, types);
    }

    @Override
    public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
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

            Text text = Text.translatable("tooltip.hemomancy.rune", formattedValue, Text.translatable("rune.hemomancy." + type.name().toLowerCase())).setStyle(Style.EMPTY.withColor(type.color));

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