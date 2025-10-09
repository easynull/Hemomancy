package com.easynull.hemomancy.registers;

import com.mw.nullcore.core.builders.ArmorMaterialBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;

public final class HcMaterials {
    public static final ToolMaterial desecratedTool = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 9999, 9.0F, 4.0F, 22, ItemTags.NETHERITE_TOOL_MATERIALS);
//    public static final ArmorMaterial desecratedArmor = ArmorMaterialBuilder.builder().durability(-1).resistance(10).asset("minecraft", "netherite").enchantability(20).build();
}
