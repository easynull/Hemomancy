package ru.easynull.hemomancy.registry.items.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import ru.easynull.hemomancy.utils.EnergyUtils;

import java.util.List;

public class SigilItem extends Item {
    protected final Context action;
    protected final int lpCost;
    protected final boolean allowAir;
    protected boolean consumeLp = true;

    public SigilItem(Properties settings, Context action, int lpCost, boolean allowAir) {
        super(settings.stacksTo(1));
        this.action = action;
        this.lpCost = lpCost;
        this.allowAir = allowAir;
    }

    public SigilItem(Properties settings, Context action, int lpCost) {
        this(settings, action, lpCost, false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = (BlockHitResult) player.pick(5.0, 0.0f, false);
        BlockPos pos = hit.getBlockPos();
        if (!allowAir && level.getBlockState(pos).isAir()) {
            return InteractionResultHolder.fail(stack);
        }
        action.perform(new SigilContext(level, pos, hit.getDirection(), player, stack, this));
        if (consumeLp) EnergyUtils.extractLp(player, lpCost);
        else consumeLp = true;
        return InteractionResultHolder.consume(stack);
    }

    public void cancelConsumeLp() {
        this.consumeLp = false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        String key = "tooltip.hemomancy." + this.getDescriptionId().split("\\.")[2] + ".desc";
        tooltip.add(Component.translatable(key).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        if(stack.has(DataComponents.CUSTOM_DATA) && stack.get(DataComponents.CUSTOM_DATA).contains("Tooltip")){
            tooltip.add(Component.literal(stack.get(DataComponents.CUSTOM_DATA).copyTag().getString("Tooltip")));
        }
    }

    public record SigilContext(Level level, BlockPos pos, Direction side, Player player, ItemStack stack, SigilItem item) {}

    @FunctionalInterface
    public interface Context {
        void perform(SigilContext ctx);
    }
}