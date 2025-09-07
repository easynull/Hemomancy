package com.easynull.hemomancy.registers;

import com.mw.nullcore.core.managers.ConfigManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.loading.FMLPaths;

import java.util.List;

import static com.easynull.hemomancy.Hemomancy.ID;

public final class HcConfig {
    public static ConfigManager.Initialize.Unit<Boolean> advancedController;
    public static ConfigManager.Initialize.Unit<List<String>> unmovementBlocks;

    public static void init() {
        ConfigManager.register(ID, FMLPaths.CONFIGDIR.get(), () -> {
            advancedController = ConfigManager.create(
                    "advanced_controller",
                    "Shows a crimson bar even when the player is not holding the controller",
                    false
            );
            unmovementBlocks = ConfigManager.create(
                    "unmovement_blocks",
                    "Blocks that cannot be moved with the movement sigil",
                    List.of("minecraft:bedrock")
            );
        }, true);
    }
}
