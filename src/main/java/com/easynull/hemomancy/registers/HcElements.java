package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.registers.blocks.*;
import com.easynull.hemomancy.registers.items.*;
import com.easynull.hemomancy.registers.items.armortools.DesecratedAxe;
import com.easynull.hemomancy.registers.items.armortools.DesecratedPickaxe;
import com.easynull.hemomancy.registers.items.armortools.DesecratedShovel;
import com.easynull.hemomancy.registers.items.armortools.DesecratedSword;
import com.easynull.hemomancy.registers.items.sigil.*;
import com.mw.nullcore.core.NcUtils;
import com.mw.nullcore.core.holders.*;
import com.mw.nullcore.registers.NcComponents;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.*;

import java.util.List;

import static com.easynull.hemomancy.Hemomancy.ID;

public final class HcElements {
    static final OuterItem items = OuterItem.create(ID);
    static final OuterBlock blocks = OuterBlock.create(ID, items);
    static final OuterCreativeTab tabs = OuterCreativeTab.create(ID);

    public static DeferredBlock<Block> bloodAltar, blankRune, speedRune, sacrificesRune, capacityRune, resonantCapacityRune, relationsRune, chimericRune, transcendentalCrystal, crimsonOrnament, alchemyTable, runeStairs, runeSlab;
    public static DeferredItem<Item> sacrificialDagger, weakOrb, apprenticeOrb, magicianOrb, masterOrb, archmageOrb, transcendentalOrb, infinityOrb, crimsonSteelIngot, hemostaticController, waterSigil, lavaSigil, drainageSigil, airSigil, resistanceSigil, magnetismSigil, movementSigil, telepositionSigil, growSigil, blankGlyph, fortifiedGlyph, crimsonGlyph, filledGlyph, demonicGlyph, infernalGlyph, cosmicGlyph, desecratedPickaxe, desecratedAxe, desecratedSword, desecratedShovel, awDesecratedPickaxe, awDesecratedAxe, awDesecratedSword, awDesecratedShovel;

    static {
        bloodAltar = blocks.registerBlock("blood_altar", AltarBlock::new, Blocks.BLACKSTONE);

        blankRune = blocks.registerBlock("blank_rune", p -> new RuneBlock(p, 0, 0, RuneBlock.Type.none), Blocks.STONE);
        speedRune = blocks.registerBlock("speed_rune", p -> new RuneBlock(p, 0.8f, RuneBlock.Type.speed), Blocks.STONE);
        sacrificesRune = blocks.registerBlock("sacrifices_rune", p -> new RuneBlock(p, 6f, RuneBlock.Type.sacrifices), Blocks.STONE);
        capacityRune = blocks.registerBlock("capacity_rune", p -> new RuneBlock(p, 1, RuneBlock.Type.capacity), Blocks.STONE);
        resonantCapacityRune = blocks.registerBlock("resonant_capacity_rune", p -> new RuneBlock(p, 1f, RuneBlock.Type.resonantCapacity), Blocks.STONE);
        relationsRune = blocks.registerBlock("relations_rune", p -> new RuneBlock(p, 1.5f, RuneBlock.Type.relations), Blocks.STONE);

        weakOrb = items.registerItem("weak_blood_orb", p -> new OrbItem(p, 1, 5000, 1));
        apprenticeOrb = items.registerItem("apprentice_blood_orb", p -> new OrbItem(p, 2, 25000, 3));
        magicianOrb = items.registerItem("magician_blood_orb", p -> new OrbItem(p, 3, 150000, 10));
        masterOrb = items.registerItem("master_blood_orb", p -> new OrbItem(p.rarity(Rarity.UNCOMMON), 4, 1000000, 20));
        archmageOrb = items.registerItem("archmage_blood_orb", p -> new OrbItem(p.rarity(Rarity.RARE), 5, 10000000, 45));
        transcendentalOrb = items.registerItem("transcendental_blood_orb", p -> new OrbItem(p.rarity(Rarity.EPIC), 6, 30000000, 100));
        sacrificialDagger = items.registerItem("sacrificial_dagger", p -> new DaggerItem(p.rarity(Rarity.UNCOMMON)));

        blankGlyph = items.registerItem("blank_glyph", Item::new);
        fortifiedGlyph = items.registerItem("fortified_glyph", Item::new);
        crimsonGlyph = items.registerItem("crimson_glyph", Item::new);
        filledGlyph = items.registerItem("filled_glyph", Item::new);
        demonicGlyph = items.registerItem("demonic_glyph", Item::new);
        infernalGlyph = items.registerItem("infernal_glyph", p -> new Item(p.rarity(Rarity.RARE)));

        crimsonOrnament = blocks.registerBlock("crimson_ornament", Block::new, Blocks.COPPER_BLOCK);
        crimsonSteelIngot = items.registerItem("crimson_steel_ingot", Item::new);
        transcendentalCrystal = blocks.registerBlock("transcendental_crystal", Block::new, Blocks.AMETHYST_BLOCK);
        alchemyTable = blocks.registerBlock("alchemy_table", AlchemyTableBlock::new, Blocks.BLACKSTONE);

        hemostaticController = items.registerItem("hemostatic_controller", ControllerItem::new);
        waterSigil = items.registerItem("water_sigil", p -> new SigilItem(p, ctx -> ctx.level().setBlock(ctx.pos().relative(ctx.direction()), Blocks.WATER.defaultBlockState(), 3), 150));
        lavaSigil = items.registerItem("lava_sigil", p -> new SigilItem(p, ctx -> ctx.level().setBlock(ctx.pos().relative(ctx.direction()), Blocks.LAVA.defaultBlockState(), 3), 150));
        drainageSigil = items.registerItem("drainage_sigil", p -> new EnSigilItem(p, ctx -> NcUtils.Block.forEachSphere(ctx.pos(), 4, pos -> {
            if(!NcUtils.Block.isFluid(ctx.level().getBlockState(pos))) return;
            ctx.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }), 50));
        airSigil = items.registerItem("air_sigil", p -> new SigilItem(p, ctx -> {
            ctx.player().getEnderChestInventory().startOpen(ctx.player());
            Player player = ctx.player();
            Vec3 look = player.getLookAngle();
            float strength = 1.8f;
            player.setDeltaMovement(look.x() * strength, look.y() * strength + 0.5, look.z() * strength);
            player.resetFallDistance();
            player.startFallFlying();
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 1.0f, 1.0f);
        }, 45, true));
        magnetismSigil = items.registerItem("magnetism_sigil", p -> new EnSigilItem(p, ctx -> {
            if(ctx.level().isClientSide() || !((EnSigilItem) ctx.item()).isActive(ctx.stack())) return;
            NcUtils.Level.getEntities(ctx.level(), ctx.pos(), 12f).forEach(e -> {
                if(e instanceof Player) return;
                Vec3 playerPos = ctx.player().position();
                Vec3 entityPos = e.position();
                double distance = playerPos.distanceTo(entityPos);
                if (distance <= 1.0) return;
                float strange = (float) (distance / (distance * 1.2f));
                double force = strange / (distance * distance);
                force = Math.min(force, 0.3);
                Vec3 direction = playerPos.subtract(entityPos).normalize();
                e.setDeltaMovement(e.getDeltaMovement().add(direction.scale(force)));
            });
        }, 150, NcUtils.Mth.secondTick(5)));
        resistanceSigil = items.registerItem("resistance_sigil", p -> new EnSigilItem(p, ctx -> {
            if (!((EnSigilItem) ctx.item()).isActive(ctx.stack())) return;
            ctx.player().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 4, false, false, false));
        }, 2500, NcUtils.Mth.secondTick(2)));
        movementSigil = items.registerItem("movement_sigil", p -> new SigilItem(p, ctx -> {
            Level level = ctx.level();
            BlockPos pos = ctx.pos();
            BlockState state = level.getBlockState(pos);
            ItemStack stack = ctx.stack();
            if (state.isAir() || NcUtils.Block.isFluid(state) || HcConfig.unmovementBlocks.get().contains(BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString())) return;
            if (stack.get(NcComponents.BLOCK) == null) {
                BlockEntity be = level.getBlockEntity(pos);
                if (state.hasBlockEntity() && be != null) {
                    stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(be.saveWithFullMetadata(level.registryAccess())));
                    level.removeBlockEntity(pos);
                }
                stack.set(NcComponents.BLOCK, state);
                stack.set(DataComponents.LORE, new ItemLore(List.of(new ItemStack(state.getBlock()).getDisplayName())));
                level.removeBlock(pos, false);
            } else {
                BlockState storedState = stack.get(NcComponents.BLOCK);
                if (storedState == null) return;
                level.setBlock(pos.relative(ctx.direction()), storedState, 3);
                CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
                if (data != null) {
                    BlockEntity be = BlockEntity.loadStatic(pos.relative(ctx.direction()), storedState, data.copyTag(), level.registryAccess());
                    if (be != null) level.setBlockEntity(be);
                }
                stack.remove(NcComponents.BLOCK);
                stack.remove(DataComponents.BLOCK_ENTITY_DATA);
                stack.remove(DataComponents.LORE);
            }
        }, 3000));
        telepositionSigil = items.registerItem("teleposition_sigil", p -> new SigilItem(p, ctx -> {
            BlockPos savePos = ctx.stack().get(NcComponents.POS);
            ResourceKey<Level> saveDim = ctx.stack().get(HcComponents.DIMENSION);
            BlockPos pos = ctx.pos();
            ItemStack stack = ctx.stack();
            Player player = ctx.player();
            if (savePos == null && saveDim == null) {
                ctx.item().shouldLP(false);
                stack.set(NcComponents.POS, pos);
                stack.set(HcComponents.DIMENSION, player.level().dimension());
                stack.set(DataComponents.LORE, new ItemLore(List.of(Component.literal(String.format("X: %d, Y: %d, Z: %d", pos.getX(), pos.getY(), pos.getZ())).withColor(0xFFFFFFFF))));
            } else {
                if(player.isShiftKeyDown()) {
                    ctx.item().shouldLP(false);
                    stack.remove(NcComponents.POS);
                    stack.remove(HcComponents.DIMENSION);
                    stack.remove(DataComponents.LORE);
                    return;
                }
                if (player.getServer() == null || player.getServer().getLevel(saveDim) == null) return;
                player.teleportTo(player.getServer().getLevel(saveDim), savePos.getX() + 0.5f, savePos.getY() + 1f, savePos.getZ() + 0.5f, Relative.DELTA, 1f, 1f, false);
            }
        }, 1500, true));
        growSigil = items.registerItem("grow_sigil", p -> new EnSigilItem(p, ctx -> {
            if (ctx.level().isClientSide() || !((EnSigilItem) ctx.item()).isActive(ctx.stack()) || !ctx.level().isDay()) return;
            NcUtils.Block.forEachCube(ctx.pos(), 5, pos -> {
                BlockState state = ctx.level().getBlockState(pos);
                Block block = state.getBlock();
                Level level = ctx.level();
                if (block == Blocks.DIRT && level.random.nextFloat() < 0.1f) level.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                else if (block instanceof FarmBlock farm && level.random.nextFloat() < 0.1f && farm.defaultBlockState().getValue(BlockStateProperties.MOISTURE) < 7) level.setBlock(pos, Blocks.FARMLAND.defaultBlockState().setValue(BlockStateProperties.MOISTURE, 7), 3);
                else if (block instanceof BonemealableBlock growable && block != Blocks.GRASS_BLOCK && growable.isValidBonemealTarget(level, pos, state) && level.random.nextFloat() < 0.05f && growable.isBonemealSuccess(level, level.random, pos, state)) growable.performBonemeal((ServerLevel) level, level.random, pos, state);
            });
        }, 150, NcUtils.Mth.secondTick(2)));

        runeStairs = blocks.registerBlock("rune_stairs", p -> new StairBlock(blankRune.get().defaultBlockState(), p), Blocks.STONE);
        runeSlab = blocks.registerBlock("rune_slab", SlabBlock::new, Blocks.STONE);

        //TODO: THIS TEST ITEMS YET
        desecratedPickaxe = items.registerItem("desecrated_pickaxe", p -> new DesecratedPickaxe(p.rarity(Rarity.EPIC), false));
        awDesecratedPickaxe = items.registerItem("awakened_desecrated_pickaxe", p -> new DesecratedPickaxe(p.rarity(Rarity.EPIC), true));
        desecratedAxe = items.registerItem("desecrated_axe", p -> new DesecratedAxe(p.rarity(Rarity.EPIC), false));
        awDesecratedAxe = items.registerItem("awakened_desecrated_axe", p -> new DesecratedAxe(p.rarity(Rarity.EPIC), true));
        desecratedSword = items.registerItem("desecrated_sword", p -> new DesecratedSword(p.rarity(Rarity.EPIC), false));
        awDesecratedSword = items.registerItem("awakened_desecrated_sword", p -> new DesecratedSword(p.rarity(Rarity.EPIC), true));
        desecratedShovel = items.registerItem("desecrated_shovel", p -> new DesecratedShovel(p.rarity(Rarity.EPIC), false));
        awDesecratedShovel = items.registerItem("awakened_desecrated_shovel", p -> new DesecratedShovel(p.rarity(Rarity.EPIC), true));

        if (ModList.get().isLoaded("avaritia")) {
            chimericRune = blocks.registerBlock("chimeric_rune", p -> new RuneBlock(p, 0, RuneBlock.Type.speed, RuneBlock.Type.capacity, RuneBlock.Type.resonantCapacity, RuneBlock.Type.sacrifices, RuneBlock.Type.relations), Blocks.STONE, new Item.Properties().rarity(Rarity.EPIC));
            infinityOrb = items.registerItem("infinity_blood_orb", p -> new OrbItem(p.rarity(AvaritiaItems.COSMIC_RARITY), 100, 1_000_000_000_000L, 1));
            cosmicGlyph = items.registerItem("cosmic_glyph", p -> new Item(p.rarity(AvaritiaItems.COSMIC_RARITY)));
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> main = tabs.registerTab("hemomancy", Component.translatable("tab.hemomancy"), alchemyTable::toStack, bloodAltar, blankRune, speedRune, sacrificesRune, capacityRune, resonantCapacityRune, relationsRune, chimericRune,
            sacrificialDagger, weakOrb, apprenticeOrb, magicianOrb, masterOrb, archmageOrb, transcendentalOrb, infinityOrb, hemostaticController, blankGlyph, fortifiedGlyph, crimsonGlyph, filledGlyph, demonicGlyph, infernalGlyph, cosmicGlyph, crimsonOrnament, crimsonSteelIngot, transcendentalCrystal,
            airSigil, magnetismSigil, waterSigil, lavaSigil, resistanceSigil, movementSigil, telepositionSigil, growSigil, alchemyTable, runeStairs, runeSlab, desecratedPickaxe, desecratedAxe, desecratedSword, desecratedShovel, awDesecratedPickaxe, awDesecratedAxe, awDesecratedSword, awDesecratedShovel);

    public static void init(final IEventBus bus) {
        items.register(bus);
        blocks.register(bus);
        tabs.register(bus);
    }
}