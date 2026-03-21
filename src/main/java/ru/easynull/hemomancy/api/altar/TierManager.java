package ru.easynull.hemomancy.api.altar;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import ru.easynull.hemomancy.registry.HmBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static ru.easynull.hemomancy.utils.HmCommonUtils.rotatePos;

public final class TierManager {
    private static final Map<Byte, List<Component>> TIERS = new HashMap<>();

    public static void onInit() {
        add((byte) 1, components -> {});

        add((byte) 2, components -> {
            components.accept(Component.of(0, -1, 0));
            addMirrored(components, 1, -1, 0, HmBlocks.RUNE_STAIRS.getDefaultState()
                    .rotate(BlockRotation.CLOCKWISE_90), true);
            addMirrored(components, 1, -1, 1, HmBlocks.RUNE_STAIRS.getDefaultState()
                            .with(StairsBlock.SHAPE, StairShape.INNER_RIGHT), false);
            addMirrored(components, 2, -1, 1, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 2, -1, 0, null, false);
            addMirrored(components, 2, -1, -1, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 2, -1, -2, HmBlocks.RUNE_SLAB.getDefaultState(), false);
        });

        add((byte) 3, components -> {
            addMirrored(components, 2, -1, 0,
                    HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 2, -1, -2,
                    HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 1, -1, -3, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, -1, -1, -3, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 0, -1, -3, HmBlocks.RUNE_STAIRS.getDefaultState().rotate(BlockRotation.CLOCKWISE_180), true);
            addMirrored(components, -2, -1, -3, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            addMirrored(components, 2, -1, -3, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            for(int h = -1; h < 2; h++) {
                addMirrored(components, 3, h, -3, null, false);
            }
            addMirrored(components, 3, 2, -3, Blocks.SHROOMLIGHT.getDefaultState(), false);
        });

        add((byte) 4, components -> {
            addMirrored(components, 3, 3, -3, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            for(int w = -1; w < 2; w++){
                addMirrored(components, w, -1, -4, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            }
            for(int h = 0; h < 2; h++) {
                addMirrored(components, 3, h, -3, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            }
            addMirrored(components, -1, -1, -6, HmBlocks.RUNE_STAIRS.getDefaultState().rotate(BlockRotation.CLOCKWISE_90), true);
            addMirrored(components, 1, -1, -6, HmBlocks.RUNE_STAIRS.getDefaultState().rotate(BlockRotation.COUNTERCLOCKWISE_90), true);
            addMirrored(components, -2, -1, -6, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            addMirrored(components, 2, -1, -6, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            for(int h = -1; h < 1; h++) {
                addMirrored(components, 5, h, -5, null,false);
            }
            addMirrored(components, 5, 1, -5, HmBlocks.CRIMSON_ORNAMENT.getDefaultState(),false);
            addMirrored(components, 5, 2, -5, HmBlocks.RUNE_SLAB.getDefaultState(),false);
        });

        add((byte) 5, components -> {
            for(int w = -7; w < -4; w++){
                addMirrored(components, 0, -1, w, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            }
            addMirrored(components, 5, 0, -5, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, -1, 0, -6, null, false);
            addMirrored(components, 1, 0, -6, null, false);
            addMirrored(components, 0, 1, -6, HmBlocks.BLANK_RUNE.getDefaultState(), false, false);
            addMirrored(components, -1, 1, -6, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            addMirrored(components, 1, 1, -6, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            for(int h = -1; h < 1; h++) {
                addMirrored(components, 0, h, -9, null,false);
            }
            addMirrored(components, 0, 1, -9, HmBlocks.TRANSCENDENTAL_CRYSTAL.getDefaultState(), false);
            addMirrored(components, -4, -1, -7, null,false);
            addMirrored(components, 4, -1, -7, null,false);
            addMirrored(components, -4, -1, -7, null,false);
            for(int h = 0; h < 3; h++) {
                addMirrored(components, 4, h, -7, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
                addMirrored(components, -4, h, -7, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            }
            addMirrored(components, 4, 3, -7, null,false);
            addMirrored(components, -4, 3, -7, null,false);
            addMirrored(components, 4, 4, -7, HmBlocks.CRIMSON_ORNAMENT.getDefaultState(),false);
            addMirrored(components, -4, 4, -7, HmBlocks.CRIMSON_ORNAMENT.getDefaultState(),false);
            addMirrored(components, 4, -1, -3, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            addMirrored(components, 4, -1, -4, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 3, -1, -4, HmBlocks.RUNE_SLAB.getDefaultState(), false);
        });

        add((byte) 6, components -> {
            addMirrored(components, 0, 0, -9, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 0, 2, -9, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            addMirrored(components, 7, -1, -7, null, false);
            for(int h = 0; h < 2; h++) {
                addMirrored(components, 7, h, -7, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            }
            addMirrored(components, -1, 0, -6, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 1, 0, -6, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 0, 1, -6, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 7, 2, -7, Blocks.BEACON.getDefaultState(), false);
            addMirrored(components, 4, 3, -7, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, -4, 3, -7, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, -4, 5, -7, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            addMirrored(components, 4, 5, -7, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            addMirrored(components, 5, -1, -4, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            addMirrored(components, -4, -1, 5, HmBlocks.RUNE_SLAB.getDefaultState(), false);

            for(int w = -4; w < 5; w++) {
                if (w == 2 || w == -2 || w == 0) continue;
                addMirrored(components, w, -1, -9, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            }
            addMirrored(components, 2, -1, -9, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            addMirrored(components, -2, -1, -9, HmBlocks.RUNE_SLAB.getDefaultState(), false);
            addMirrored(components, -4, -1, -8, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            addMirrored(components, 4, -1, -8, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            for(int w = -5; w > -7; w--) {
                addMirrored(components, w, -1, -7, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
                addMirrored(components, -w, -1, -7, HmBlocks.BLANK_RUNE.getDefaultState(), true, false);
            }
        });
    }

    public static void add(byte tier, Consumer<Consumer<Component>> builder) {
        if (TIERS.containsKey(tier)) {
            throw new IllegalStateException("Tier already registered!");
        }
        List<Component> components = new ArrayList<>();

        if (tier > 1) {
            List<Component> prev = TIERS.get((byte) (tier - 1));
            if (prev != null) {
                components.addAll(prev);
            }
        }

        List<Component> newComponents = new ArrayList<>();
        builder.accept(newComponents::add);

        for (Component newComp : newComponents) {
            boolean replaced = false;
            for (int i = 0; i < components.size(); i++) {
                Component existing = components.get(i);
                if (existing.pos.equals(newComp.pos)) {
                    components.set(i, newComp);
                    replaced = true;
                    break;
                }
            }
            if (!replaced) {
                components.add(newComp);
            }
        }
        TIERS.put(tier, components);
    }

    public static Map<Byte, List<Component>> getTiers() {
        return TIERS;
    }

    public static void addMirrored(Consumer<Component> components, int relX, int relY, int relZ, @Nullable BlockState state, boolean universal, boolean rotable) {
        BlockPos basePos = new BlockPos(relX, relY, relZ);
        Component baseComp = new Component(basePos, state, rotable);
        if (universal) {
            baseComp.universal();
        }
        components.accept(baseComp);

        BlockRotation[] rotations = {
                BlockRotation.CLOCKWISE_90,
                BlockRotation.CLOCKWISE_180,
                BlockRotation.COUNTERCLOCKWISE_90
        };

        int[] reverseIndices = {2, 1, 0};

        for (int i = 0; i < rotations.length; i++) {
            BlockRotation rot = rotations[i];

            BlockPos rotatedRelPos = rotatePos(basePos, rot);

            BlockRotation oRot = rotations[reverseIndices[i]];

            BlockState rotatedState = rotable ? state != null ? state.rotate(oRot) : null : state;

            Component mirroredComp = new Component(rotatedRelPos, rotatedState, rotable);
            if (universal) {
                mirroredComp.universal();
            }

            components.accept(mirroredComp);
        }
    }

    public static void addMirrored(Consumer<Component> components, int relX, int relY, int relZ, @Nullable BlockState state, boolean rotable) {
        addMirrored(components, relX, relY, relZ, state, false, rotable);
    }

    public static class Component {
        public final BlockPos pos;
        public final BlockState state;
        public final boolean stateble;
        private boolean universal;

        public Component(BlockPos pos, @Nullable BlockState state, boolean stateble) {
            this.pos = pos;
            this.state = state;
            this.stateble = stateble;
        }

        public static Component of(int x, int y, int z, Block block) {
            return new Component(new BlockPos(x, y, z), block.getDefaultState(), false);
        }

        public static Component of(int x, int y, int z) {
            return new Component(new BlockPos(x, y, z), null, false);
        }

        public Component universal() {
            this.universal = true;
            return this;
        }

        public boolean isUniversal() {
            return universal;
        }

        @Override
        public String toString() {
            return String.format("[Block %s with pos %s]", state, pos.toString());
        }
    }
}