package ru.easynull.hemomancy.render.gui.book.element;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.net.UpdateNbtC2SPacket;
import ru.easynull.hemomancy.registry.items.RitualStaffItem;

import static ru.easynull.hemomancy.render.gui.book.BookGui.BOOK;

public final class RitualSetterElement implements PageElement {
    final Identifier ritualId;
    ButtonWidget button;

    public RitualSetterElement(Identifier ritualId) {
        this.ritualId = ritualId;
    }

    @Override
    public int getHeight(int maxWidth) {
        return MinecraftClient.getInstance().textRenderer.fontHeight + 10;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        if (button == null) {
            x = x + 18;
            this.button = ButtonWidget.builder(Text.translatable("message.hemomancy.ritual.bound"), b -> onPress(ritualId)).dimensions(x - 6, y, 100, 24).build();
        } else {
            button.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        onPress(ritualId);
        return true;
    }

    private static void onPress(Identifier id) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        boolean find = false;
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);
            if (stack != null && stack.getItem() instanceof RitualStaffItem) {
                NbtCompound nbt = stack.getOrCreateNbt();
                nbt.putString("RitualId", id.toString());
                ClientPlayNetworking.send(new UpdateNbtC2SPacket(slot, nbt));
                find = true;
            }
        }

        if (find) {
            player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f);
        }
    }
}