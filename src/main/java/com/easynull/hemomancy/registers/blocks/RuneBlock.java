package com.easynull.hemomancy.registers.blocks;

import com.easynull.hemomancy.core.Tierable;
import com.easynull.hemomancy.registers.HcElements;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.common.asm.enumextension.IndexedEnum;
import net.neoforged.fml.common.asm.enumextension.NamedEnum;
import net.neoforged.fml.common.asm.enumextension.NetworkedEnum;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RuneBlock extends Block implements Tierable {
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
            tooltip.add(Component.translatable("tooltip.hemomancy.rune", n instanceof Float ? 10f * getTier() + "%" : n.intValue() * getTier() * 1500, Component.translatable("rune.hemomancy." + tp.name())).withColor(tp.color));
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    public Type getType() {
        return type.getFirst();
    }

    public Type[] getTypes() {
        return type.toArray(new Type[0]);
    }

    @Override
    public byte getTier() {
        return tier.byteValue();
    }

    @NetworkedEnum(NetworkedEnum.NetworkCheck.BIDIRECTIONAL)
    @IndexedEnum
    @NamedEnum(1)
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
