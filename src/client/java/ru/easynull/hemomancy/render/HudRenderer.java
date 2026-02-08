package ru.easynull.hemomancy.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.items.ControllerItem;

public final class HudRenderer {
    public static final MinecraftClient client = MinecraftClient.getInstance();

    public static void onRenderControllerHud(DrawContext ctx) {
        PlayerEntity player = client.player;
        if (player == null) return;

        ItemStack stack = player.getMainHandStack();
        if (!(stack.getItem() instanceof ControllerItem controller)) return;

        if (controller.getCurrentLp() == 0) return;

        int x = 10;
        int y = 10;

        Identifier barTexture = Hemomancy.path("textures/gui/bloodbar.png");

        int progress = (int) (controller.getCurrentLp() * 27.0 / controller.getCurrentMaxLp());
        int barHeight = (int) (progress * 1.5);

        ctx.drawTexture(barTexture, x + 3, y + 3, 0, 0, 21, 48, 48, 48);

        ctx.drawTexture(barTexture, x + 3 + 6, y + 3 + 40 - barHeight, 21, 33 - barHeight, 12, barHeight, 48, 48);

        if (controller.getCurrentDisplayedItem() != null) {
            ctx.drawItem(new ItemStack(controller.getCurrentDisplayedItem()), x + 3 + 26, y + 3 + 7);
        }

        if (controller.getCurrentTier() > 0) {
            String tierText = Text.translatable("tooltip.hemomancy.tier", controller.getCurrentTier()).getString();
            ctx.drawText(client.textRenderer, tierText, x + 3 + 23, y + 3 + 27, 0xFFFFFFFF, false);
        }

        String lpText = Long.toString(controller.getCurrentLp());
        ctx.drawCenteredTextWithShadow(client.textRenderer, lpText, x + 3 + 14, y + 3 + 50, 0xFFFF0000);
    }
}
