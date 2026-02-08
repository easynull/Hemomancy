package ru.easynull.hemomancy;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.easynull.hemomancy.api.altar.Tier;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.HmItems;

public final class Hemomancy implements ModInitializer {
    public static final String ID = "hemomancy";
    public static final Logger LOGGER = LoggerFactory.getLogger("Hemomancy");

    @Override
    public void onInitialize() {
        HmItems.onInit();
        HmBlocks.onInit();
        HmBlockEntities.onInit();
        Tier.onInit();
//        NetHandler.onInit();
        onEvents();
    }

    private static void onEvents(){}

    public static Identifier path(String path) {
        return Identifier.of(ID, path);
    }
}
