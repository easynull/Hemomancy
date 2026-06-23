package ru.easynull.hemomancy.render.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.api.mage.quest.task.Task;

public final class MageStatueGui extends Screen {
    private static float pY;
    final Player player;

    public MageStatueGui(Player player) {
        super(Component.empty());
        this.player = player;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        var data = MagePlayer.of(player);
        drawQuest(data, context, mouseX, mouseY, delta);
        drawMageTree(data, context, mouseX, mouseY, delta);
    }

    private void drawQuest(MagePlayer.Data data, GuiGraphics context, int mouseX, int mouseY, float delta){
        var quest = data.getCurrentQuest();
        if (quest != null) {
            var textRenderer = minecraft.font;
            int x = 10;
            int bottomY = height - 10;
            int lineHeight = 12;

            Component title = Component.translatable("quest." + quest.id().getNamespace() + "." + quest.id().getPath() + ".name");

            int maxWidth = textRenderer.width(title);
            for (Task task : quest.tasks()) {
                maxWidth = Math.max(maxWidth, textRenderer.width(task.getDescription()));
            }
            int cardWidth = maxWidth + 20;
            int cardHeight = lineHeight * (quest.tasks().size() + 1) + 10;
            int topY = bottomY - cardHeight;
            int rightX = x + cardWidth;

            context.fill(x - 5, topY - 5, rightX + 5, bottomY + 5, 0xAA000000);

            context.drawString(textRenderer, title, x, topY, 0xFFFFFF, false);

            int y = topY + lineHeight;
            for (Task task : quest.tasks()) {
                context.drawString(textRenderer, task.getDescription(), x + 10, y, 0xCCCCCC, false);
                y += lineHeight;
            }
        }
    }

    private void drawMageTree(MagePlayer.Data data, GuiGraphics context, int mouseX, int mouseY, float delta){
        int lvl = data.getLevel();
        int y = (int) (pY + (int) (height / 2.5f)), yL = (int) (pY + (int) (height / 2.5f));
        int centerX = (int) (width / 2f);
        for(short l = 0; l <= 10; l++){
            context.vLine(centerX, y, yL, 0xFFFFFFFF);
            yL -= 40;
        }
        for(short l = 0; l <= 10; l++){
            int color = 0xFFFF0000;
            if(l <= lvl){
                color = 0xFF00FF00;
            }
            context.fill(centerX - 11, y - 11, centerX + 11, y + 11, color);
            context.drawCenteredString(font, String.valueOf(l), centerX, y - 4, 0xFFFFFF);
            context.renderOutline(centerX - 12, y - 12, 24, 24, 0xFFFFFFFF);
            y -= 40;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount, double g) {
        pY += (float) amount * 14f;
        return super.mouseScrolled(mouseX, mouseY, amount, g);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        pY += (float) deltaY;
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}