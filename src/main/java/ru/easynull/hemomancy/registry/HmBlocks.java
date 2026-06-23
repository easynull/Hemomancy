package ru.easynull.hemomancy.registry;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.blocks.*;

public final class HmBlocks {

    public static final Block BLOOD_ALTAR = registerBlock("blood_altar", new BloodAltarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE)));
    public static final Block ALCHEMY_TABLE = registerBlock("alchemy_table", new AlchemyTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE)));

    public static final Block BLANK_RUNE = registerBlock("blank_rune", new RuneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE), 0, RuneBlock.Type.NONE));
    public static final Block SPEED_RUNE = registerBlock("speed_rune", new RuneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE), 0.8f, RuneBlock.Type.SPEED));
    public static final Block SACRIFICES_RUNE = registerBlock("sacrifices_rune", new RuneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE), 2.5f, RuneBlock.Type.SACRIFICES));
    public static final Block CAPACITY_RUNE = registerBlock("capacity_rune", new RuneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE), 1500, RuneBlock.Type.CAPACITY));
    public static final Block RESONANT_CAPACITY_RUNE = registerBlock("resonant_capacity_rune", new RuneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE), 1f, RuneBlock.Type.RESONANT_CAPACITY));
    public static final Block RELATIONS_RUNE = registerBlock("relations_rune", new RuneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE), 1.5f, RuneBlock.Type.RELATIONS));
    public static final Block CHIMERIC_RUNE = null;//registerBlock("chimeric_rune", new RuneBlock(AbstractBlock.Settings.ofFullCopy(Blocks.STONE), 4.5f, RuneBlock.Type.values()), new Item.Settings().rarity(Rarity.EPIC), "avaritia");

    public static final Block RITUAL_STONE = registerBlock("ritual_stone", new RitualStoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

    public static final Block CRIMSON_ORNAMENT = registerBlock("crimson_ornament", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)));
    public static final Block TRANSCENDENTAL_CRYSTAL = registerBlock("transcendental_crystal", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));

    public static final Block RUNE_STAIRS = registerBlock("rune_stairs", new StairBlock(BLANK_RUNE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final Block RUNE_SLAB = registerBlock("rune_slab", new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

    public static final Block MAGE_STATUE = registerBlock("mage_statue", new MageStatueBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final Block BLOOD = registerOnlyBlock("blood", new LiquidBlock(HmFluids.BLOOD, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    private static <T extends Block> T registerBlock(String name, T block, Item.Properties itemSettings, String requiredMod) {
        if (requiredMod != null && !FabricLoader.getInstance().isModLoaded(requiredMod)) return null;
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Hemomancy.path(name));
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);

        if(itemSettings != null) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Hemomancy.path(name));
            Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, itemSettings));
        }

        return block;
    }

    private static <T extends Block> T registerBlock(String name, T block, String requiredMod) {
        return registerBlock(name, block, new Item.Properties(), requiredMod);
    }

    private static <T extends Block> T registerOnlyBlock(String name, T block, String requiredMod) {
        return registerBlock(name, block, null, requiredMod);
    }

    private static <T extends Block> T registerBlock(String name, T block) {
        return registerBlock(name, block, null);
    }

    private static <T extends Block> T registerOnlyBlock(String name, T block) {
        return registerOnlyBlock(name, block, null);
    }

    public static void onInit() {}
}