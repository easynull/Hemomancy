package ru.easynull.hemomancy.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
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
import ru.easynull.hemomancy.registry.items.ControllerItem;
import ru.easynull.hemomancy.registry.items.DaggerItem;
import ru.easynull.hemomancy.registry.items.OrbItem;
import ru.easynull.hemomancy.registry.items.sigil.TickableSigilItem;
import ru.easynull.hemomancy.registry.items.sigil.SigilItem;
import ru.easynull.hemomancy.registry.items.tools.DesecratedAxeItem;
import ru.easynull.hemomancy.registry.items.tools.DesecratedPickaxeItem;
import ru.easynull.hemomancy.registry.items.tools.DesecratedShovelItem;
import ru.easynull.hemomancy.registry.items.tools.DesecratedSwordItem;
import ru.easynull.hemomancy.utils.HmUtils;

import java.util.List;

import static ru.easynull.hemomancy.registry.HmBlocks.*;

public final class HmItems {
    // Orbs
    public static final Item WEAK_BLOOD_ORB = register("weak_blood_orb", new OrbItem(1, 5000, 1));
    public static final Item APPRENTICE_BLOOD_ORB = register("apprentice_blood_orb", new OrbItem(2, 25000, 3));
    public static final Item MAGICIAN_BLOOD_ORB = register("magician_blood_orb", new OrbItem(3, 150000, 10));
    public static final Item MASTER_BLOOD_ORB = register("master_blood_orb", new OrbItem(new Item.Settings().rarity(Rarity.UNCOMMON), 4, 1000000, 20));
    public static final Item ARCHMAGE_BLOOD_ORB = register("archmage_blood_orb", new OrbItem(new Item.Settings().rarity(Rarity.RARE), 5, 10000000, 45));
    public static final Item TRANSCENDENTAL_BLOOD_ORB = register("transcendental_blood_orb", new OrbItem(new Item.Settings().rarity(Rarity.EPIC), 6, 30000000, 100));
    public static final Item INFINITY_BLOOD_ORB = register("infinity_blood_orb", new OrbItem(new Item.Settings().rarity(Rarity.EPIC), 6, 30000000, 100));

    // Glyphs
    public static final Item BLANK_GLYPH = register("blank_glyph", new Item(new Item.Settings()));
    public static final Item FORTIFIED_GLYPH = register("fortified_glyph", new Item(new Item.Settings()));
    public static final Item CRIMSON_GLYPH = register("crimson_glyph", new Item(new Item.Settings()));
    public static final Item FILLED_GLYPH = register("filled_glyph", new Item(new Item.Settings()));
    public static final Item DEMONIC_GLYPH = register("demonic_glyph", new Item(new Item.Settings()));
    public static final Item INFERNAL_GLYPH = register("infernal_glyph", new Item(new Item.Settings().rarity(Rarity.RARE)));

    public static final Item CRIMSON_STEEL_INGOT = register("crimson_steel_ingot", new Item(new Item.Settings()));

    public static final Item HEMOSTATIC_CONTROLLER = register("hemostatic_controller", new ControllerItem(new Item.Settings()));

    // Sigils
    public static final Item WATER_SIGIL = register("water_sigil", new SigilItem(
            new Item.Settings(),
            ctx -> {
                BlockPos target = ctx.pos().offset(ctx.side());
                ctx.world().setBlockState(target, Blocks.WATER.getDefaultState(), Block.NOTIFY_LISTENERS);
            },
            150
    ));

    public static final Item LAVA_SIGIL = register("lava_sigil", new SigilItem(
            new Item.Settings(),
            ctx -> {
                BlockPos target = ctx.pos().offset(ctx.side());
                ctx.world().setBlockState(target, Blocks.LAVA.getDefaultState(), Block.NOTIFY_LISTENERS);
            },
            150
    ));

    public static final Item DRAINAGE_SIGIL = register("drainage_sigil", new TickableSigilItem(
            new Item.Settings(),
            ctx -> {
                if (ctx.world().isClient) return;
                if (!ctx.item().isActive(ctx.stack())) return;

                HmUtils.forEachInCube(ctx.pos(), 4, p -> {
                    BlockState state = ctx.world().getBlockState(p);
                    if (state.getFluidState().isStill() || state.getFluidState().getLevel() > 0) {
                        ctx.world().setBlockState(p, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }
                });
            }, 50, 20
    ));

    public static final Item AIR_SIGIL = register("air_sigil", new SigilItem(
            new Item.Settings(),
            ctx -> {
                PlayerEntity player = ctx.player();
                if (player == null) return;

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

    public static final Item MAGNETISM_SIGIL = register("magnetism_sigil", new TickableSigilItem(
            new Item.Settings(),
            ctx -> {
                if (ctx.world().isClient) return;
                if (!ctx.item().isActive(ctx.stack())) return;

                Vec3d playerPos = ctx.player().getPos();
                List<Entity> entities = ctx.world().getOtherEntities(ctx.player(), ctx.player().getBoundingBox().expand(12));

                for (Entity e : entities) {
                    if (e instanceof PlayerEntity) continue;
                    Vec3d entityPos = e.getPos();
                    double dist = playerPos.distanceTo(entityPos);
                    if (dist <= 1.0) continue;

                    float strange = (float) (dist / (dist * 1.2));
                    double force = Math.min(strange / (dist * dist), 0.3);
                    Vec3d dir = playerPos.subtract(entityPos).normalize();
                    e.setVelocity(e.getVelocity().add(dir.multiply(force)));
                }
            }, 150, 100
    ));

    public static final Item RESISTANCE_SIGIL = register("resistance_sigil", new TickableSigilItem(
            new Item.Settings(),
            ctx -> {
                if (!ctx.item().isActive(ctx.stack())) return;
                ctx.player().addStatusEffect(new StatusEffectInstance(
                        StatusEffects.RESISTANCE, 2, 4, false, false, false));
            }, 2500, 40
    ));

    public static final Item MOVEMENT_SIGIL = register("movement_sigil", new SigilItem(
            new Item.Settings(),
            ctx -> {
                World world = ctx.world();
                BlockPos pos = ctx.pos();
                BlockState state = world.getBlockState(pos);
                ItemStack stack = ctx.stack();

                if (state.isAir() || state.getFluidState().isStill()) {
                    return;
                }

                NbtCompound nbt = stack.getOrCreateNbt();

                if (!nbt.contains("StoredBlockState")) {
                    BlockEntity be = world.getBlockEntity(pos);
                    if (be != null) {
                        NbtCompound tag = be.createNbtWithIdentifyingData();
                        nbt.put("StoredBlockEntity", tag);
                        world.removeBlockEntity(pos);
                    }
                    nbt.put("StoredBlockState", NbtHelper.fromBlockState(state));
                    nbt.put("StoredLore", HmUtils.fromList(List.of(state.getBlock().getName())));
                    world.removeBlock(pos, false);
                } else {
                    NbtCompound stateNbt = nbt.getCompound("StoredBlockState");
                    BlockState stored = NbtHelper.toBlockState(Registries.BLOCK.getReadOnlyWrapper(), stateNbt);
                    if (stored == null) return;

                    BlockPos placePos = pos.offset(ctx.side());
                    world.setBlockState(placePos, stored, Block.NOTIFY_LISTENERS);

                    if (nbt.contains("StoredBlockEntity")) {
                        NbtCompound tag = nbt.getCompound("StoredBlockEntity");
                        BlockEntity be = BlockEntity.createFromNbt(placePos, stored, tag);
                        if (be != null) world.addBlockEntity(be);
                    }

                    nbt.remove("StoredBlockState");
                    nbt.remove("StoredBlockEntity");
                    nbt.remove("StoredLore");
                }
            }, 3000
    ));

    public static final Item TELEPOSITION_SIGIL = register("teleposition_sigil", new SigilItem(
            new Item.Settings(),
            ctx -> {
                ItemStack stack = ctx.stack();
                NbtCompound nbt = stack.getOrCreateNbt();

                if (!nbt.contains("SavedPosX")) {
                    BlockPos pos = ctx.pos();
                    nbt.putInt("SavedPosX", pos.getX());
                    nbt.putInt("SavedPosY", pos.getY());
                    nbt.putInt("SavedPosZ", pos.getZ());

                    nbt.putString("SavedLore", String.format("X: %d, Y: %d, Z: %d", pos.getX(), pos.getY(), pos.getZ()));
                } else {
                    PlayerEntity player = ctx.player();
                    if (player == null) return;

                    if (player.isSneaking()) {
                        nbt.remove("SavedPosX");
                        nbt.remove("SavedPosY");
                        nbt.remove("SavedPosZ");
                        nbt.remove("SavedLore");
                        return;
                    }

                    int x = nbt.getInt("SavedPosX");
                    int y = nbt.getInt("SavedPosY");
                    int z = nbt.getInt("SavedPosZ");

                    player.teleport(x + 0.5, y + 1, z + 0.5);
                }
            }, 1500, true
    ));

    public static final Item GROW_SIGIL = register("grow_sigil", new TickableSigilItem(
            new Item.Settings(),
            ctx -> {
                if (ctx.world().isClient) return;
                if (!ctx.item().isActive(ctx.stack())) return;
                if (!ctx.world().isDay()) return;

                BlockPos center = ctx.pos();
                World world = ctx.world();
                Random random = world.random;

                for (int x = -5; x <= 5; x++) {
                    for (int y = -5; y <= 5; y++) {
                        for (int z = -5; z <= 5; z++) {
                            if (x * x + y * y + z * z > 25) continue;
                            BlockPos pos = center.add(x, y, z);
                            BlockState state = world.getBlockState(pos);
                            Block block = state.getBlock();

                            if (block == Blocks.DIRT && random.nextFloat() < 0.1f) {
                                world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
                            } else if (block instanceof FarmlandBlock && random.nextFloat() < 0.1f) {
                                int moisture = state.get(FarmlandBlock.MOISTURE);
                                if (moisture < 7) {
                                    world.setBlockState(pos, state.with(FarmlandBlock.MOISTURE, 7), Block.NOTIFY_LISTENERS);
                                }
                            } else if (block instanceof Fertilizable fertilizable &&
                                    fertilizable.isFertilizable(world, pos, state, false) &&
                                    random.nextFloat() < 0.05f) {
                                fertilizable.grow((ServerWorld) world, random, pos, state);
                            }
                        }
                    }
                }
            }, 150, 40
    ));

    // Tools
    public static final Item SACRIFICIAL_DAGGER = register("sacrificial_dagger", new DaggerItem(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item DESECRATED_PICKAXE = register("desecrated_pickaxe", new DesecratedPickaxeItem(new Item.Settings().rarity(Rarity.EPIC), false));
    public static final Item DESECRATED_AXE = register("desecrated_axe", new DesecratedAxeItem(new Item.Settings().rarity(Rarity.EPIC), false));
    public static final Item DESECRATED_SWORD = register("desecrated_sword", new DesecratedSwordItem(new Item.Settings().rarity(Rarity.EPIC), false));
    public static final Item DESECRATED_SHOVEL = register("desecrated_shovel", new DesecratedShovelItem(new Item.Settings().rarity(Rarity.EPIC), false));

    public static final Item AWAKENED_DESECRATED_PICKAXE = register("awakened_desecrated_pickaxe", new DesecratedPickaxeItem(new Item.Settings().rarity(Rarity.EPIC), true));
    public static final Item AWAKENED_DESECRATED_AXE = register("awakened_desecrated_axe", new DesecratedAxeItem(new Item.Settings().rarity(Rarity.EPIC), true));
    public static final Item AWAKENED_DESECRATED_SWORD = register("awakened_desecrated_sword", new DesecratedSwordItem(new Item.Settings().rarity(Rarity.EPIC), true));
    public static final Item AWAKENED_DESECRATED_SHOVEL = register("awakened_desecrated_shovel", new DesecratedShovelItem(new Item.Settings().rarity(Rarity.EPIC), true));

    public static final RegistryKey<ItemGroup> TAB = registerTab("hemomancy", Text.translatable("tab.hemomancy"),
            ALCHEMY_TABLE,
            BLOOD_ALTAR, BLANK_RUNE, SPEED_RUNE, SACRIFICES_RUNE, CAPACITY_RUNE, RESONANT_CAPACITY_RUNE, RELATIONS_RUNE,
            SACRIFICIAL_DAGGER,
            WEAK_BLOOD_ORB, APPRENTICE_BLOOD_ORB, MAGICIAN_BLOOD_ORB, MASTER_BLOOD_ORB, ARCHMAGE_BLOOD_ORB, TRANSCENDENTAL_BLOOD_ORB,
            HEMOSTATIC_CONTROLLER,
            BLANK_GLYPH, FORTIFIED_GLYPH, CRIMSON_GLYPH, FILLED_GLYPH, DEMONIC_GLYPH, INFERNAL_GLYPH,
            CRIMSON_STEEL_INGOT,
            AIR_SIGIL, MAGNETISM_SIGIL, WATER_SIGIL, LAVA_SIGIL, DRAINAGE_SIGIL, RESISTANCE_SIGIL, MOVEMENT_SIGIL, TELEPOSITION_SIGIL, GROW_SIGIL,
            ALCHEMY_TABLE, RUNE_STAIRS, RUNE_SLAB,
            DESECRATED_PICKAXE, DESECRATED_AXE, DESECRATED_SWORD, DESECRATED_SHOVEL,
            AWAKENED_DESECRATED_PICKAXE, AWAKENED_DESECRATED_AXE, AWAKENED_DESECRATED_SWORD, AWAKENED_DESECRATED_SHOVEL
    );

    private static <T extends Item> T register(String name, T item) {
        var id = Hemomancy.path(name);
        return Registry.register(Registries.ITEM, id, item);
    }

    private static RegistryKey<ItemGroup> registerTab(String name, Text title, ItemConvertible icon, ItemConvertible... items) {
        var key = RegistryKey.of(RegistryKeys.ITEM_GROUP, Hemomancy.path(name));

        var group = FabricItemGroup.builder()
                .displayName(title)
                .icon(() -> new ItemStack(icon))
                .entries((ctx, entries) -> {
                    for (var item : items) {
                        if (item != null) entries.add(item);
                    }
                })
                .build();

        Registry.register(Registries.ITEM_GROUP, key, group);
        return key;
    }

    public static void onInit() {}
}