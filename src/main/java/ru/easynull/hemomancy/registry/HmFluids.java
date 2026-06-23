package ru.easynull.hemomancy.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FlowingFluid;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.fluids.BloodFluid;

public final class HmFluids {
    public static final FlowingFluid BLOOD = Registry.register(
            BuiltInRegistries.FLUID,
            Hemomancy.path("blood"),
            new BloodFluid.Source()
    );

    public static final FlowingFluid FLOWING_BLOOD = Registry.register(
            BuiltInRegistries.FLUID,
            Hemomancy.path("flowing_blood"),
            new BloodFluid.Flowing()
    );

    public static void onInit() {}
}