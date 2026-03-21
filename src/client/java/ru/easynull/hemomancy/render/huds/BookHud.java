package ru.easynull.hemomancy.render.huds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.items.BookItem;

public final class BookHud {
    public static final MinecraftClient client = MinecraftClient.getInstance();
    private static final int UPDATE_INTERVAL = 5;

    private static BookItem cachedBook = null;

    private static long cachedLp = 0;
    private static long cachedMaxLp = 0;
    private static Item cachedDisplayedItem = null;
    private static byte cachedTier = 0;

    private static final int HUD_X = 10;
    private static final int HUD_Y = 10;
    private static final Identifier BAR_TEXTURE = Hemomancy.path("textures/gui/bloodbar.png");

    public static void onRender(DrawContext ctx) {
        PlayerEntity player = client.player;
        if (player == null) return;

        if (client.world.getTime() % UPDATE_INTERVAL == 0) {
            updateCache(player);
        }

        if (cachedBook == null || cachedLp == 0) return;

        long lp = cachedLp;
        long maxLp = cachedMaxLp;
        Item displayedItem = cachedDisplayedItem;
        byte tier = cachedTier;

        int progress = (int) (lp * 27.0 / maxLp);
        int barHeight = (int) (progress * 1.5);

        int x = HUD_X;
        int y = HUD_Y;

        ctx.drawTexture(BAR_TEXTURE, x + 3, y + 3, 0, 0, 21, 48, 48, 48);
        ctx.drawTexture(BAR_TEXTURE, x + 9, y + 43 - barHeight, 21, 33 - barHeight, 12, barHeight, 48, 48);

        if (displayedItem != null) {
            ctx.drawItem(new ItemStack(displayedItem), x + 29, y + 10);
        }

        if (tier > 0) {
            String tierText = Text.translatable("tooltip.hemomancy.tier", tier).getString();
            ctx.drawText(client.textRenderer, tierText, x + 26, y + 30, 0xFFFFFFFF, false);
        }

        String lpText = String.format("%,d", lp);
        ctx.drawCenteredTextWithShadow(client.textRenderer, lpText, x + 17, y + 53, 0xFFFF0000);
    }

    private static void updateCache(PlayerEntity player) {
        cachedBook = null;
        cachedLp = 0;
        cachedMaxLp = 0;
        cachedDisplayedItem = null;
        cachedTier = 0;
        DefaultedList<ItemStack> main = player.getInventory().main;
        for (ItemStack stack : main) {
            if (stack.getItem() instanceof BookItem book && stack.hasNbt() && stack.getNbt().getInt("Level") > 1) {
                cachedBook = book;

                cachedLp = book.getCurrentLp();
                cachedMaxLp = book.getCurrentMaxLp();
                cachedDisplayedItem = book.getCurrentDisplayedItem();
                cachedTier = book.getCurrentTier();
                break;
            }
        }
    }
}
