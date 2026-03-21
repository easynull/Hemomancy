package ru.easynull.hemomancy.registry.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.ritual.RitualManager;

import java.util.List;

public final class RitualStaffItem extends Item {
    public RitualStaffItem() {
        super(new Settings().maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        String id = stack.getOrCreateNbt().getString("RitualId");
        if (id != null && !id.isEmpty() && !context.getWorld().isClient()) {
            RitualManager.Ritual ritual = RitualManager.get(Identifier.tryParse(id));
            if (ritual != null) {
                for(var component : ritual.components()) {
                    BlockState state = component.state;
                    BlockPos pos = context.getBlockPos().add(component.pos);
                    if (context.getWorld().getBlockState(pos).isAir()) context.getWorld().setBlockState(pos, state, 3);
                }
                return ActionResult.CONSUME;
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        String id = stack.getOrCreateNbt().getString("RitualId");
        if (id != null && !id.isEmpty()) tooltip.add(Text.translatable("tooltip.hemomancy.ritual_staff", Text.translatable(Identifier.tryParse(id).toTranslationKey("ritual", "name"))));
    }
}
