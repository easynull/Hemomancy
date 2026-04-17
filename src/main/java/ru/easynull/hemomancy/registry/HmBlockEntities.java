package ru.easynull.hemomancy.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.blocks.*;
import ru.easynull.hemomancy.registry.blocks.type.*;
import ru.easynull.hemomancy.utils.HmCommonUtils;

import java.util.List;

public final class HmBlockEntities {

    public static final BlockEntityType<BloodAltarBlockEntity> BLOOD_ALTAR = registerBlockEntity("blood_altar", BloodAltarBlockEntity::new, HmCommonUtils.getElementsClasses(Registries.BLOCK, BloodAltarBlock.class));
    public static final BlockEntityType<AlchemyTableBlockEntity> ALCHEMY_TABLE = registerBlockEntity("alchemy_table", AlchemyTableBlockEntity::new, HmCommonUtils.getElementsClasses(Registries.BLOCK, AlchemyTableBlock.class));

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, List<Block> blocks) {
        Identifier id = Hemomancy.path(name);
        return (BlockEntityType<T>) Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.create(entityFactory, blocks.toArray(new Block[0])).build());
    }

    public static void onInit(){}
}
