package com.easynull.hemomancy.core.altar;

import com.easynull.hemomancy.registers.HcElements;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Tier {
    private static final Map<Byte, List<Component>> tiers = new HashMap<>();

    public static void init(){
        registerTier((byte) 1, components -> {});
        registerTier((byte) 2, components -> {
            components.accept(Component.of(-1, -1, -1, HcElements.rune));
            components.accept(Component.of(0, -1, -1, HcElements.rune).upgradeSlot());
            components.accept(Component.of(1, -1, -1, HcElements.rune));
            components.accept(Component.of(-1, -1, 0, HcElements.rune).upgradeSlot());
            components.accept(Component.of(1, -1, 0, HcElements.rune).upgradeSlot());
            components.accept(Component.of(-1, -1, 1, HcElements.rune));
            components.accept(Component.of(0, -1, 1, HcElements.rune).upgradeSlot());
            components.accept(Component.of(1, -1, 1, HcElements.rune));
        });
        registerTier((byte) 3, components -> {
            components.accept(Component.of(-3, -1, -3));
            components.accept(Component.of(-3, 0, -3));
            components.accept(Component.of(3, -1, -3));
            components.accept(Component.of(3, 0, -3));
            components.accept(Component.of(-3, -1, 3));
            components.accept(Component.of(-3, 0, 3));
            components.accept(Component.of(3, -1, 3));
            components.accept(Component.of(3, 0, 3));
            components.accept(Component.of(-3, 1, -3, ()-> Blocks.GLOWSTONE));
            components.accept(Component.of(3, 1, -3, ()-> Blocks.GLOWSTONE));
            components.accept(Component.of(-3, 1, 3, ()-> Blocks.GLOWSTONE));
            components.accept(Component.of(3, 1, 3, ()-> Blocks.GLOWSTONE));

            components.accept(Component.of(-1, -1, -1, HcElements.rune).upgradeSlot());
            components.accept(Component.of(0, -1, -1, HcElements.rune).upgradeSlot());
            components.accept(Component.of(1, -1, -1, HcElements.rune).upgradeSlot());
            components.accept(Component.of(-1, -1, 0, HcElements.rune).upgradeSlot());
            components.accept(Component.of(1, -1, 0, HcElements.rune).upgradeSlot());
            components.accept(Component.of(-1, -1, 1, HcElements.rune).upgradeSlot());
            components.accept(Component.of(0, -1, 1, HcElements.rune).upgradeSlot());
            components.accept(Component.of(1, -1, 1, HcElements.rune).upgradeSlot());

            for (int i = -2; i <= 2; i++) {
                components.accept(Component.of(3, -2, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(-3, -2, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -2, 3, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -2, -3, HcElements.rune).upgradeSlot());
            }
        });
        registerTier((byte) 4, components -> {
            getTiers().get((byte)3).forEach(components);
            for (int i = -2; i <= 1; i++) {
                components.accept(Component.of(5, i, 5));
                components.accept(Component.of(5, i, -5));
                components.accept(Component.of(-5, i, -5));
                components.accept(Component.of(-5, i, 5));
            }
            components.accept(Component.of(5, 2, 5, HcElements.bloodOrnament));
            components.accept(Component.of(5, 2, -5, HcElements.bloodOrnament));
            components.accept(Component.of(-5, 2, -5, HcElements.bloodOrnament));
            components.accept(Component.of(-5, 2, 5, HcElements.bloodOrnament));

            for (int i = -3; i <= 3; i++) {
                components.accept(Component.of(5, -3, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(-5, -3, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -3, 5, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -3, -5, HcElements.rune).upgradeSlot());
            }
        });
        registerTier((byte) 5, components -> {
            getTiers().get((byte)4).forEach(components);
            for (int i = -3; i <= -2; i++) {
                components.accept(Component.of(-8, i, 8));
                components.accept(Component.of(-8, i, -8));
                components.accept(Component.of(8, i, -8));
                components.accept(Component.of(8, i, 8));
            }
            components.accept(Component.of(-8, -1, 8, ()-> Blocks.BEACON));
            components.accept(Component.of(-8, -1, -8, ()-> Blocks.BEACON));
            components.accept(Component.of(8, -1, -8, ()-> Blocks.BEACON));
            components.accept(Component.of(8, -1, 8, ()-> Blocks.BEACON));

            for (int i = -6; i <= 6; i++) {
                components.accept(Component.of(8, -4, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(-8, -4, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -4, 8, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -4, -8, HcElements.rune).upgradeSlot());
            }
        });
        registerTier((byte) 6, components -> {
            getTiers().get((byte)5).forEach(components);
            for (int i = -4; i <= 1; i++) {
                components.accept(Component.of(11, i, 11));
                components.accept(Component.of(-11, i, -11));
                components.accept(Component.of(11, i, -11));
                components.accept(Component.of(-11, i, 11));
            }
            components.accept(Component.of(11, 3, 11, HcElements.transcendentalCrystal));
            components.accept(Component.of(-11, 3, -11, HcElements.transcendentalCrystal));
            components.accept(Component.of(11, 3, -11, HcElements.transcendentalCrystal));
            components.accept(Component.of(-11, 3, 11, HcElements.transcendentalCrystal));

            for (int i = -9; i <= 9; i++) {
                components.accept(Component.of(11, -5, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(-11, -5, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -5, 11, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -5, -11, HcElements.rune).upgradeSlot());
            }
        });
        registerTier((byte) 7, components -> {
            getTiers().get((byte)6).forEach(components);
            for (int i = -5; i <= -1; i++) {
                components.accept(Component.of(14, i, 14));
                components.accept(Component.of(-14, i, -14));
                components.accept(Component.of(14, i, -14));
                components.accept(Component.of(-14, i, 14));
            }
            components.accept(Component.of(14, 1, 14, HcElements.transcendentalCrystal));
            components.accept(Component.of(-14, 1, -14, HcElements.transcendentalCrystal));
            components.accept(Component.of(14, 1, -14, HcElements.transcendentalCrystal));
            components.accept(Component.of(-14, 1, 14, HcElements.transcendentalCrystal));

            for (int i = -12; i <= -2; i++) {
                components.accept(Component.of(14, -6, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(-14, -6, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -6, 14, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -6, -14, HcElements.rune).upgradeSlot());
            }
            for (int i = 2; i <= 12; i++) {
                components.accept(Component.of(14, -6, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(-14, -6, i, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -6, 14, HcElements.rune).upgradeSlot());
                components.accept(Component.of(i, -6, -14, HcElements.rune).upgradeSlot());
            }

            for (int i = -5; i <= -4; i++) {
                components.accept(Component.of(14, i, 0));
                components.accept(Component.of(0, i, -14));
                components.accept(Component.of(-14, i, 0));
                components.accept(Component.of(0, i, 14));
            }
            components.accept(Component.of(14, -3, 0, HcElements.transcendentalCrystal));
            components.accept(Component.of(0, -3, -14, HcElements.transcendentalCrystal));
            components.accept(Component.of(14, -3, 0, HcElements.transcendentalCrystal));
            components.accept(Component.of(0, -3, 14, HcElements.transcendentalCrystal));
            components.accept(Component.of(14, -2, 0, ()-> Blocks.BEACON));
            components.accept(Component.of(0, -2, -14, ()-> Blocks.BEACON));
            components.accept(Component.of(-14, -2, 0, ()-> Blocks.BEACON));
            components.accept(Component.of(0, -2, 14, ()-> Blocks.BEACON));
        });
    }

    public static void registerTier(byte tier, Consumer<Consumer<Component>> builder) {
        if (tiers.containsKey(tier)) throw new IllegalStateException("Tier already registered!");
        List<Component> components = new ArrayList<>();
        builder.accept(components::add);
        tiers.put(tier, components);
    }

    public static Map<Byte, List<Component>> getTiers() {
        return tiers;
    }

    public static byte getMaxTier(){
        return (byte) tiers.size();
    }

    public static class Component{
        public final BlockPos pos;
        public final Block block;
        private boolean upgrade;

        public Component(BlockPos pos, @Nullable Supplier<Block> block) {
            this.pos = pos;
            this.block = block != null ? block.get() : null;
        }

        public static Component of(int x, int y, int z, Supplier<Block> block){
            return new Component(new BlockPos(x, y, z), block);
        }

        public static Component of(int x, int y, int z){
            return new Component(new BlockPos(x, y, z), null);
        }

        public Component upgradeSlot(){
            this.upgrade = true;
            return this;
        }

        public boolean isUpgrade() {
            return upgrade;
        }
    }
}