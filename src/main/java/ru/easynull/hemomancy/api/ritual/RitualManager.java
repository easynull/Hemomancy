package ru.easynull.hemomancy.api.ritual;

import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.altar.TierManager;
import ru.easynull.hemomancy.api.ritual.trigger.RitualTrigger;
import ru.easynull.hemomancy.registry.HmBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public final class RitualManager {
    public static final Map<ResourceLocation, Ritual> RITUALS = new HashMap<>();

    public static void onInit(){
//        add(Hemomancy.path("rain"), 2, components -> {
//            TierManager.addMirrored(components, 1, 0, 1, HmBlocks.RITUAL_STONE.getDefaultState(), true, false);
//        }, new RitualTrigger() {
//            @Override
//            public boolean canStart(ServerWorld level, BlockPos pos) {
//                return false;
//            }
//
//            @Override
//            public boolean inProgress(World level, BlockPos pos) {
//                return false;
//            }
//
//            @Override
//            public void onCompleted(World level, BlockPos pos) {
//
//            }
//        });
    }

    public static void add(ResourceLocation id, int lvl, Consumer<Consumer<TierManager.Component>> builder, RitualTrigger trigger) {
        List<TierManager.Component> components = new ArrayList<>();
        builder.accept(components::add);
        Ritual ritual = new Ritual(lvl, components, trigger);
        if (RITUALS.containsKey(id)) {
            throw new IllegalStateException("Ritual already registered!");
        }
        RITUALS.put(id, ritual);
    }

    public static Ritual get(ResourceLocation id){
        return RITUALS.get(id);
    }

    public record Ritual(int lvl, List<TierManager.Component> components, RitualTrigger trigger){}
}
