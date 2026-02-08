package ru.easynull.hemomancy.registry.items.sigil;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import ru.easynull.hemomancy.utils.EnergyUtils;

import java.util.List;

public class SigilItem extends Item {
    protected final Context action;
    protected final int lpCost;
    protected final boolean allowAir;
    protected boolean consumeLp = true;

    public SigilItem(Settings settings, Context action, int lpCost, boolean allowAir) {
        super(settings.maxCount(1));
        this.action = action;
        this.lpCost = lpCost;
        this.allowAir = allowAir;
    }

    public SigilItem(Settings settings, Context action, int lpCost) {
        this(settings, action, lpCost, false);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        BlockHitResult hit = (BlockHitResult) player.raycast(5.0, 0.0f, false);
        BlockPos pos = hit.getBlockPos();
        if (!allowAir && world.getBlockState(pos).isAir()) {
            return TypedActionResult.fail(stack);
        }
        action.perform(new SigilContext(world, pos, hit.getSide(), player, stack, this));
        EnergyUtils.extractLp(player, lpCost);
        return consumeLp ? TypedActionResult.consume(stack) : TypedActionResult.fail(stack);
    }

    public void setConsumeLp(boolean consume) {
        this.consumeLp = consume;
    }

    public boolean isActive(ItemStack stack){
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        String key = "tooltip.hemomancy." + this.getTranslationKey().split("\\.")[2] + ".desc";
        tooltip.add(Text.translatable(key).formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
    }

    public record SigilContext(World world, BlockPos pos, Direction side, PlayerEntity player, ItemStack stack, SigilItem item) {}

    @FunctionalInterface
    public interface Context {
        void perform(SigilContext ctx);
    }
}