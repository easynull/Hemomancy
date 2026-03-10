package ru.easynull.hemomancy.api.mage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.altar.Tier;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.HmItems;
import ru.easynull.hemomancy.render.gui.book.Page;
import ru.easynull.hemomancy.render.gui.book.element.*;

import java.util.*;

public final class ResearchManager {
    private static final Map<Identifier, Research> RESEARCHES = new HashMap<>();

    public static void onInit() {
        add(Hemomancy.path("hemomancy_origins"), HmItems.BOOK.asItem().getDefaultStack(), 0, 0, 0,
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.hemomancy_origins.title")),
                        new TextElement(Text.translatable("research.hemomancy.hemomancy_origins.desc1"), 120)),
                new Page(0, new TextElement(Text.translatable("research.hemomancy.hemomancy_origins.desc2"), 120)),
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.hemomancy_origins.title2")),
                        new TextElement(Text.translatable("research.hemomancy.hemomancy_origins.desc3"), 100),
                        new ItemsElement(HmItems.BOOK, HmBlocks.MAGE_STATUE.asItem())),
                new Page(0, new CraftingElement(HmItems.BOOK.asItem())),
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.hemomancy_origins.title3")),
                        new TextElement(Text.translatable("research.hemomancy.hemomancy_origins.desc4"), 120))
        );

        add(Hemomancy.path("crimson_mage_path"), HmBlocks.MAGE_STATUE.asItem().getDefaultStack(), -50, 50, 0,
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.crimson_mage_path.title")),
                        new TextElement(Text.translatable("research.hemomancy.crimson_mage_path.desc1"), 120)),
                new Page(0, new TextElement(Text.translatable("research.hemomancy.crimson_mage_path.desc2"), 120)),
                new Page(0, new CraftingElement(HmBlocks.MAGE_STATUE.asItem())),
                new Page(0, new TextElement(Text.translatable("research.hemomancy.crimson_mage_path.desc3"), 120))
        );

        add(Hemomancy.path("life_essence"), HmItems.SACRIFICIAL_DAGGER.asItem().getDefaultStack(), 50, 50, 0,
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.life_essence.title")),
                        new TextElement(Text.translatable("research.hemomancy.life_essence.desc1"), 120)),
                new Page(0, new TextElement(Text.translatable("research.hemomancy.life_essence.desc2"), 120)),
                new Page(0, new CraftingElement(HmItems.SACRIFICIAL_DAGGER.asItem())),
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.life_essence.title2")),
                        new TextElement(Text.translatable("research.hemomancy.life_essence.desc4"), 120))
        );

        add(Hemomancy.path("blood_altar_foundations"), HmBlocks.BLOOD_ALTAR.asItem().getDefaultStack(), 0, 100, 0,
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.blood_altar_foundations.title")),
                        new TextElement(Text.translatable("research.hemomancy.blood_altar_foundations.desc1"), 80),
                        new TextElement(Text.translatable("research.hemomancy.blood_altar_foundations.desc2"), 40)),
                new Page(0, new CraftingElement(HmBlocks.BLOOD_ALTAR.asItem())),
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.blood_altar_foundations.title2")),
                        new TextElement(Text.translatable("research.hemomancy.blood_altar_foundations.desc3"), 40),
                        new StructureElement(Tier.getTiers().get((byte)2), HmBlocks.BLOOD_ALTAR.getDefaultState(), 70)),
                new Page(1, new TitleElement(Text.translatable("research.hemomancy.blood_altar_foundations.title3")),
                        new TextElement(Text.translatable("research.hemomancy.blood_altar_foundations.desc4"), 40),
                        new StructureElement(Tier.getTiers().get((byte)3), HmBlocks.BLOOD_ALTAR.getDefaultState(), 70)),
                new Page(2, new TitleElement(Text.translatable("research.hemomancy.blood_altar_foundations.title4")),
                        new TextElement(Text.translatable("research.hemomancy.blood_altar_foundations.desc5"), 40),
                        new StructureElement(Tier.getTiers().get((byte)4), HmBlocks.BLOOD_ALTAR.getDefaultState(), 80)),
                new Page(4, new TitleElement(Text.translatable("research.hemomancy.blood_altar_foundations.title5")),
                        new TextElement(Text.translatable("research.hemomancy.blood_altar_foundations.desc6"), 40),
                        new StructureElement(Tier.getTiers().get((byte)5), HmBlocks.BLOOD_ALTAR.getDefaultState(), 80)),
                new Page(6, new TitleElement(Text.translatable("research.hemomancy.blood_altar_foundations.title6")),
                        new TextElement(Text.translatable("research.hemomancy.blood_altar_foundations.desc7"), 40),
                        new StructureElement(Tier.getTiers().get((byte)6), HmBlocks.BLOOD_ALTAR.getDefaultState(), 80))
        );

        add(Hemomancy.path("blood_orbs"), HmItems.WEAK_BLOOD_ORB.asItem().getDefaultStack(), -100, 150, 0,
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.blood_orbs.title")),
                        new TextElement(Text.translatable("research.hemomancy.blood_orbs.desc1"), 130),
                        new TextElement(Text.translatable("research.hemomancy.blood_orbs.desc2"), 110)),
                new Page(0, new FusionElement(HmItems.WEAK_BLOOD_ORB.asItem())),
                new Page(0, new FusionElement(HmItems.APPRENTICE_BLOOD_ORB.asItem()),
                        new TextElement(Text.translatable("research.hemomancy.blood_orbs.desc3"), 80)),
                new Page(1, new TitleElement(Text.translatable("research.hemomancy.blood_orbs.title2")),
                        new TextElement(Text.translatable("research.hemomancy.blood_orbs.desc4"), 90)),
                new Page(2, new FusionElement(HmItems.MAGICIAN_BLOOD_ORB.asItem())),
                new Page(4, new FusionElement(HmItems.MASTER_BLOOD_ORB.asItem())),
                new Page(6, new TitleElement(Text.translatable("research.hemomancy.blood_orbs.title3")),
                        new TextElement(Text.translatable("research.hemomancy.blood_orbs.desc5"), 100)),
                new Page(6, new FusionElement(HmItems.ARCHMAGE_BLOOD_ORB.asItem())),
                new Page(8, new FusionElement(HmItems.TRANSCENDENTAL_BLOOD_ORB.asItem())),
                new Page(10, "avaritia", new TitleElement(Text.translatable("research.hemomancy.blood_orbs.title4")),
                        new TextElement(Text.translatable("research.hemomancy.blood_orbs.desc6"), 120),
                        new ItemsElement(HmItems.INEXHAUSTIBLE_BLOOD_ORB))
        );

        add(Hemomancy.path("glyphs_of_power"), HmItems.BLANK_GLYPH.getDefaultStack(), 100, 150, 0,
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.glyphs_of_power.title")),
                        new TextElement(Text.translatable("research.hemomancy.glyphs_of_power.desc1"), 120)),
                new Page(0, new FusionElement(HmItems.BLANK_GLYPH)),
                new Page(0, new TextElement(Text.translatable("research.hemomancy.glyphs_of_power.desc2"), 120)),
                new Page(0, new FusionElement(HmItems.FORTIFIED_GLYPH)),
                new Page(0, new FusionElement(HmItems.CRIMSON_GLYPH)),
                new Page(1, new TitleElement(Text.translatable("research.hemomancy.glyphs_of_power.title2")),
                        new TextElement(Text.translatable("research.hemomancy.glyphs_of_power.desc3"), 120)),
                new Page(1, new FusionElement(HmItems.FILLED_GLYPH)),
                new Page(2, new FusionElement(HmItems.DEMONIC_GLYPH)),
                new Page(4, new FusionElement(HmItems.INFERNAL_GLYPH))
        );

        add(Hemomancy.path("alchemy_mastery"), HmBlocks.ALCHEMY_TABLE.asItem().getDefaultStack(), 0, 200, 1,
                new Page(1, new TitleElement(Text.translatable("research.hemomancy.alchemy_mastery.title")),
                        new TextElement(Text.translatable("research.hemomancy.alchemy_mastery.desc1"), 70),
                        new TextElement(Text.translatable("research.hemomancy.alchemy_mastery.desc2"), 50)),
                new Page(1, new CraftingElement(HmBlocks.ALCHEMY_TABLE.asItem())),
                new Page(1, new TitleElement(Text.translatable("research.hemomancy.alchemy_mastery.title2")),
                        new TextElement(Text.translatable("research.hemomancy.alchemy_mastery.desc4"), 120)),
                new Page(1, new AlchemyElement(HmItems.BOOK)),
                new Page(4, new TitleElement(Text.translatable("research.hemomancy.alchemy_mastery.title3")),
                        new TextElement(Text.translatable("research.hemomancy.alchemy_mastery.desc5"), 110)),
                new Page(4, new AlchemyElement(Items.OBSIDIAN))
        );

        add(Hemomancy.path("runes_of_enhancement"), HmBlocks.BLANK_RUNE.asItem().getDefaultStack(), -150, 250, 0,
                new Page(0, new TitleElement(Text.translatable("research.hemomancy.runes_of_enhancement.title")),
                        new TextElement(Text.translatable("research.hemomancy.runes_of_enhancement.desc1"), 60),
                        new TextElement(Text.translatable("research.hemomancy.runes_of_enhancement.desc2"), 60)),
                new Page(0, new CraftingElement(HmBlocks.BLANK_RUNE.asItem())),
                new Page(0, new TextElement(Text.translatable("research.hemomancy.runes_of_enhancement.desc5"), 120)),
                new Page(0, new CraftingElement(HmBlocks.RUNE_STAIRS.asItem())),
                new Page(0, new CraftingElement(HmBlocks.RUNE_SLAB.asItem())),
                new Page(0, new CraftingElement(HmBlocks.SPEED_RUNE.asItem())),
                new Page(0, new CraftingElement(HmBlocks.SACRIFICES_RUNE.asItem())),
                new Page(1, new TitleElement(Text.translatable("research.hemomancy.runes_of_enhancement.title2")),
                        new TextElement(Text.translatable("research.hemomancy.runes_of_enhancement.desc3"), 90)),
                new Page(1, new CraftingElement(HmBlocks.CAPACITY_RUNE.asItem())),
                new Page(1, new CraftingElement(HmBlocks.RELATIONS_RUNE.asItem())),
                new Page(3, new CraftingElement(HmBlocks.RESONANT_CAPACITY_RUNE.asItem())),
                new Page(10, "avaritia", new TitleElement(Text.translatable("research.hemomancy.runes_of_enhancement.title3")),
                        new TextElement(Text.translatable("research.hemomancy.runes_of_enhancement.desc4"), 100))
        );

        add(Hemomancy.path("sigils_of_command"), HmItems.AIR_SIGIL.getDefaultStack(), 150, 250, 3,
                new Page(3, new TitleElement(Text.translatable("research.hemomancy.sigils_of_command.title")),
                        new TextElement(Text.translatable("research.hemomancy.sigils_of_command.desc1"), 80),
                        new TextElement(Text.translatable("research.hemomancy.sigils_of_command.desc2"), 40)),
                new Page(3, new AlchemyElement(HmItems.WATER_SIGIL)),
                new Page(3, new AlchemyElement(HmItems.LAVA_SIGIL)),
                new Page(3, new AlchemyElement(HmItems.DRAINAGE_SIGIL)),
                new Page(4, new TitleElement(Text.translatable("research.hemomancy.sigils_of_command.title2")),
                        new TextElement(Text.translatable("research.hemomancy.sigils_of_command.desc3"), 120)),
                new Page(4, new AlchemyElement(HmItems.AIR_SIGIL)),
                new Page(4, new AlchemyElement(HmItems.MAGNETISM_SIGIL)),
                new Page(4, new AlchemyElement(HmItems.RESISTANCE_SIGIL)),
                new Page(5, new TitleElement(Text.translatable("research.hemomancy.sigils_of_command.title3")),
                        new TextElement(Text.translatable("research.hemomancy.sigils_of_command.desc4"), 120)),
                new Page(5, new AlchemyElement(HmItems.MOVEMENT_SIGIL)),
                new Page(5, new AlchemyElement(HmItems.TELEPOSITION_SIGIL)),
                new Page(5, new AlchemyElement(HmItems.GROW_SIGIL))
        );

        add(Hemomancy.path("crimson_artifacts"), HmItems.CRIMSON_STEEL_INGOT.getDefaultStack(), 0, 300, 1,
                new Page(1, new TitleElement(Text.translatable("research.hemomancy.crimson_artifacts.title")),
                        new TextElement(Text.translatable("research.hemomancy.crimson_artifacts.desc1"), 60),
                        new TextElement(Text.translatable("research.hemomancy.crimson_artifacts.desc2"), 60)),
                new Page(1, new FusionElement(HmItems.CRIMSON_STEEL_INGOT)),
                new Page(2, new TextElement(Text.translatable("research.hemomancy.crimson_artifacts.desc3"), 120)),
                new Page(2, new CraftingElement(HmBlocks.CRIMSON_ORNAMENT.asItem())),
                new Page(2, new TitleElement(Text.translatable("research.hemomancy.crimson_artifacts.title2")),
                        new TextElement(Text.translatable("research.hemomancy.crimson_artifacts.desc4"), 120)),
                new Page(4, new AlchemyElement(HmBlocks.TRANSCENDENTAL_CRYSTAL.asItem()))
        );

        add(Hemomancy.path("desecrated_arsenal"), HmItems.DESECRATED_SWORD.getDefaultStack(), -200, 350, 7,
                new Page(7, new TitleElement(Text.translatable("research.hemomancy.desecrated_arsenal.title")),
                        new TextElement(Text.translatable("research.hemomancy.desecrated_arsenal.desc1"), 80),
                        new TextElement(Text.translatable("research.hemomancy.desecrated_arsenal.desc2"), 40)),
                new Page(7, new FusionElement(HmItems.DESECRATED_PICKAXE)),
                new Page(7, new FusionElement(HmItems.DESECRATED_AXE)),
                new Page(7, new FusionElement(HmItems.DESECRATED_SWORD)),
                new Page(7, new FusionElement(HmItems.DESECRATED_SHOVEL)),
                new Page(10, new TitleElement(Text.translatable("research.hemomancy.desecrated_arsenal.title2")),
                        new TextElement(Text.translatable("research.hemomancy.desecrated_arsenal.desc3"), 120)),
                new Page(10, new FusionElement(HmItems.AWAKENED_DESECRATED_PICKAXE)),
                new Page(10, new FusionElement(HmItems.AWAKENED_DESECRATED_AXE)),
                new Page(10, new FusionElement(HmItems.AWAKENED_DESECRATED_SWORD)),
                new Page(10, new FusionElement(HmItems.AWAKENED_DESECRATED_SHOVEL))
        );
    }

    public static void add(Identifier id, Object icon, int x, int y, int lvl, Page... pages) {
        Research research = new Research(id, icon, List.of(pages), x, y, lvl);
        RESEARCHES.put(research.id(), research);
    }

    public static Collection<Research> getAll() {
        return RESEARCHES.values();
    }

    public record Research(Identifier id, Object icon, List<Page> pages, int x, int y, int lvl) {
        public boolean isUnlocked(PlayerEntity player){
            return MagePlayer.of(player).getLevel() >= lvl();
        }
    }
}
