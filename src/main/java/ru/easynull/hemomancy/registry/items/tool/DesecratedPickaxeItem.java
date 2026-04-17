package ru.easynull.hemomancy.registry.items.tool;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.easynull.hemomancy.utils.EnergyUtils;
import ru.easynull.hemomancy.utils.HmCommonUtils;

public final class DesecratedPickaxeItem extends PickaxeItem implements DesecratedTool {
    private final boolean awakened;

    public DesecratedPickaxeItem(Settings settings, boolean awakened) {
        super(ToolMaterials.NETHERITE, 2, -2.8f, settings);
        this.awakened = awakened;
    }

    @Override
    public void onAbilityMine(ItemStack stack, World world, BlockState state, BlockPos pos, ServerPlayerEntity player) {
        if (!awakened || !state.isIn(BlockTags.PICKAXE_MINEABLE) || player.isSneaking()) {
            return;
        }
        HmCommonUtils.forEachInCube(pos, 3, p -> {
            BlockPos above = p.up();
            BlockState aboveState = world.getBlockState(above);
            if (aboveState.isAir()) return;
            if (aboveState.isIn(BlockTags.PICKAXE_MINEABLE)) {
                world.breakBlock(above, !player.isCreative(), player);
            }
        });

        EnergyUtils.extractLp(player, 100000);
    }

//    public static Optional<SmeltingRecipe> getOreRecipe(World world, ItemStack stack) {
//        return world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(stack), world);
//    }
}