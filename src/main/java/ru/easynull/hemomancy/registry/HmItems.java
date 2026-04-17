package ru.easynull.hemomancy.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
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
    public static final Item MASTER_BLOOD_ORB = registerItem("master_blood_orb", new OrbItem(new Item.Settings().rarity(Rarity.UNCOMMON), 4, 1000000, 20));
    public static final Item ARCHMAGE_BLOOD_ORB = registerItem("archmage_blood_orb", new OrbItem(new Item.Settings().rarity(Rarity.RARE), 5, 10000000, 45));
    public static final Item TRANSCENDENTAL_BLOOD_ORB = registerItem("transcendental_blood_orb", new OrbItem(new Item.Settings().rarity(Rarity.EPIC), 6, 30000000, 100));
    public static final Item INEXHAUSTIBLE_BLOOD_ORB = null;//registerItem("inexhaustible_blood_orb", new OrbItem(new Item.Settings().rarity(Rarity.EPIC), Byte.MAX_VALUE, 300000000000000L, 999), "avaritia");

    // Glyphs
    public static final Item BLANK_GLYPH = registerItem("blank_glyph", new Item(new Item.Settings()));
    public static final Item FORTIFIED_GLYPH = registerItem("fortified_glyph", new Item(new Item.Settings()));
    public static final Item CRIMSON_GLYPH = registerItem("crimson_glyph", new Item(new Item.Settings()));
    public static final Item FILLED_GLYPH = registerItem("filled_glyph", new Item(new Item.Settings()));
    public static final Item DEMONIC_GLYPH = registerItem("demonic_glyph", new Item(new Item.Settings()));
    public static final Item INFERNAL_GLYPH = registerItem("infernal_glyph", new Item(new Item.Settings().rarity(Rarity.RARE)));

    // Other
    public static final Item BOOK = registerItem("book", new BookItem(new Item.Settings()));
    public static final Item RITUAL_STAFF = registerItem("ritual_staff", new RitualStaffItem());
    public static final Item CRIMSON_STEEL_INGOT = registerItem("crimson_steel_ingot", new Item(new Item.Settings()));

    // Sigils
    public static final Item WATER_SIGIL = registerItem("water_sigil", new SigilItem(
            new Item.Settings(),
            ctx -> {
                BlockPos target = ctx.pos().offset(ctx.side());
                ctx.world().setBlockState(target, Blocks.WATER.getDefaultState(), Block.NOTIFY_LISTENERS);
            },
            150
    ));

    public static final Item LAVA_SIGIL = registerItem("lava_sigil", new SigilItem(
            new Item.Settings(),
            ctx -> {
                BlockPos target = ctx.pos().offset(ctx.side());
                ctx.world().setBlockState(target, Blocks.LAVA.getDefaultState(), Block.NOTIFY_LISTENERS);
            },
            150
    ));

    public static final Item DRAINAGE_SIGIL = registerItem("drainage_sigil", new EnabledSigilItem(
            new Item.Settings(),
            ctx -> HmCommonUtils.forEachInCube(ctx.pos(), 3, p -> {
                BlockState state = ctx.world().getBlockState(p);
                if (state.getBlock() instanceof FluidDrainable drain) {
                    if (!drain.tryDrainFluid(ctx.world(), p, state).isEmpty()) {
                        return;
                    }
                }

                if (state.getBlock() instanceof FluidBlock) {
                    ctx.world().setBlockState(p, Blocks.AIR.getDefaultState(), 3);
                }
            }), 50, 20, 8
    ));

    public static final Item AIR_SIGIL = registerItem("air_sigil", new SigilItem(
            new Item.Settings(),
            ctx -> {
                PlayerEntity player = ctx.player();
                player.getEnderChestInventory().onOpen(player);

                Vec3d look = player.getRotationVector();
                float strength = 1.8f;
                player.setVelocity(look.multiply(strength).add(0, 0.5, 0));
                player.velocityDirty = true;
                player.fallDistance = 0;
                player.startFallFlying();

                ctx.world().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_TRIDENT_RIPTIDE_3, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }, 45, true
    ));

    public static final Item MAGNETISM_SIGIL = registerItem("magnetism_sigil", new EnabledSigilItem(
            new Item.Settings(),
            ctx -> HmCommonUtils.attractEntities(ctx.world(), ctx.pos(), 12f, 1.1f, ItemEntity.class, e -> true),
            150, 3, 80
    ));

    public static final Item RESISTANCE_SIGIL = registerItem("resistance_sigil", new EnabledSigilItem(
            new Item.Settings(),
            ctx -> ctx.player().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2, 4, false, false, false)), 2500, 40
    ));

    public static final Item MOVEMENT_SIGIL = registerItem("movement_sigil", new SigilItem(
            new Item.Settings(),
            ctx -> {
                World world = ctx.world();
                BlockPos pos = ctx.pos();
                BlockState state = world.getBlockState(pos);
                ItemStack stack = ctx.stack();

                if (state.getFluidState().isStill()) return;

                NbtCompound nbt = stack.getOrCreateNbt();

                if (!nbt.contains("BlockState")) {
                    BlockEntity be = world.getBlockEntity(pos);
                    if (be != null) {
                        NbtCompound tag = be.createNbtWithIdentifyingData();
                        nbt.put("BlockEntity", tag);
                        world.removeBlockEntity(pos);
                    }
                    nbt.put("BlockState", NbtHelper.fromBlockState(state));
                    nbt.put("Tooltip", HmCommonUtils.fromList(List.of(state.getBlock().getName())));
                    world.removeBlock(pos, false);
                } else {
                    NbtCompound stateNbt = nbt.getCompound("BlockState");
                    BlockState stored = NbtHelper.toBlockState(Registries.BLOCK.getReadOnlyWrapper(), stateNbt);
                    if (stored == null) return;

                    BlockPos placePos = pos.offset(ctx.side());
                    world.setBlockState(placePos, stored, Block.NOTIFY_LISTENERS);

                    if (nbt.contains("BlockEntity")) {
                        NbtCompound tag = nbt.getCompound("BlockEntity");
                        BlockEntity be = BlockEntity.createFromNbt(placePos, stored, tag);
                        if (be != null) world.addBlockEntity(be);
                    }

                    nbt.remove("BlockState");
                    nbt.remove("BlockEntity");
                    nbt.remove("Tooltip");
                }
            }, 3000, false
    ));

    public static final Item TELEPOSITION_SIGIL = registerItem("teleposition_sigil", new SigilItem(
            new Item.Settings(),
            ctx -> {
                ItemStack stack = ctx.stack();
                NbtCompound nbt = stack.getOrCreateNbt();

                if (!nbt.contains("X")) {
                    BlockPos pos = ctx.pos();
                    nbt.putInt("X", pos.getX());
                    nbt.putInt("Y", pos.getY());
                    nbt.putInt("Z", pos.getZ());

                    nbt.putString("Tooltip", String.format("X: %d, Y: %d, Z: %d", pos.getX(), pos.getY(), pos.getZ()));
                    ctx.item().cancelConsumeLp();
                } else {
                    PlayerEntity player = ctx.player();
                    if (player == null) return;

                    if (player.isSneaking()) {
                        nbt.remove("X");
                        nbt.remove("Y");
                        nbt.remove("Z");
                        nbt.remove("Tooltip");
                        return;
                    }

                    int x = nbt.getInt("X");
                    int y = nbt.getInt("Y");
                    int z = nbt.getInt("Z");

                    player.teleport(x + 0.5, y + 1, z + 0.5);
                }
            }, 1500, true
    ));

    public static final Item GROW_SIGIL = registerItem("grow_sigil", new EnabledSigilItem(
            new Item.Settings(),
            ctx -> {
                if(ctx.world().isClient()) return;
                if (!ctx.world().isDay()) return;

                BlockPos center = ctx.pos();
                World world = ctx.world();
                HmCommonUtils.forEachInCube(center, 5, pos -> {
                    Random random = world.random;
                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();

                    if (block == Blocks.DIRT && random.nextFloat() < 0.1f && world.getBlockState(pos.up()).isAir()) {
                        world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
                    } else if (block instanceof FarmlandBlock && random.nextFloat() < 0.1f) {
                        int moisture = state.get(FarmlandBlock.MOISTURE);
                        if (moisture < 7) {
                            world.setBlockState(pos, state.with(FarmlandBlock.MOISTURE, 7), Block.NOTIFY_LISTENERS);
                        }
                    } else if (block instanceof Fertilizable fertilizable && random.nextFloat() < 0.5f && !(block instanceof GrassBlock)) {
                        if (fertilizable.isFertilizable(world, pos, state, false)) {
                            if (fertilizable.canGrow(world, world.random, pos, state)) {
                                fertilizable.grow((ServerWorld) world, world.random, pos, state);
                            }
                        }
                    }
                });
            }, 150, 40
    ));

    // Tools
    public static final Item SACRIFICIAL_DAGGER = registerItem("sacrificial_dagger", new DaggerItem(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item DESECRATED_PICKAXE = registerItem("desecrated_pickaxe", new DesecratedPickaxeItem(new Item.Settings().rarity(Rarity.EPIC), false));
    public static final Item DESECRATED_AXE = registerItem("desecrated_axe", new DesecratedAxeItem(new Item.Settings().rarity(Rarity.EPIC), false));
    public static final Item DESECRATED_SWORD = registerItem("desecrated_sword", new DesecratedSwordItem(new Item.Settings().rarity(Rarity.EPIC), false));
    public static final Item DESECRATED_SHOVEL = registerItem("desecrated_shovel", new DesecratedShovelItem(new Item.Settings().rarity(Rarity.EPIC), false));

    public static final Item AWAKENED_DESECRATED_PICKAXE = registerItem("awakened_desecrated_pickaxe", new DesecratedPickaxeItem(new Item.Settings().rarity(Rarity.EPIC), true));
    public static final Item AWAKENED_DESECRATED_AXE = registerItem("awakened_desecrated_axe", new DesecratedAxeItem(new Item.Settings().rarity(Rarity.EPIC), true));
    public static final Item AWAKENED_DESECRATED_SWORD = registerItem("awakened_desecrated_sword", new DesecratedSwordItem(new Item.Settings().rarity(Rarity.EPIC), true));
    public static final Item AWAKENED_DESECRATED_SHOVEL = registerItem("awakened_desecrated_shovel", new DesecratedShovelItem(new Item.Settings().rarity(Rarity.EPIC), true));

    public static final RegistryKey<ItemGroup> TAB = registerTab("hemomancy", Text.translatable("tab.hemomancy"), BOOK,
            BOOK, BLOOD_ALTAR, BLANK_RUNE, SPEED_RUNE, SACRIFICES_RUNE, CAPACITY_RUNE, RESONANT_CAPACITY_RUNE, RELATIONS_RUNE, CHIMERIC_RUNE, RITUAL_STAFF, RITUAL_STONE,
            SACRIFICIAL_DAGGER, MAGE_STATUE,
            WEAK_BLOOD_ORB, APPRENTICE_BLOOD_ORB, MAGICIAN_BLOOD_ORB, MASTER_BLOOD_ORB, ARCHMAGE_BLOOD_ORB, TRANSCENDENTAL_BLOOD_ORB, INEXHAUSTIBLE_BLOOD_ORB,
            BLANK_GLYPH, FORTIFIED_GLYPH, CRIMSON_GLYPH, FILLED_GLYPH, DEMONIC_GLYPH, INFERNAL_GLYPH,
            CRIMSON_STEEL_INGOT, CRIMSON_ORNAMENT, TRANSCENDENTAL_CRYSTAL,
            AIR_SIGIL, MAGNETISM_SIGIL, WATER_SIGIL, LAVA_SIGIL, DRAINAGE_SIGIL, RESISTANCE_SIGIL, MOVEMENT_SIGIL, TELEPOSITION_SIGIL, GROW_SIGIL,
            ALCHEMY_TABLE, RUNE_STAIRS, RUNE_SLAB,
            DESECRATED_PICKAXE, DESECRATED_AXE, DESECRATED_SWORD, DESECRATED_SHOVEL,
            AWAKENED_DESECRATED_PICKAXE, AWAKENED_DESECRATED_AXE, AWAKENED_DESECRATED_SWORD, AWAKENED_DESECRATED_SHOVEL
    );

    private static <T extends Item> T registerItem(String name, T item, String requiredMod) {
        if (requiredMod != null && !FabricLoader.getInstance().isModLoaded(requiredMod)) return null;
        var id = Hemomancy.path(name);
        return Registry.register(Registries.ITEM, id, item);
    }

    private static <T extends Item> T registerItem(String name, T item) {
        return registerItem(name, item, null);
    }

    private static RegistryKey<ItemGroup> registerTab(String name, Text title, ItemConvertible icon, ItemConvertible... items) {
        var key = RegistryKey.of(RegistryKeys.ITEM_GROUP, Hemomancy.path(name));

        var group = FabricItemGroup.builder().displayName(title).icon(() -> new ItemStack(icon))
                .entries((ctx, entries) -> {
                    for (var item : items) {
                        if (item != null) entries.add(item);
                    }
                }).build();

        Registry.register(Registries.ITEM_GROUP, key, group);
        return key;
    }

    public static void onInit() {}
}