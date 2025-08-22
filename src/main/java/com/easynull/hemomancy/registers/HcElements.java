package com.easynull.hemomancy.registers;

import com.easynull.hemomancy.registers.blocks.*;
import com.easynull.hemomancy.registers.blocks.type.AltarBE;
import com.easynull.hemomancy.registers.items.*;
import com.easynull.hemomancy.registers.items.SigilItem;
import com.mw.nullcore.core.holders.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.easynull.hemomancy.Hemomancy.ID;

public final class HcElements {
    static final OuterItem items = OuterItem.create(ID);
    static final OuterBlock blocks = OuterBlock.create(ID, items);
    static final DeferredRegister<CreativeModeTab> tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ID);

    public static DeferredBlock<Block> bloodAltar, rune, speedRune, sacrificesRune, capacityRune, resonantCapacityRune, relationsRune, chimericRune, transcendentalCrystal, bloodOrnament, test;
    public static DeferredItem<Item> sacrificialDagger, weakOrb, apprenticeOrb, magicianOrb, masterOrb, archmageOrb, transcendentalOrb, infinityOrb, statisticalSigil, waterSigil, lavaSigil;

    static {
        bloodAltar = blocks.registerBlock("blood_altar", AltarBlock::new, Blocks.BLACKSTONE);
        rune = blocks.registerBlock("rune", p -> new RuneBlock(p, -1, RuneBlock.Type.none), Blocks.STONE);
        speedRune = blocks.registerBlock("speed_rune", p -> new RuneBlock(p, RuneBlock.Type.speed), Blocks.STONE);
        sacrificesRune = blocks.registerBlock("sacrifices_rune", p -> new RuneBlock(p, RuneBlock.Type.sacrifices), Blocks.STONE);
        capacityRune = blocks.registerBlock("capacity_rune", p -> new RuneBlock(p, 1, RuneBlock.Type.capacity), Blocks.STONE);
        resonantCapacityRune = blocks.registerBlock("resonant_capacity_rune", p -> new RuneBlock(p, RuneBlock.Type.resonantCapacity), Blocks.STONE);
        relationsRune = blocks.registerBlock("relations_rune", p -> new RuneBlock(p, RuneBlock.Type.relations), Blocks.STONE);
        chimericRune = blocks.registerBlock("chimeric_rune", p -> new RuneBlock(p, RuneBlock.Type.speed, RuneBlock.Type.capacity, RuneBlock.Type.resonantCapacity, RuneBlock.Type.sacrifices, RuneBlock.Type.relations), Blocks.STONE);
        test = blocks.registerBlock("test_chimeric_rune", p -> new RuneBlock(p, 8, RuneBlock.Type.speed, RuneBlock.Type.capacity, RuneBlock.Type.resonantCapacity, RuneBlock.Type.sacrifices, RuneBlock.Type.relations), Blocks.STONE);

        weakOrb = items.registerItem("weak_blood_orb", p -> new OrbItem(p, 1, 5000));
        apprenticeOrb = items.registerItem("apprentice_blood_orb", p -> new OrbItem(p, 2, 25000));
        magicianOrb = items.registerItem("magician_blood_orb", p -> new OrbItem(p, 3, 150000));
        masterOrb = items.registerItem("master_blood_orb", p -> new OrbItem(p, 4, 1000000));
        archmageOrb = items.registerItem("archmage_blood_orb", p -> new OrbItem(p, 5, 10000000));
        transcendentalOrb = items.registerItem("transcendental_blood_orb", p -> new OrbItem(p, 6, 30000000));
        sacrificialDagger = items.registerItem("sacrificial_dagger", DaggerItem::new);

        statisticalSigil = items.registerItem("statistical_sigil", p -> new SigilItem(p, ctx -> {
            if (ctx.level().getBlockEntity(ctx.pos()) instanceof AltarBE altar) {
                ctx.player().displayClientMessage(Component.translatable("message.hemomancy.statistical", altar.getLp(altar), altar.getMaxLp()).withStyle(ChatFormatting.DARK_RED), true);
            } else {
                ctx.player().inventoryMenu.getItems().forEach(stack -> {
                    if (stack.getItem() instanceof OrbItem orb) {
                        ctx.player().displayClientMessage(Component.translatable("message.hemomancy.statistical", orb.getLp(stack), orb.getMaxLp()).withStyle(ChatFormatting.DARK_RED), true);
                    }
                });
            }
        }, 0));
        waterSigil = items.registerItem("water_sigil", p -> new SigilItem(p, ctx -> ctx.level().setBlock(ctx.pos().above(), Blocks.WATER.defaultBlockState(), 1), 450));
        lavaSigil = items.registerItem("lava_sigil", p -> new SigilItem(p, ctx -> ctx.level().setBlock(ctx.pos().above(), Blocks.LAVA.defaultBlockState(), 1), 450));

        transcendentalCrystal = blocks.registerBlock("transcendental_crystal", Block::new, Blocks.AMETHYST_BLOCK);
        bloodOrnament = blocks.registerBlock("blood_ornament", Block::new, Blocks.NETHERRACK);

        if (ModList.get().isLoaded("avaritia")) {
            infinityOrb = items.registerItem("infinity_blood_orb", p -> new OrbItem(p, Byte.MAX_VALUE, Long.MAX_VALUE));
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> main = tabs.register("hemomancy", RegisterUtils.createSearchTab(Component.translatable("tab.hemomancy"), bloodAltar::toStack, bloodAltar, rune, speedRune, sacrificesRune, capacityRune, resonantCapacityRune, relationsRune, chimericRune,
            sacrificialDagger, weakOrb, apprenticeOrb, magicianOrb, masterOrb, archmageOrb, transcendentalOrb, infinityOrb, statisticalSigil, waterSigil, lavaSigil, bloodOrnament, transcendentalCrystal));

    public static void init(final IEventBus bus) {
        items.register(bus);
        blocks.register(bus);
        tabs.register(bus);
    }
}
