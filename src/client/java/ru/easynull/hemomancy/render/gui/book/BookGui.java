package ru.easynull.hemomancy.render.gui.book;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.mage.ResearchManager;
import ru.easynull.hemomancy.api.mage.ResearchManager.Research;

import java.util.LinkedList;
import java.util.List;

public final class BookGui extends Screen {
    private static final Identifier BACKGROUND = Hemomancy.path("textures/gui/book/background.jpg");
    public static final Identifier BOOK = Hemomancy.path("textures/gui/book/book.png");

    private static final int BG_OFFSET = 16;
    private static final int ICON_OFFSET = 11;
    private static final float ZOOM = 7.0f;

    private final LinkedList<Research> researches = new LinkedList<>();
    private int boundsLeft, boundsTop, boundsRight, boundsBottom;
    private int startX, startY, screenX, screenY;
    private int lastMouseX, lastMouseY;
    private float pX, pY;
    private float targetMapX, targetMapY;
    private float curMouseX, curMouseY;
    private boolean mousePressed;
    private boolean dragging;
    private Research induced;
    private final PlayerEntity player;

    public static float lastX = 1000f, lastY = 1000f;

    public BookGui() {
        this(lastX, lastY);
    }

    public BookGui(float x, float y) {
        super(Text.empty());
        this.player = MinecraftClient.getInstance().player;
        this.targetMapX = this.pX = this.curMouseX = x;
        this.targetMapY = this.pY = this.curMouseY = y;
    }

    private void recalculateBounds() {
//        boundsLeft = boundsTop = Integer.MAX_VALUE;
//        boundsRight = boundsBottom = Integer.MIN_VALUE;
        for (Research research : researches) {
            boundsLeft = Math.min(boundsLeft, research.x() - screenX + 48);
            boundsRight = Math.max(boundsRight, research.x() - 24);
            boundsTop = Math.min(boundsTop, research.y() - screenY + 48);
            boundsBottom = Math.max(boundsBottom, research.y() - 24);
        }
    }

    public void updateResearch() {
        clearChildren();
        startX = (int) (width * 0.25f);
        startY = (int) (height * 0.2f);
        screenX = width - 2 * startX;
        screenY = height - 2 * startY;

        researches.clear();
        ResearchManager.getAll().stream().filter(r -> r.isUnlocked(player)).forEach(researches::add);

        recalculateBounds();
    }

    @Override
    protected void init() {
        updateResearch();
        if (lastX == 1000f) {
            targetMapX = pX = curMouseX = (boundsLeft + boundsRight) / 2f;
        }
        if (lastY == 1000f) {
            targetMapY = pY = curMouseY = (boundsTop + boundsBottom) / 2f;
        }
    }

    @Override
    public void close() {
        lastX = pX;
        lastY = pY;
        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        handleDragging(mouseX, mouseY);
        clampTargetMap();

        int locX = MathHelper.floor(curMouseX + (pX - curMouseX) * delta);
        int locY = MathHelper.floor(curMouseY + (pY - curMouseY) * delta);
        locX = MathHelper.clamp(locX, boundsLeft, boundsRight - 1);
        locY = MathHelper.clamp(locY, boundsTop, boundsBottom - 1);

        drawBackground(context, mouseX, mouseY, locX, locY);
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 300);
        drawFrame(context, mouseX, mouseY);
        context.getMatrices().pop();
    }

    private void handleDragging(int mouseX, int mouseY) {
        if (!mousePressed) {
            dragging = false;
            return;
        }

        boolean inArea = mouseX >= startX && mouseX < startX + screenX && mouseY >= startY && mouseY < startY + screenY;
        if (!inArea) return;

        if (!dragging) {
            dragging = true;
        } else {
            pX -= (mouseX - lastMouseX);
            pY -= (mouseY - lastMouseY);
            targetMapX = pX;
            targetMapY = pY;
            curMouseX = pX;
            curMouseY = pY;
        }
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    private void clampTargetMap() {
        targetMapX = MathHelper.clamp(targetMapX, boundsLeft, boundsRight - 1);
        targetMapY = MathHelper.clamp(targetMapY, boundsTop, boundsBottom - 1);
    }

    @Override
    public void tick() {
        curMouseX = pX;
        curMouseY = pY;
        float dx = targetMapX - pX;
        float dy = targetMapY - pY;
        if (dx * dx + dy * dy < 4f) {
            pX += dx;
            pY += dy;
        } else {
            pX += dx * 0.85f;
            pY += dy * 0.85f;
        }
    }

    private void drawBackground(DrawContext context, int mx, int my, int locX, int locY) {
        context.enableScissor(startX + 10, startY + 10, width - startX - 12, height - startY - 12);
        int bgU = (int) (locX / ZOOM);
        int bgV = (int) (locY / ZOOM);
        drawBackgroundTexture(context, startX - 2, startY - 2, bgU - 20, bgV, screenX + 4, screenY + 4);

        induced = null;
        for (Research research : researches) {
            int col = research.x() - locX;
            int row = research.y() - locY;
            if (col >= -24 && row >= -24 && col <= screenX && row <= screenY) {
                int centerX = startX + col + 8;
                int centerY = startY + row + 8;
                context.getMatrices().push();
                context.getMatrices().translate(centerX - ICON_OFFSET, centerY - ICON_OFFSET, 0);
                context.getMatrices().scale(1.4f, 1.4f, 1f);
                drawBookTexture(context, -3, -3, 75, PageGui.BOOK_HEIGHT + 9, 22, 22, 512, 512);
                if (research.icon() instanceof ItemStack stack) {
                    context.drawItem(stack, 0, 0);
                } else {
                    context.drawTexture((Identifier) research.icon(), -3, -3, 16, 16, 0, 0, 16, 16, 16, 16);
                }
                context.getMatrices().pop();

                if (mx >= startX && my >= startY && mx < startX + screenX && my < startY + screenY && mx >= (centerX - BG_OFFSET) && mx <= (centerX + BG_OFFSET) && my >= (centerY - BG_OFFSET) && my <= (centerY + BG_OFFSET)) {
                    induced = research;
                }
            }
        }
        context.disableScissor();
    }

    private void drawFrame(DrawContext context, int mx, int my) {
        RenderSystem.enableBlend();
        int delta = startX - 4, deltaT = startY - 4;
        int leftX = -2 + delta, topY = -2 + deltaT;
        int rightX = width - 20 - delta, bottomY = height - 20 - deltaT;

        for (int c = leftX + 17; c < rightX; c += 64) {
            int p = Math.min(64, rightX + 3 - c);
            if (p > 0) {
                drawBookTexture(context, c, topY + 3, PageGui.BOOK_WIDTH / 2 + 20, PageGui.BOOK_HEIGHT + 1, p, 16, 512, 512);
                drawBookTexture(context, c, bottomY + 3, PageGui.BOOK_WIDTH / 2 + 20, PageGui.BOOK_HEIGHT + 1, p, 16, 512, 512);
            }
        }
        for (int c = topY + 18; c < bottomY; c += 64) {
            int p = Math.min(64, bottomY + 3 - c);
            if (p > 0) {
                drawBookTexture(context, leftX + 2, c, PageGui.BOOK_WIDTH / 2, PageGui.BOOK_HEIGHT + 20, 16, p, 512, 512);
                drawBookTexture(context, rightX + 2, c, PageGui.BOOK_WIDTH / 2, PageGui.BOOK_HEIGHT + 20, 16, p, 512, 512);
            }
        }
        drawBookTexture(context, leftX, topY, PageGui.BOOK_WIDTH / 2, PageGui.BOOK_HEIGHT, 20, 20, 512, 512);
        drawBookTexture(context, leftX, bottomY, PageGui.BOOK_WIDTH / 2, PageGui.BOOK_HEIGHT, 20, 20, 512, 512);
        drawBookTexture(context, rightX, topY, PageGui.BOOK_WIDTH / 2, PageGui.BOOK_HEIGHT, 20, 20, 512, 512);
        drawBookTexture(context, rightX, bottomY, PageGui.BOOK_WIDTH / 2, PageGui.BOOK_HEIGHT, 20, 20, 512, 512);

        if (induced != null) {
            context.drawTooltip(textRenderer, List.of(Text.translatable("research." + induced.id().getNamespace() + "." + induced.id().getPath() + ".name")), mx, my);
        }
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (induced != null && induced.isUnlocked(player)) {
            client.setScreen(new PageGui(induced, pX, pY));
            return true;
        }
        mousePressed = true;
        lastMouseX = (int) mx;
        lastMouseY = (int) my;
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        mousePressed = false;
        dragging = false;
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static void drawBookTexture(DrawContext context, int x, int y, int u, int v, int w, int h, int tw, int th) {
        context.drawTexture(BookGui.BOOK, x, y, u, v, w, h, tw, th);
    }

    private static void drawBackgroundTexture(DrawContext context, int x, int y, int u, int v, int w, int h) {
        context.drawTexture(BookGui.BACKGROUND, x, y, u + 96, v + 128, w, h, 512, 512);
    }
}