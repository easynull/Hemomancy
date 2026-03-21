package ru.easynull.hemomancy.render.gui.book;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.mage.ResearchManager.Research;

import java.util.List;
import java.util.stream.Collectors;

import static ru.easynull.hemomancy.render.gui.book.BookGui.BOOK;

public final class PageGui extends Screen {
    public static final int BOOK_WIDTH = 280;
    public static final int BOOK_HEIGHT = 180;

    private static final int PAGE_WIDTH = 120;
    private static final int PAGE_HEIGHT = 160;
    private static final int PAGE_TOP = 10;
    private static final int PAGE_LEFT_MARGIN = 20;
    private static final int PAGE_RIGHT_MARGIN = 7;

    private static final int BACK_BUTTON_X = 10;
    private static final int FORWARD_BUTTON_X = 261;
    private static final int BUTTON_Y = 158;
    private static final int BUTTON_SIZE = 10;

    private final List<Page> availablePages;
    private int currentPage;
    private final float guiMapX, guiMapY;

    public PageGui(Research research, float guiMapX, float guiMapY) {
        super(Text.empty());
        this.guiMapX = guiMapX;
        this.guiMapY = guiMapY;
        this.availablePages = research.pages().stream()
                .filter(p -> p.isUnlocked(MinecraftClient.getInstance().player))
                .filter(p -> p.requireMod() == null || FabricLoader.getInstance().isModLoaded(p.requireMod()))
                .collect(Collectors.toList());
        this.currentPage = 0;
    }

    private int getGuiLeft() { return (width - BOOK_WIDTH) / 2; }
    private int getGuiTop()  { return (height - BOOK_HEIGHT) / 2; }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        renderBook(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderBook(DrawContext context, int mouseX, int mouseY, float delta) {
        int guiLeft = getGuiLeft();
        int guiTop = getGuiTop();

        context.drawTexture(BOOK, guiLeft, guiTop, 0, 0, BOOK_WIDTH, BOOK_HEIGHT, 512, 512);

        boolean hasNext = currentPage + 2 < availablePages.size();
        int backU = isBackButton(mouseX, mouseY) ? 290 : 280;
        context.drawTexture(BOOK, guiLeft + BACK_BUTTON_X, guiTop + BUTTON_Y, backU, 0, BUTTON_SIZE, BUTTON_SIZE, 512, 512);

        if (hasNext) {
            int forwardU = isNextButton(mouseX, mouseY) ? 290 : 280;
            context.drawTexture(BOOK, guiLeft + FORWARD_BUTTON_X, guiTop + BUTTON_Y, forwardU, 10, BUTTON_SIZE, BUTTON_SIZE, 512, 512);
        }

        int rightPageX = guiLeft + (BOOK_WIDTH / 2 - 6);
        int pageY = guiTop + PAGE_TOP;

        if (currentPage < availablePages.size())
            availablePages.get(currentPage).render(context, guiLeft, pageY, mouseX, mouseY, delta);
        if (currentPage + 1 < availablePages.size())
            availablePages.get(currentPage + 1).render(context, rightPageX, pageY, mouseX, mouseY, delta);

        PageHit hit = getPageHit(mouseX, mouseY);
        if (hit != null) {
            hit.page().renderTooltip(context, mouseX, mouseY, hit.relX(), hit.relY());
        }
    }

    private boolean isBackButton(double mx, double my) {
        int left = getGuiLeft() + BACK_BUTTON_X, top = getGuiTop() + BUTTON_Y;
        return mx >= left && mx <= left + BUTTON_SIZE && my >= top && my <= top + BUTTON_SIZE;
    }

    private boolean isNextButton(double mx, double my) {
        int left = getGuiLeft() + FORWARD_BUTTON_X, top = getGuiTop() + BUTTON_Y;
        return mx >= left && mx <= left + BUTTON_SIZE && my >= top && my <= top + BUTTON_SIZE;
    }

    private PageHit getPageHit(double mx, double my) {
        int guiLeft = getGuiLeft();
        int guiTop = getGuiTop();
        int leftX = guiLeft + PAGE_LEFT_MARGIN;
        int rightX = guiLeft + BOOK_WIDTH - PAGE_RIGHT_MARGIN - PAGE_WIDTH;
        int pageY = guiTop + PAGE_TOP;

        int maxPages = availablePages.size();
        if (currentPage < maxPages && isPointInRect(mx, my, leftX, pageY, PAGE_WIDTH, PAGE_HEIGHT))
            return new PageHit(availablePages.get(currentPage), (int) (mx - leftX), (int) (my - pageY));
        if (currentPage + 1 < maxPages && isPointInRect(mx, my, rightX, pageY, PAGE_WIDTH, PAGE_HEIGHT))
            return new PageHit(availablePages.get(currentPage + 1), (int) (mx - rightX), (int) (my - pageY));
        return null;
    }

    private static boolean isPointInRect(double x, double y, int rectX, int rectY, int w, int h) {
        return x >= rectX && x <= rectX + w && y >= rectY && y <= rectY + h;
    }

    private record PageHit(Page page, int relX, int relY) {}

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return super.mouseClicked(mx, my, button);

        PageHit hit = getPageHit(mx, my);
        if (hit != null && hit.page.mouseClicked(hit.relX, hit.relY, button))
            return true;

        if (isNextButton(mx, my) && currentPage + 2 < availablePages.size()) {
            turnPage(2);
            return true;
        }
        if (isBackButton(mx, my)) {
            if (currentPage > 0) turnPage(-2);
            else close();
            return true;
        }
        return super.mouseClicked(mx, my, button);
    }

    private void turnPage(int delta) {
        currentPage += delta;
        playTurnSound();
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        PageHit hit = getPageHit(mx, my);
        return hit != null && hit.page.mouseDragged(hit.relX, hit.relY, button, dx, dy) ||
                super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double amount) {
        PageHit hit = getPageHit(mx, my);
        if(hit == null || !hit.page.mouseScrolled(hit.relX, hit.relY, amount)) {
            if (amount == 1 && currentPage + 2 < availablePages.size()) {
                turnPage(2);
                return true;
            }
            if (amount == -1) {
                if (currentPage > 0) turnPage(-2);
                else close();
                return true;
            }
        } else {
            return hit.page.mouseScrolled(hit.relX, hit.relY, amount);
        }
        return super.mouseScrolled(mx, my, amount);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        PageHit hit = getPageHit(mx, my);
        if (hit != null) hit.page.mouseReleased(hit.relX, hit.relY, button);
        return super.mouseReleased(mx, my, button);
    }

    private void playTurnSound() {
        if (client != null && client.player != null)
            client.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1f, 1f);
    }

    @Override
    public void close() {
        if (client != null) {
//            for (Page page : availablePages) {
//                page.onClose();
//            }
            client.setScreen(new BookGui(guiMapX, guiMapY));
        }
    }

    @Override public boolean shouldPause() { return false; }
}