package ru.easynull.hemomancy.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.blocks.type.*;

public final class HmBlockEntities {

    public static final BlockEntityType<AltarBE> BLOOD_ALTAR = registerBlockEntity("blood_altar", AltarBE::new, HmBlocks.BLOOD_ALTAR);
    public static final BlockEntityType<AlchemyTableBE> ALCHEMY_TABLE = registerBlockEntity("alchemy_table", AlchemyTableBE::new, HmBlocks.ALCHEMY_TABLE);

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
        Identifier id = Hemomancy.path(name);
        return (BlockEntityType<T>) Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.create(entityFactory, blocks).build());
    }

    public static void onInit(){}
}
