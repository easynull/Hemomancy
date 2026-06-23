package ru.easynull.hemomancy.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.items.BookItem;
import ru.easynull.hemomancy.registry.items.DaggerItem;
import ru.easynull.hemomancy.registry.items.OrbItem;
import ru.easynull.hemomancy.registry.items.RitualStaffItem;
import ru.easynull.hemomancy.registry.items.sigil.EnabledSigilItem;
import ru.easynull.hemomancy.registry.items.sigil.SigilItem;
import ru.easynull.hemomancy.registry.items.tool.DesecratedAxeItem;
import ru.easynull.hemomancy.registry.items.tool.DesecratedPickaxeItem;
import ru.easynull.hemomancy.registry.items.tool.DesecratedShovelItem;
import ru.easynull.hemomancy.registry.items.tool.DesecratedSwordItem;
import ru.easynull.hemomancy.utils.HmCommonUtils;

import java.util.List;

import static ru.easynull.hemomancy.registry.HmBlocks.*;

public final class HmItems {
    // Orbs
    public static final Item WEAK_BLOOD_ORB = registerItem("weak_blood_orb", new OrbItem(1, 5000, 1));
    public static final Item APPRENTICE_BLOOD_ORB = registerItem("apprentice_blood_orb", new OrbItem(2, 25000, 3));
    public static final Item MAGICIAN_BLOOD_ORB = registerItem("magician_blood_orb", new OrbItem(3, 150000, 10));
    public static final Item MASTER_BLOOD_ORB = registerItem("master_blood_orb", new OrbItem(new Item.Properties().rarity(Rarity.UNCOMMON), 4, 1000000, 20));
    public static final Item ARCHMAGE_BLOOD_ORB = registerItem("archmage_blood_orb", new OrbItem(new Item.Properties().rarity(Rarity.RARE), 5, 10000000, 45));
    public static final Item TRANSCENDENTAL_BLOOD_ORB = registerItem("transcendental_blood_orb", new OrbItem(new Item.Properties().rarity(Rarity.EPIC), 6, 30000000, 100));
    public static final Item INEXHAUSTIBLE_BLOOD_ORB = null;//registerItem("inexhaustible_blood_orb", new OrbItem(new Item.Settings().rarity(Rarity.EPIC), Byte.MAX_VALUE, 300000000000000L, 999), "avaritia");

    // Glyphs
    public static final Item BLANK_GLYPH = registerItem("blank_glyph", new Item(new Item.Properties()));
    public static final Item FORTIFIED_GLYPH = registerItem("fortified_glyph", new Item(new Item.Properties()));
    public static final Item CRIMSON_GLYPH = registerItem("crimson_glyph", new Item(new Item.Properties()));
    public static final Item FILLED_GLYPH = registerItem("filled_glyph", new Item(new Item.Properties()));
    public static final Item DEMONIC_GLYPH = registerItem("demonic_glyph", new Item(new Item.Properties()));
    public static final Item INFERNAL_GLYPH = registerItem("infernal_glyph", new Item(new Item.Properties().rarity(Rarity.RARE)));

    // Other
    public static final Item BOOK = registerItem("book", new BookItem(new Item.Properties()));
    public static final Item RITUAL_STAFF = registerItem("ritual_staff", new RitualStaffItem());
    public static final Item CRIMSON_STEEL_INGOT = registerItem("crimson_steel_ingot", new Item(new Item.Properties()));
    public static final Item BLOOD_BUCKET = registerItem("blood_bucket", new BucketItem(HmFluids.BLOOD, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    // Sigils
    public static final Item WATER_SIGIL = registerItem("water_sigil", new SigilItem(
            new Item.Properties(),
            ctx -> {
                BlockPos target = ctx.pos().relative(ctx.side());
                ctx.level().setBlock(target, Blocks.WATER.defaultBlockState(), Block.UPDATE_CLIENTS);
            },
            150
    ));

    public static final Item LAVA_SIGIL = registerItem("lava_sigil", new SigilItem(
            new Item.Properties(),
            ctx -> {
                BlockPos target = ctx.pos().relative(ctx.side());
                ctx.level().setBlock(target, Blocks.LAVA.defaultBlockState(), Block.UPDATE_CLIENTS);
            },
            150
    ));

    public static final Item DRAINAGE_SIGIL = registerItem("drainage_sigil", new EnabledSigilItem(
            new Item.Properties(),
            ctx -> HmCommonUtils.forEachInCube(ctx.pos(), 3, p -> {
                BlockState state = ctx.level().getBlockState(p);
                if (state.getBlock() instanceof BucketPickup drain) {
                    if (!drain.pickupBlock(ctx.player(), ctx.level(), p, state).isEmpty()) {
                        return;
                    }
                }

                if (state.getBlock() instanceof LiquidBlock) {
                    ctx.level().setBlock(p, Blocks.AIR.defaultBlockState(), 3);
                }
            }), 50, 20, 8
    ));

    public static final Item AIR_SIGIL = registerItem("air_sigil", new SigilItem(
            new Item.Properties(),
            ctx -> {
                Player player = ctx.player();
                player.getEnderChestInventory().startOpen(player);

                Vec3 look = player.getLookAngle();
                float strength = 1.8f;
                player.setDeltaMovement(look.scale(strength).add(0, 0.5, 0));
                player.hasImpulse = true;
                player.fallDistance = 0;
                player.startFallFlying();

                ctx.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 1.0f, 1.0f);
            }, 45, true
    ));

    public static final Item MAGNETISM_SIGIL = registerItem("magnetism_sigil", new EnabledSigilItem(
            new Item.Properties(),
            ctx -> HmCommonUtils.attractEntities(ctx.level(), ctx.pos(), 12f, 1.1f, ItemEntity.class, e -> true),
            150, 3, 80
    ));

    public static final Item RESISTANCE_SIGIL = registerItem("resistance_sigil", new EnabledSigilItem(
            new Item.Properties(),
            ctx -> ctx.player().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 4, false, false, false)), 2500, 40
    ));

    public static final Item MOVEMENT_SIGIL = registerItem("movement_sigil", new SigilItem(
            new Item.Properties(),
            ctx -> {
                Level level = ctx.level();
                BlockPos pos = ctx.pos();
                BlockState state = level.getBlockState(pos);
                ItemStack stack = ctx.stack();

                if (state.getFluidState().isSource()) return;
                stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> {
                    CompoundTag nbt = data.copyTag();
                    if (!nbt.contains("BlockState")) {
                        BlockEntity be = level.getBlockEntity(pos);
                        if (be != null) {
                            CompoundTag tag = be.saveWithFullMetadata(level.registryAccess());
                            nbt.put("BlockEntity", tag);
                            level.removeBlockEntity(pos);
                        }
                        nbt.put("BlockState", NbtUtils.writeBlockState(state));
                        nbt.put("Tooltip", HmCommonUtils.fromList(List.of(state.getBlock().getName()), level.registryAccess()));
                        level.removeBlock(pos, false);
                    } else {
                        CompoundTag stateNbt = nbt.getCompound("BlockState");
                        BlockState stored = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), stateNbt);

                        BlockPos placePos = pos.relative(ctx.side());
                        level.setBlock(placePos, stored, Block.UPDATE_CLIENTS);

                        if (nbt.contains("BlockEntity")) {
                            CompoundTag tag = nbt.getCompound("BlockEntity");
                            BlockEntity be = BlockEntity.loadStatic(placePos, stored, tag, level.registryAccess());
                            if (be != null) level.setBlockEntity(be);
                        }

                        nbt.remove("BlockState");
                        nbt.remove("BlockEntity");
                        nbt.remove("Tooltip");
                    }
                    return CustomData.of(nbt);
                });
            }, 3000, false
    ));

    public static final Item TELEPOSITION_SIGIL = registerItem("teleposition_sigil", new SigilItem(
            new Item.Properties(),
            ctx -> {
                ItemStack stack = ctx.stack();
                stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> {
                    CompoundTag nbt = data.copyTag();

                    if (!nbt.contains("X")) {
                        BlockPos pos = ctx.pos();
                        nbt.putInt("X", pos.getX());
                        nbt.putInt("Y", pos.getY());
                        nbt.putInt("Z", pos.getZ());

                        nbt.putString("Tooltip", String.format("X: %d, Y: %d, Z: %d", pos.getX(), pos.getY(), pos.getZ()));
                        ctx.item().cancelConsumeLp();
                    } else {
                        Player player = ctx.player();
                        if (player == null) return CustomData.of(nbt);

                        if (player.isShiftKeyDown()) {
                            nbt.remove("X");
                            nbt.remove("Y");
                            nbt.remove("Z");
                            nbt.remove("Tooltip");
                            return CustomData.of(nbt);
                        }

                        int x = nbt.getInt("X");
                        int y = nbt.getInt("Y");
                        int z = nbt.getInt("Z");

                        player.teleportTo(x + 0.5, y + 1, z + 0.5);
                    }
                    return CustomData.of(nbt);
                });
            }, 1500, true
    ));

    public static final Item GROW_SIGIL = registerItem("grow_sigil", new EnabledSigilItem(
            new Item.Properties(),
            ctx -> {
                if(ctx.level().isClientSide()) return;
                if (!ctx.level().isDay()) return;

                BlockPos center = ctx.pos();
                Level level = ctx.level();
                HmCommonUtils.forEachInCube(center, 5, pos -> {
                    RandomSource random = level.random;
                    BlockState state = level.getBlockState(pos);
                    Block block = state.getBlock();

                    if (block == Blocks.DIRT && random.nextFloat() < 0.1f && level.getBlockState(pos.above()).isAir()) {
                        level.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), Block.UPDATE_CLIENTS);
                    } else if (block instanceof FarmBlock && random.nextFloat() < 0.1f) {
                        int moisture = state.getValue(FarmBlock.MOISTURE);
                        if (moisture < 7) {
                            level.setBlock(pos, state.setValue(FarmBlock.MOISTURE, 7), Block.UPDATE_CLIENTS);
                        }
                    } else if (block instanceof BonemealableBlock fertilizable && random.nextFloat() < 0.5f && !(block instanceof GrassBlock)) {
                        if (fertilizable.isValidBonemealTarget(level, pos, state)) {
                            if (fertilizable.isBonemealSuccess(level, level.random, pos, state)) {
                                fertilizable.performBonemeal((ServerLevel) level, level.random, pos, state);
                            }
                        }
                    }
                });
            }, 150, 40
    ));

    // Tools
    public static final Item SACRIFICIAL_DAGGER = registerItem("sacrificial_dagger", new DaggerItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Item DESECRATED_PICKAXE = registerItem("desecrated_pickaxe", new DesecratedPickaxeItem(new Item.Properties().rarity(Rarity.EPIC), false));
    public static final Item DESECRATED_AXE = registerItem("desecrated_axe", new DesecratedAxeItem(new Item.Properties().rarity(Rarity.EPIC), false));
    public static final Item DESECRATED_SWORD = registerItem("desecrated_sword", new DesecratedSwordItem(new Item.Properties().rarity(Rarity.EPIC), false));
    public static final Item DESECRATED_SHOVEL = registerItem("desecrated_shovel", new DesecratedShovelItem(new Item.Properties().rarity(Rarity.EPIC), false));

    public static final Item AWAKENED_DESECRATED_PICKAXE = registerItem("awakened_desecrated_pickaxe", new DesecratedPickaxeItem(new Item.Properties().rarity(Rarity.EPIC), true));
    public static final Item AWAKENED_DESECRATED_AXE = registerItem("awakened_desecrated_axe", new DesecratedAxeItem(new Item.Properties().rarity(Rarity.EPIC), true));
    public static final Item AWAKENED_DESECRATED_SWORD = registerItem("awakened_desecrated_sword", new DesecratedSwordItem(new Item.Properties().rarity(Rarity.EPIC), true));
    public static final Item AWAKENED_DESECRATED_SHOVEL = registerItem("awakened_desecrated_shovel", new DesecratedShovelItem(new Item.Properties().rarity(Rarity.EPIC), true));

    public static final ResourceKey<CreativeModeTab> TAB = registerTab("hemomancy", Component.translatable("tab.hemomancy"), BOOK,
            BOOK, BLOOD_ALTAR, BLANK_RUNE, SPEED_RUNE, SACRIFICES_RUNE, CAPACITY_RUNE, RESONANT_CAPACITY_RUNE, RELATIONS_RUNE, CHIMERIC_RUNE, RITUAL_STAFF, RITUAL_STONE,
            SACRIFICIAL_DAGGER, MAGE_STATUE,
            WEAK_BLOOD_ORB, APPRENTICE_BLOOD_ORB, MAGICIAN_BLOOD_ORB, MASTER_BLOOD_ORB, ARCHMAGE_BLOOD_ORB, TRANSCENDENTAL_BLOOD_ORB, INEXHAUSTIBLE_BLOOD_ORB,
            BLANK_GLYPH, FORTIFIED_GLYPH, CRIMSON_GLYPH, FILLED_GLYPH, DEMONIC_GLYPH, INFERNAL_GLYPH,
            CRIMSON_STEEL_INGOT, CRIMSON_ORNAMENT, TRANSCENDENTAL_CRYSTAL,
            AIR_SIGIL, MAGNETISM_SIGIL, WATER_SIGIL, LAVA_SIGIL, DRAINAGE_SIGIL, RESISTANCE_SIGIL, MOVEMENT_SIGIL, TELEPOSITION_SIGIL, GROW_SIGIL,
            ALCHEMY_TABLE, RUNE_STAIRS, RUNE_SLAB, BLOOD_BUCKET,
            DESECRATED_PICKAXE, DESECRATED_AXE, DESECRATED_SWORD, DESECRATED_SHOVEL,
            AWAKENED_DESECRATED_PICKAXE, AWAKENED_DESECRATED_AXE, AWAKENED_DESECRATED_SWORD, AWAKENED_DESECRATED_SHOVEL
    );

    private static <T extends Item> T registerItem(String name, T item, String requiredMod) {
        if (requiredMod != null && !FabricLoader.getInstance().isModLoaded(requiredMod)) return null;
        var id = Hemomancy.path(name);
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    private static <T extends Item> T registerItem(String name, T item) {
        return registerItem(name, item, null);
    }

    private static ResourceKey<CreativeModeTab> registerTab(String name, Component title, ItemLike icon, ItemLike... items) {
        var key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Hemomancy.path(name));

        var group = FabricItemGroup.builder().title(title).icon(() -> new ItemStack(icon))
                .displayItems((ctx, entries) -> {
                    for (var item : items) {
                        if (item != null) entries.accept(item);
                    }
                }).build();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, group);
        return key;
    }

    public static void onInit() {}
}