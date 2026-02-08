package ru.easynull.hemomancy.api.altar;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import ru.easynull.hemomancy.registry.HmBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class Tier {

    private static final Map<Byte, List<Component>> TIERS = new HashMap<>();

    public static void onInit() {
        registerTier((byte) 1, components -> {});

        registerTier((byte) 2, components -> {
            components.accept(Component.of(-1, -1, -1, HmBlocks.BLANK_RUNE));
            components.accept(Component.of(0, -1, -1, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(1, -1, -1, HmBlocks.BLANK_RUNE));
            components.accept(Component.of(-1, -1, 0, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(1, -1, 0, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(-1, -1, 1, HmBlocks.BLANK_RUNE));
            components.accept(Component.of(0, -1, 1, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(1, -1, 1, HmBlocks.BLANK_RUNE));
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
            components.accept(Component.of(-3, 1, -3, Blocks.GLOWSTONE));
            components.accept(Component.of(3, 1, -3, Blocks.GLOWSTONE));
            components.accept(Component.of(-3, 1, 3, Blocks.GLOWSTONE));
            components.accept(Component.of(3, 1, 3, Blocks.GLOWSTONE));

            components.accept(Component.of(-1, -1, -1, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(0, -1, -1, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(1, -1, -1, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(-1, -1, 0, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(1, -1, 0, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(-1, -1, 1, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(0, -1, 1, HmBlocks.BLANK_RUNE).upgradeSlot());
            components.accept(Component.of(1, -1, 1, HmBlocks.BLANK_RUNE).upgradeSlot());

            for (int i = -2; i <= 2; i++) {
                components.accept(Component.of(3, -2, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(-3, -2, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -2, 3, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -2, -3, HmBlocks.BLANK_RUNE).upgradeSlot());
            }
        });

        registerTier((byte) 4, components -> {
            getTiers().get((byte) 3).forEach(components);

            for (int i = -2; i <= 1; i++) {
                components.accept(Component.of(5, i, 5));
                components.accept(Component.of(5, i, -5));
                components.accept(Component.of(-5, i, -5));
                components.accept(Component.of(-5, i, 5));
            }

            components.accept(Component.of(5, 2, 5, HmBlocks.CRIMSON_ORNAMENT));
            components.accept(Component.of(5, 2, -5, HmBlocks.CRIMSON_ORNAMENT));
            components.accept(Component.of(-5, 2, -5, HmBlocks.CRIMSON_ORNAMENT));
            components.accept(Component.of(-5, 2, 5, HmBlocks.CRIMSON_ORNAMENT));

            for (int i = -4; i <= 4; i++) {
                components.accept(Component.of(5, -3, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(-5, -3, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -3, 5, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -3, -5, HmBlocks.BLANK_RUNE).upgradeSlot());
            }
        });

        registerTier((byte) 5, components -> {
            getTiers().get((byte) 4).forEach(components);

            for (int i = -1; i <= 0; i++) {
                components.accept(Component.of(9, i, 9));
                components.accept(Component.of(-9, i, -9));
                components.accept(Component.of(9, i, -9));
                components.accept(Component.of(-9, i, 9));
            }

            components.accept(Component.of(9, 1, 9, HmBlocks.CRIMSON_ORNAMENT));
            components.accept(Component.of(-9, 1, -9, HmBlocks.CRIMSON_ORNAMENT));
            components.accept(Component.of(9, 1, -9, HmBlocks.CRIMSON_ORNAMENT));
            components.accept(Component.of(-9, 1, 9, HmBlocks.CRIMSON_ORNAMENT));

            for (int i = -8; i <= 8; i++) {
                components.accept(Component.of(9, -4, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(-9, -4, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -4, 9, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -4, -9, HmBlocks.BLANK_RUNE).upgradeSlot());
            }

            for (int i = -3; i <= -2; i++) {
                components.accept(Component.of(9, i, 0));
                components.accept(Component.of(0, i, -9));
                components.accept(Component.of(-9, i, 0));
                components.accept(Component.of(0, i, 9));
            }

            components.accept(Component.of(9, -1, 0, Blocks.BEACON));
            components.accept(Component.of(0, -1, -9, Blocks.BEACON));
            components.accept(Component.of(-9, -1, 0, Blocks.BEACON));
            components.accept(Component.of(0, -1, 9, Blocks.BEACON));
        });

        registerTier((byte) 6, components -> {
            getTiers().get((byte) 5).forEach(components);

            for (int i = -1; i <= 0; i++) {
                components.accept(Component.of(14, i, 14));
                components.accept(Component.of(-14, i, -14));
                components.accept(Component.of(14, i, -14));
                components.accept(Component.of(-14, i, 14));
            }

            components.accept(Component.of(14, 1, 14, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(-14, 1, -14, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(14, 1, -14, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(-14, 1, 14, HmBlocks.TRANSCENDENTAL_CRYSTAL));

            for (int i = -12; i <= -2; i++) {
                components.accept(Component.of(14, -6, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(-14, -6, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -6, 14, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -6, -14, HmBlocks.BLANK_RUNE).upgradeSlot());
            }

            for (int i = 2; i <= 12; i++) {
                components.accept(Component.of(14, -6, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(-14, -6, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -6, 14, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -6, -14, HmBlocks.BLANK_RUNE).upgradeSlot());
            }

            for (int i = -5; i <= -4; i++) {
                components.accept(Component.of(14, i, 0));
                components.accept(Component.of(0, i, -14));
                components.accept(Component.of(-14, i, 0));
                components.accept(Component.of(0, i, 14));
            }

            components.accept(Component.of(14, -3, 0, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(0, -3, -14, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(-14, -3, 0, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(0, -3, 14, HmBlocks.TRANSCENDENTAL_CRYSTAL));

            components.accept(Component.of(14, -2, 0, Blocks.BEACON));
            components.accept(Component.of(0, -2, -14, Blocks.BEACON));
            components.accept(Component.of(-14, -2, 0, Blocks.BEACON));
            components.accept(Component.of(0, -2, 14, Blocks.BEACON));
        });

        registerTier((byte) 7, components -> {
            getTiers().get((byte) 6).forEach(components);

            for (int i = -5; i <= -1; i++) {
                components.accept(Component.of(14, i, 14));
                components.accept(Component.of(-14, i, -14));
                components.accept(Component.of(14, i, -14));
                components.accept(Component.of(-14, i, 14));
            }

            components.accept(Component.of(14, 1, 14, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(-14, 1, -14, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(14, 1, -14, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(-14, 1, 14, HmBlocks.TRANSCENDENTAL_CRYSTAL));

            for (int i = -12; i <= -2; i++) {
                components.accept(Component.of(14, -6, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(-14, -6, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -6, 14, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -6, -14, HmBlocks.BLANK_RUNE).upgradeSlot());
            }
            for (int i = 2; i <= 12; i++) {
                components.accept(Component.of(14, -6, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(-14, -6, i, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -6, 14, HmBlocks.BLANK_RUNE).upgradeSlot());
                components.accept(Component.of(i, -6, -14, HmBlocks.BLANK_RUNE).upgradeSlot());
            }

            for (int i = -5; i <= -4; i++) {
                components.accept(Component.of(14, i, 0));
                components.accept(Component.of(0, i, -14));
                components.accept(Component.of(-14, i, 0));
                components.accept(Component.of(0, i, 14));
            }

            components.accept(Component.of(14, -3, 0, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(0, -3, -14, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(-14, -3, 0, HmBlocks.TRANSCENDENTAL_CRYSTAL));
            components.accept(Component.of(0, -3, 14, HmBlocks.TRANSCENDENTAL_CRYSTAL));

            components.accept(Component.of(14, -2, 0, Blocks.BEACON));
            components.accept(Component.of(0, -2, -14, Blocks.BEACON));
            components.accept(Component.of(-14, -2, 0, Blocks.BEACON));
            components.accept(Component.of(0, -2, 14, Blocks.BEACON));
        });
    }

    public static void registerTier(byte tier, Consumer<Consumer<Component>> builder) {
        if (TIERS.containsKey(tier)) {
            throw new IllegalStateException("Tier already registered!");
        }

        List<Component> components = new ArrayList<>();
        builder.accept(components::add);
        TIERS.put(tier, components);
    }

    public static Map<Byte, List<Component>> getTiers() {
        return TIERS;
    }

    public static byte getSize() {
        return (byte) TIERS.size();
    }

    public static class Component {
        public final BlockPos pos;
        public final Block block;
        private boolean upgrade;

        public Component(BlockPos pos, @Nullable Block block) {
            this.pos = pos;
            this.block = block;
        }

        public static Component of(int x, int y, int z, Block block) {
            return new Component(new BlockPos(x, y, z), block);
        }

        public static Component of(int x, int y, int z) {
            return new Component(new BlockPos(x, y, z), null);
        }

        public Component upgradeSlot() {
            this.upgrade = true;
            return this;
        }

        public boolean isUpgrade() {
            return upgrade;
        }
    }
}