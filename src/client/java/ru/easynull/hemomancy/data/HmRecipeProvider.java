package ru.easynull.hemomancy.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.HmDataComponents;
import ru.easynull.hemomancy.registry.HmItems;
import ru.easynull.hemomancy.registry.recipes.AlchemyRecipe;
import ru.easynull.hemomancy.registry.recipes.FusionRecipe;

import java.util.concurrent.CompletableFuture;

import static ru.easynull.hemomancy.data.HmItemTagsProvider.ORBS;

public final class HmRecipeProvider extends FabricRecipeProvider {
    public HmRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.ALCHEMY_TABLE)
                .pattern("CGC")
                .pattern("DRD")
                .pattern("SSS")
                .define('C', HmItems.CRIMSON_STEEL_INGOT)
                .define('G', HmItems.FILLED_GLYPH)
                .define('R', HmBlocks.BLANK_RUNE)
                .define('S', HmBlocks.RUNE_SLAB)
                .define('D', Items.RAW_GOLD)
                .unlockedBy(getHasName(HmBlocks.BLANK_RUNE), has(HmBlocks.BLANK_RUNE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.BLANK_RUNE)
                .pattern("SSS")
                .pattern("GOG")
                .pattern("SSS")
                .define('G', HmItems.BLANK_GLYPH)
                .define('O', ORBS)
                .define('S', Items.STONE)
                .unlockedBy(getHasName(HmItems.BLANK_GLYPH), has(HmItems.BLANK_GLYPH))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.BLOOD_ALTAR)
                .pattern("S S")
                .pattern("SFS")
                .pattern("GDG")
                .define('G', Items.GOLD_INGOT)
                .define('F', Items.BUCKET)
                .define('S', Items.STONE)
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmItems.BOOK)
                .pattern("CG ")
                .pattern("D  ")
                .pattern("   ")
                .define('C', Items.REDSTONE)
                .define('G', Items.GOLD_INGOT)
                .define('D', Items.BOOK)
                .unlockedBy(getHasName(Items.BOOK), has(Items.BOOK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.CAPACITY_RUNE)
                .pattern("SAS")
                .pattern("ARA")
                .pattern("SGS")
                .define('G', HmItems.CRIMSON_GLYPH)
                .define('R', ORBS)
                .define('S', Items.STONE)
                .define('A', Items.BUCKET)
                .unlockedBy(getHasName(HmItems.CRIMSON_GLYPH), has(HmItems.CRIMSON_GLYPH))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.CRIMSON_ORNAMENT)
                .pattern("SSS")
                .pattern("SOS")
                .pattern("SSS")
                .define('S', HmItems.CRIMSON_STEEL_INGOT)
                .define('O', ORBS)
                .unlockedBy(getHasName(HmItems.CRIMSON_STEEL_INGOT), has(HmItems.CRIMSON_STEEL_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.MAGE_STATUE)
                .pattern(" R ")
                .pattern("SGB")
                .pattern("LCL")
                .define('B', Items.BONE)
                .define('R', Items.ROTTEN_FLESH)
                .define('S', Items.STRING)
                .define('G', HmItems.FORTIFIED_GLYPH)
                .define('C', HmBlocks.BLANK_RUNE)
                .define('L', HmBlocks.RUNE_SLAB)
                .unlockedBy(getHasName(HmBlocks.BLANK_RUNE), has(HmBlocks.BLANK_RUNE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.RELATIONS_RUNE)
                .pattern("SAS")
                .pattern("ORO")
                .pattern("SGS")
                .define('G', HmItems.CRIMSON_GLYPH)
                .define('R', HmBlocks.BLANK_RUNE)
                .define('O', ORBS)
                .define('S', Items.STONE)
                .define('A', Items.ECHO_SHARD)
                .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.RESONANT_CAPACITY_RUNE)
                .pattern("SBS")
                .pattern("GRG")
                .pattern("SOS")
                .define('G', HmItems.DEMONIC_GLYPH)
                .define('R', HmBlocks.CAPACITY_RUNE)
                .define('O', ORBS)
                .define('S', Items.DEEPSLATE)
                .define('B', Items.BUCKET)
                .unlockedBy(getHasName(HmBlocks.CAPACITY_RUNE), has(HmBlocks.CAPACITY_RUNE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.RUNE_SLAB, 3)
                .pattern("SGS")
                .define('G', HmItems.BLANK_GLYPH)
                .define('S', Items.STONE)
                .unlockedBy(getHasName(HmItems.BLANK_GLYPH), has(HmItems.BLANK_GLYPH))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.RUNE_STAIRS, 2)
                .pattern("S  ")
                .pattern("GS ")
                .pattern("OGS")
                .define('G', HmItems.BLANK_GLYPH)
                .define('O', ORBS)
                .define('S', Items.STONE)
                .unlockedBy(getHasName(HmItems.BLANK_GLYPH), has(HmItems.BLANK_GLYPH))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.SACRIFICES_RUNE)
                .pattern("SGS")
                .pattern("AOA")
                .pattern("SGS")
                .define('G', HmItems.FORTIFIED_GLYPH)
                .define('O', ORBS)
                .define('S', Items.STONE)
                .define('A', Items.GLOWSTONE_DUST)
                .unlockedBy(getHasName(HmItems.FORTIFIED_GLYPH), has(HmItems.FORTIFIED_GLYPH))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmItems.SACRIFICIAL_DAGGER)
                .pattern("  I")
                .pattern("LG ")
                .pattern("SL ")
                .define('S', Items.STICK)
                .define('L', Items.GOLD_NUGGET)
                .define('G', ConventionalItemTags.GLASS_BLOCKS)
                .define('I', ConventionalItemTags.IRON_INGOTS)
                .unlockedBy(getHasName(Items.GOLD_NUGGET), has(Items.GOLD_NUGGET))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HmBlocks.SPEED_RUNE)
                .pattern("SGS")
                .pattern("ARA")
                .pattern("SGS")
                .define('G', HmItems.FORTIFIED_GLYPH)
                .define('R', HmBlocks.BLANK_RUNE)
                .define('S', Items.STONE)
                .define('A', Items.SUGAR)
                .unlockedBy(getHasName(HmItems.FORTIFIED_GLYPH), has(HmItems.FORTIFIED_GLYPH))
                .save(output);

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.APPRENTICE_BLOOD_ORB), Ingredient.of(Items.EMERALD_BLOCK), 5000, 2)
                .save(output, Hemomancy.path("apprentice_orb"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.ARCHMAGE_BLOOD_ORB), Ingredient.of(Items.BARRIER), 50000, 5)
                .save(output, Hemomancy.path("archmage_orb"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.AWAKENED_DESECRATED_AXE), Ingredient.of(HmItems.DESECRATED_AXE), 210000, 6)
                .save(output, Hemomancy.path("aw_desecrated_axe"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.AWAKENED_DESECRATED_PICKAXE), Ingredient.of(HmItems.DESECRATED_PICKAXE), 200000, 6)
                .save(output, Hemomancy.path("aw_desecrated_pickaxe"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.AWAKENED_DESECRATED_SHOVEL), Ingredient.of(HmItems.DESECRATED_SHOVEL), 200000, 6)
                .save(output, Hemomancy.path("aw_desecrated_shovel"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.AWAKENED_DESECRATED_SWORD), Ingredient.of(HmItems.DESECRATED_SWORD), 220000, 6)
                .save(output, Hemomancy.path("aw_desecrated_sword"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.BLANK_GLYPH, 3), Ingredient.of(Items.STONE), 500, 1)
                .save(output, Hemomancy.path("blank_glyph"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.CRIMSON_GLYPH), Ingredient.of(HmItems.FORTIFIED_GLYPH), 2500, 3)
                .save(output, Hemomancy.path("crimson_glyph"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.CRIMSON_STEEL_INGOT), Ingredient.of(Items.IRON_INGOT), 750, 2)
                .save(output, Hemomancy.path("crimson_steel_ingot"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.DEMONIC_GLYPH), Ingredient.of(HmItems.FILLED_GLYPH), 10000, 5)
                .save(output, Hemomancy.path("demonic_glyph"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.DESECRATED_AXE), Ingredient.of(Items.NETHERITE_AXE), 100000, 5)
                .save(output, Hemomancy.path("desecrated_axe"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.DESECRATED_PICKAXE), Ingredient.of(Items.NETHERITE_PICKAXE), 100000, 5)
                .save(output, Hemomancy.path("desecrated_pickaxe"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.DESECRATED_SHOVEL), Ingredient.of(Items.NETHERITE_SHOVEL), 100000, 5)
                .save(output, Hemomancy.path("desecrated_shovel"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.DESECRATED_SWORD), Ingredient.of(Items.NETHERITE_SWORD), 90000, 5)
                .save(output, Hemomancy.path("desecrated_sword"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.FILLED_GLYPH), Ingredient.of(HmItems.CRIMSON_GLYPH), 5000, 4)
                .save(output, Hemomancy.path("filled_glyph"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.FORTIFIED_GLYPH), Ingredient.of(HmItems.BLANK_GLYPH), 1500, 2)
                .save(output, Hemomancy.path("fortified_glyph"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.INFERNAL_GLYPH), Ingredient.of(HmItems.DEMONIC_GLYPH), 20000, 6)
                .save(output, Hemomancy.path("infernal_glyph"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.MAGICIAN_BLOOD_ORB), Ingredient.of(Items.NETHERITE_BLOCK), 15000, 3)
                .save(output, Hemomancy.path("magician_orb"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.MASTER_BLOOD_ORB), Ingredient.of(Items.BARRIER), 25000, 4)
                .save(output, Hemomancy.path("master_orb"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.TRANSCENDENTAL_BLOOD_ORB), Ingredient.of(HmBlocks.TRANSCENDENTAL_CRYSTAL), 100000, 6)
                .save(output, Hemomancy.path("transcendental_orb"));

        FusionRecipe.Builder.fusion(new ItemStack(HmItems.WEAK_BLOOD_ORB), Ingredient.of(Items.DIAMOND), 1000, 1)
                .save(output, Hemomancy.path("weak_orb"));

        ItemStack book = new ItemStack(HmItems.BOOK);
        book.set(HmDataComponents.EXTENDED, true);
        AlchemyRecipe.Builder.alchemy(book, 15000)
                .requires(HmItems.BOOK)
                .requires(HmItems.FORTIFIED_GLYPH)
                .requires(HmItems.CRIMSON_GLYPH)
                .requires(HmItems.FORTIFIED_GLYPH)
                .save(output, Hemomancy.path("advanced_book"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(HmItems.AIR_SIGIL), 16000)
                .requires(Items.FEATHER)
                .requires(Items.FEATHER)
                .requires(Items.FEATHER)
                .requires(Items.GHAST_TEAR)
                .requires(Items.PHANTOM_MEMBRANE)
                .requires(Items.PHANTOM_MEMBRANE)
                .requires(HmItems.FILLED_GLYPH)
                .save(output, Hemomancy.path("air_sigil"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(HmItems.DRAINAGE_SIGIL), 12000)
                .requires(Items.SPONGE)
                .requires(Items.SPONGE)
                .requires(Items.DRIED_KELP_BLOCK)
                .requires(Items.DRIED_KELP_BLOCK)
                .requires(Items.BLAZE_ROD)
                .requires(Items.GHAST_TEAR)
                .requires(HmItems.CRIMSON_GLYPH)
                .save(output, Hemomancy.path("drainage_sigil"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(HmItems.GROW_SIGIL), 15000)
                .requires(Items.BONE_MEAL)
                .requires(Items.BONE_MEAL)
                .requires(Items.BONE_MEAL)
                .requires(Items.WHEAT_SEEDS)
                .requires(Items.WHEAT_SEEDS)
                .requires(Items.SUGAR_CANE)
                .requires(Items.SUGAR_CANE)
                .requires(Items.SUGAR_CANE)
                .requires(Items.PITCHER_PLANT)
                .requires(HmItems.FORTIFIED_GLYPH)
                .save(output, Hemomancy.path("grow_sigil"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(HmItems.LAVA_SIGIL), 8000)
                .requires(Items.LAVA_BUCKET)
                .requires(Items.LAVA_BUCKET)
                .requires(Items.FIRE_CHARGE)
                .requires(Items.FIRE_CHARGE)
                .requires(Items.MAGMA_CREAM)
                .requires(Items.MAGMA_CREAM)
                .requires(Items.MAGMA_CREAM)
                .requires(HmItems.CRIMSON_GLYPH)
                .save(output, Hemomancy.path("lava_sigil"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(HmItems.MAGNETISM_SIGIL), 25000)
                .requires(Items.ENDER_PEARL)
                .requires(Items.ENDER_PEARL)
                .requires(Items.IRON_BLOCK)
                .requires(Items.REDSTONE_BLOCK)
                .requires(HmItems.DEMONIC_GLYPH)
                .requires(Items.SCULK_SENSOR)
                .save(output, Hemomancy.path("magnetism_sigil"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(HmItems.MOVEMENT_SIGIL), 25000)
                .requires(Items.PISTON)
                .requires(Items.STICKY_PISTON)
                .requires(Items.OBSERVER)
                .requires(Items.SLIME_BALL)
                .requires(Items.SLIME_BALL)
                .requires(HmItems.FILLED_GLYPH)
                .save(output, Hemomancy.path("movement_sigil"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(Items.OBSIDIAN, 2), 1000)
                .requires(Items.LAVA_BUCKET)
                .requires(Items.WATER_BUCKET)
                .save(output, Hemomancy.path("obsidian_from_alchemy"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(HmItems.RESISTANCE_SIGIL), 15000)
                .requires(HmItems.CRIMSON_STEEL_INGOT)
                .requires(HmItems.CRIMSON_STEEL_INGOT)
                .requires(Items.BLAZE_POWDER)
                .requires(HmItems.CRIMSON_GLYPH)
                .save(output, Hemomancy.path("resistance_sigil"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(HmItems.TELEPOSITION_SIGIL), 35000)
                .requires(Items.ENDER_PEARL)
                .requires(Items.ENDER_PEARL)
                .requires(Items.ENDER_PEARL)
                .requires(Items.CHORUS_FRUIT)
                .requires(Items.CHORUS_FRUIT)
                .requires(Items.CHORUS_FRUIT)
                .requires(Items.CHORUS_FRUIT)
                .requires(Items.CHORUS_FRUIT)
                .requires(Items.ENDER_EYE)
                .requires(HmItems.INFERNAL_GLYPH)
                .save(output, Hemomancy.path("teleposition_sigil"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(HmBlocks.TRANSCENDENTAL_CRYSTAL), 120000)
                .requires(Items.AMETHYST_BLOCK)
                .requires(Items.AMETHYST_SHARD)
                .requires(Items.AMETHYST_BLOCK)
                .requires(Items.ECHO_SHARD)
                .requires(Items.AMETHYST_SHARD)
                .requires(Items.AMETHYST_BLOCK)
                .requires(Items.AMETHYST_SHARD)
                .save(output, Hemomancy.path("transcendental_crystal"));

        AlchemyRecipe.Builder.alchemy(new ItemStack(HmItems.WATER_SIGIL), 6000)
                .requires(Items.WATER_BUCKET)
                .requires(Items.WATER_BUCKET)
                .requires(Items.KELP)
                .requires(Items.KELP)
                .requires(Items.HEART_OF_THE_SEA)
                .requires(Items.POTION)
                .requires(HmItems.CRIMSON_GLYPH)
                .save(output, Hemomancy.path("water_sigil"));
    }
}
