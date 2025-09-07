package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.registers.blocks.*;
import com.easynull.hemomancy.registers.items.*;
import com.easynull.hemomancy.registers.items.sigil.*;
import com.mw.nullcore.Utils;
import com.mw.nullcore.core.blocks.EntitibleBlock;
import com.mw.nullcore.core.holders.*;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.*;

import static com.easynull.hemomancy.Hemomancy.ID;

public final class HcElements {
    static final OuterItem items = OuterItem.create(ID);
    static final OuterBlock blocks = OuterBlock.create(ID, items);
    static final OuterCreativeTab tabs = OuterCreativeTab.create(ID);

    public static DeferredBlock<Block> bloodAltar, rune, speedRune, sacrificesRune, capacityRune, resonantCapacityRune, relationsRune, chimericRune, transcendentalCrystal, crimsonOrnament, pedestal, alchemy;
    public static DeferredItem<Item> sacrificialDagger, weakOrb, apprenticeOrb, magicianOrb, masterOrb, archmageOrb, transcendentalOrb, infinityOrb, hemostaticController, waterSigil, lavaSigil, airSigil, resistanceSigil, movementSigil, spaceSigil, blankGlyph, fortifiedGlyph, crimsonGlyph, filledGlyph, demonicGlyph, infernalGlyph, cosmicGlyph;

    static {
        rune = blocks.registerBlock("rune", p -> new RuneBlock(p, -1, RuneBlock.Type.none), Blocks.STONE);
        speedRune = blocks.registerBlock("speed_rune", p -> new RuneBlock(p, RuneBlock.Type.speed), Blocks.STONE);
        sacrificesRune = blocks.registerBlock("sacrifices_rune", p -> new RuneBlock(p, RuneBlock.Type.sacrifices), Blocks.STONE);
        capacityRune = blocks.registerBlock("capacity_rune", p -> new RuneBlock(p, 1, RuneBlock.Type.capacity), Blocks.STONE);
        resonantCapacityRune = blocks.registerBlock("resonant_capacity_rune", p -> new RuneBlock(p, RuneBlock.Type.resonantCapacity), Blocks.STONE);
        relationsRune = blocks.registerBlock("relations_rune", p -> new RuneBlock(p, RuneBlock.Type.relations), Blocks.STONE);
        chimericRune = blocks.registerBlock("chimeric_rune", p -> new RuneBlock(p, RuneBlock.Type.speed, RuneBlock.Type.capacity, RuneBlock.Type.resonantCapacity, RuneBlock.Type.sacrifices, RuneBlock.Type.relations), Blocks.STONE, new Item.Properties().rarity(Rarity.EPIC));

        weakOrb = items.registerItem("weak_blood_orb", p -> new OrbItem(p, 1, 5000));
        apprenticeOrb = items.registerItem("apprentice_blood_orb", p -> new OrbItem(p, 2, 25000));
        magicianOrb = items.registerItem("magician_blood_orb", p -> new OrbItem(p, 3, 150000));
        masterOrb = items.registerItem("master_blood_orb", p -> new OrbItem(p.rarity(Rarity.UNCOMMON), 4, 1000000));
        archmageOrb = items.registerItem("archmage_blood_orb", p -> new OrbItem(p.rarity(Rarity.RARE), 5, 10000000));
        transcendentalOrb = items.registerItem("transcendental_blood_orb", p -> new OrbItem(p.rarity(Rarity.EPIC), 6, 30000000));
        sacrificialDagger = items.registerItem("sacrificial_dagger", p -> new DaggerItem(p.rarity(Rarity.UNCOMMON)));

        hemostaticController = items.registerItem("hemostatic_controller", ControllerItem::new);
        waterSigil = items.registerItem("water_sigil", p -> new SigilItem(p, ctx -> ctx.level().setBlock(ctx.pos().above(), Blocks.WATER.defaultBlockState(), 3), 135));
        lavaSigil = items.registerItem("lava_sigil", p -> new SigilItem(p, ctx -> ctx.level().setBlock(ctx.pos().above(), Blocks.LAVA.defaultBlockState(), 3), 135));
        airSigil = items.registerItem("air_sigil", p -> new SigilItem(p, ctx -> {
            Player player = ctx.player();
            Vec3 look = player.getLookAngle();
            float strength = 1.8f;
            player.setDeltaMovement(look.x() * strength, look.y() * strength + 0.5, look.z() * strength);
            player.resetFallDistance();
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 1.0f, 1.0f);
        }, 65, true));
        resistanceSigil = items.registerItem("resistance_sigil", p -> new TickSigilItem(p, ctx -> {
            if (((TickSigilItem) ctx.item()).isActive(ctx.stack())) {
                ctx.player().addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE,
                        2,
                        4,
                        false,
                        false,
                        false
                ));
            }
        }, 350));
        movementSigil = items.registerItem("movement_sigil", p -> new SigilItem(p, ctx -> {
            Level level = ctx.level();
            BlockPos pos = ctx.pos();
            BlockState state = level.getBlockState(pos);
            ItemStack stack = ctx.stack();
            if (state.isAir() || Utils.Block.isFluid(state) || HcConfig.unmovementBlocks.get().contains(BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString())) return;
            if (stack.get(HcComponents.blockState) == null) {
                BlockEntity be = level.getBlockEntity(pos);
                if (state.hasBlockEntity()) {
                    if (be != null) {
                        stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(be.saveWithFullMetadata(level.registryAccess())));
                        level.removeBlockEntity(pos);
                    }
                }
                stack.set(HcComponents.blockState, state);
                level.removeBlock(pos, false);
            } else {
                BlockState storedState = stack.get(HcComponents.blockState);
                if (storedState != null) {
                    level.setBlock(pos, storedState, 3);
                    if (stack.get(DataComponents.BLOCK_ENTITY_DATA) != null) {
                        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
                        if (data != null) {
                            BlockEntity be = BlockEntity.loadStatic(pos, storedState, data.copyTag(), level.registryAccess());
                            if (be != null) {
                                level.setBlockEntity(be);
                            }
                        }
                    }
                    stack.remove(HcComponents.blockState);
                    stack.remove(DataComponents.BLOCK_ENTITY_DATA);
                }
            }
        }, 15000));
        spaceSigil = items.registerItem("space_sigil", p -> new SigilItem(p, ctx -> {
            BlockPos savePos = ctx.stack().get(HcComponents.pos);
            BlockPos pos = ctx.pos();
            ItemStack stack = ctx.stack();
            Player player = ctx.player();
            if (savePos == null) {
                ctx.item().shouldLP(false);
                stack.set(HcComponents.pos, pos);
            } else {
                if(player.isShiftKeyDown()) {
                    stack.remove(HcComponents.pos);
                    return;
                }
                player.teleportTo(savePos.getX() + 0.5f, savePos.getY() + 1f, savePos.getZ() + 0.5f);
            }
        }, 2000, true));

        transcendentalCrystal = blocks.registerBlock("transcendental_crystal", Block::new, Blocks.AMETHYST_BLOCK);
        crimsonOrnament = blocks.registerBlock("crimson_ornament", Block::new, Blocks.NETHERRACK);
        bloodAltar = blocks.registerBlock("blood_altar", p -> new PedestalBlock(p, HcBlockEntities.bloodAltar::get), Blocks.BLACKSTONE);
        pedestal = blocks.registerBlock("pedestal", p -> new PedestalBlock(p, HcBlockEntities.pedestal::get), Blocks.BLACKSTONE);

        blankGlyph = items.registerItem("blank_glyph", Item::new);
        fortifiedGlyph = items.registerItem("fortified_glyph", Item::new);
        crimsonGlyph = items.registerItem("crimson_glyph", Item::new);
        filledGlyph = items.registerItem("filled_glyph", Item::new);
        demonicGlyph = items.registerItem("demonic_glyph", Item::new);
        infernalGlyph = items.registerItem("infernal_glyph", p -> new Item(p.rarity(Rarity.RARE)));

        alchemy = blocks.registerBlock("alchemy", AlchemyBlock::new, Blocks.BLACKSTONE);

        if (ModList.get().isLoaded("avaritia")) {
            infinityOrb = items.registerItem("infinity_blood_orb", p -> new OrbItem(p.rarity(AvaritiaItems.COSMIC_RARITY), Byte.MAX_VALUE, 1_000_000_000_000L));
            cosmicGlyph = items.registerItem("cosmic_glyph", p -> new Item(p.rarity(AvaritiaItems.COSMIC_RARITY)));
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> main = tabs.registerSearchTab("hemomancy", Component.translatable("tab.hemomancy"), bloodAltar::toStack, rune, speedRune, sacrificesRune, capacityRune, resonantCapacityRune, relationsRune, chimericRune,
            sacrificialDagger, weakOrb, apprenticeOrb, magicianOrb, masterOrb, archmageOrb, transcendentalOrb, infinityOrb, hemostaticController, airSigil, waterSigil, lavaSigil, resistanceSigil, movementSigil, spaceSigil, crimsonOrnament, transcendentalCrystal, bloodAltar, pedestal, blankGlyph, fortifiedGlyph, crimsonGlyph, filledGlyph,
            demonicGlyph, infernalGlyph, cosmicGlyph);

    public static void init(final IEventBus bus) {
        items.register(bus);
        blocks.register(bus);
        tabs.register(bus);
    }
}
