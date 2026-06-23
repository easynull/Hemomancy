package ru.easynull.hemomancy.render.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.registry.HmDataComponents;
import ru.easynull.hemomancy.registry.items.BookItem;

public final class BookHud {
    public static final Minecraft client = Minecraft.getInstance();
    private static final int UPDATE_INTERVAL = 5;

    private static BookItem cachedBook = null;

    private static long cachedLp = 0;
    private static long cachedMaxLp = 0;
    private static Item cachedDisplayedItem = null;
    private static byte cachedTier = 0;

    private static final int HUD_X = 10;
    private static final int HUD_Y = 10;
    private static final ResourceLocation BAR_TEXTURE = Hemomancy.path("textures/gui/bloodbar.png");

    public static void onRender(GuiGraphics ctx) {
        Player player = client.player;
        if (player == null) return;

        if (client.level.getGameTime() % UPDATE_INTERVAL == 0) {
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

        ctx.blit(BAR_TEXTURE, x + 3, y + 3, 0, 0, 21, 48, 48, 48);
        ctx.blit(BAR_TEXTURE, x + 9, y + 43 - barHeight, 21, 33 - barHeight, 12, barHeight, 48, 48);

        if (displayedItem != null) {
            ctx.renderItem(new ItemStack(displayedItem), x + 29, y + 10);
        }

        if (tier > 0) {
            String tierText = Component.translatable("tooltip.hemomancy.tier", tier).getString();
            ctx.drawString(client.font, tierText, x + 26, y + 30, 0xFFFFFFFF, false);
        }

        String lpText = String.format("%,d", lp);
        ctx.drawCenteredString(client.font, lpText, x + 17, y + 53, 0xFFFF0000);
    }

    private static void updateCache(Player player) {
        cachedBook = null;
        cachedLp = 0;
        cachedMaxLp = 0;
        cachedDisplayedItem = null;
        cachedTier = 0;
        NonNullList<ItemStack> main = player.getInventory().items;
        for (ItemStack stack : main) {
            if (stack.getItem() instanceof BookItem book && stack.get(HmDataComponents.EXTENDED)) {
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
