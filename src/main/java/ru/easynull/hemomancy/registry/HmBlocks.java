package ru.easynull.hemomancy.registry;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.blocks.*;

public final class HmBlocks {

    public static final Block BLOOD_ALTAR = registerBlock("blood_altar", new BloodAltarBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE)));
    public static final Block ALCHEMY_TABLE = registerBlock("alchemy_table", new AlchemyTableBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE)));

    public static final Block BLANK_RUNE = registerBlock("blank_rune", new RuneBlock(AbstractBlock.Settings.copy(Blocks.STONE), 0, RuneBlock.Type.NONE));
    public static final Block SPEED_RUNE = registerBlock("speed_rune", new RuneBlock(AbstractBlock.Settings.copy(Blocks.STONE), 0.8f, RuneBlock.Type.SPEED));
    public static final Block SACRIFICES_RUNE = registerBlock("sacrifices_rune", new RuneBlock(AbstractBlock.Settings.copy(Blocks.STONE), 2.5f, RuneBlock.Type.SACRIFICES));
    public static final Block CAPACITY_RUNE = registerBlock("capacity_rune", new RuneBlock(AbstractBlock.Settings.copy(Blocks.STONE), 1500, RuneBlock.Type.CAPACITY));
    public static final Block RESONANT_CAPACITY_RUNE = registerBlock("resonant_capacity_rune", new RuneBlock(AbstractBlock.Settings.copy(Blocks.STONE), 1f, RuneBlock.Type.RESONANT_CAPACITY));
    public static final Block RELATIONS_RUNE = registerBlock("relations_rune", new RuneBlock(AbstractBlock.Settings.copy(Blocks.STONE), 1.5f, RuneBlock.Type.RELATIONS));
    public static Block CHIMERIC_RUNE;

    public static final Block CRIMSON_ORNAMENT = registerBlock("crimson_ornament", new Block(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));
    public static final Block TRANSCENDENTAL_CRYSTAL = registerBlock("transcendental_crystal", new Block(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK)));

    public static final Block RUNE_STAIRS = registerBlock("rune_stairs", new StairsBlock(BLANK_RUNE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block RUNE_SLAB = registerBlock("rune_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.STONE)));

    public static final Block MAGE_STATUE = registerBlock("mage_statue", new MageStatueBlock(AbstractBlock.Settings.copy(Blocks.STONE)));

    private static <T extends Block> T registerBlock(String name, T block, Item.Settings itemSettings) {
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Hemomancy.path(name));
        Registry.register(Registries.BLOCK, blockKey, block);

        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Hemomancy.path(name));
        Registry.register(Registries.ITEM, itemKey, new BlockItem(block, itemSettings));

        return block;
    }

    private static <T extends Block> T registerBlock(String name, T block) {
        return registerBlock(name, block, new Item.Settings());
    }

    public static void onInit() {
        if (FabricLoader.getInstance().isModLoaded("avaritia")) {
            CHIMERIC_RUNE = registerBlock("chimeric_rune", new RuneBlock(AbstractBlock.Settings.copy(Blocks.STONE), 4.5f, RuneBlock.Type.values()), new Item.Settings().rarity(Rarity.EPIC));
        }
    }
}