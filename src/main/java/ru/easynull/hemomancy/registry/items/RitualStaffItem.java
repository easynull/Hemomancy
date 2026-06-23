package ru.easynull.hemomancy.registry.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import ru.easynull.hemomancy.api.ritual.RitualManager;

import java.util.List;

public final class RitualStaffItem extends Item {
    public RitualStaffItem() {
        super(new Properties().component(DataComponents.CUSTOM_DATA, CustomData.EMPTY).stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        String id = stack.get(DataComponents.CUSTOM_DATA).copyTag().getString("RitualId");
        if (!id.isEmpty() && !context.getLevel().isClientSide()) {
            RitualManager.Ritual ritual = RitualManager.get(ResourceLocation.tryParse(id));
            if (ritual != null) {
                for(var component : ritual.components()) {
                    BlockState state = component.state;
                    BlockPos pos = context.getClickedPos().offset(component.pos);
                    if (context.getLevel().getBlockState(pos).isAir()) context.getLevel().setBlock(pos, state, 3);
                }
                return InteractionResult.CONSUME;
            }
        }
        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        String id = stack.get(DataComponents.CUSTOM_DATA).copyTag().getString("RitualId");
        if (!id.isEmpty()) tooltip.add(Component.translatable("tooltip.hemomancy.ritual_staff", Component.translatable(ResourceLocation.tryParse(id).toLanguageKey("ritual", "name"))));
    }
}
