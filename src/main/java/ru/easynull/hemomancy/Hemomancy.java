package ru.easynull.hemomancy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.easynull.hemomancy.api.altar.TierManager;
import ru.easynull.hemomancy.api.mage.quest.MageQuestManager;
import ru.easynull.hemomancy.api.ritual.RitualManager;
import ru.easynull.hemomancy.net.NetHandler;
import ru.easynull.hemomancy.proxy.MainProxy;
import ru.easynull.hemomancy.registry.HmBlockEntities;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.HmItems;
import ru.easynull.hemomancy.registry.HmRecipes;
import ru.easynull.hemomancy.registry.commands.MageCommands;

public final class Hemomancy implements ModInitializer {
    public static final String ID = "hemomancy";
    public static final Logger LOGGER = LoggerFactory.getLogger("Hemomancy");

    @Override
    public void onInitialize() {
        MainProxy.setProxy(new MainProxy());
        HmItems.onInit();
        HmBlocks.onInit();
        HmBlockEntities.onInit();
        HmRecipes.onInit();
        NetHandler.onInit();
        TierManager.onInit();
        MageQuestManager.onInit();
        RitualManager.onInit();
        onEvents();
    }

    private static void onEvents(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> MageCommands.onInit(dispatcher));
    }

    public static Identifier path(String path) {
        return Identifier.of(ID, path);
    }
}
