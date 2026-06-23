//package ru.easynull.hemomancy.render.gui.book.element;
//
//import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.core.component.DataComponents;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import ru.easynull.hemomancy.Hemomancy;
//import ru.easynull.hemomancy.net.UpdateNbtC2SPacket;
//import ru.easynull.hemomancy.registry.items.RitualStaffItem;
//
//import static ru.easynull.hemomancy.render.gui.book.BookGui.BOOK;
//
//public final class RitualSetterElement implements PageElement {
//    final ResourceLocation ritualId;
//    Button button;
//
//    public RitualSetterElement(ResourceLocation ritualId) {
//        this.ritualId = ritualId;
//    }
//
//    @Override
//    public int getHeight(int maxWidth) {
//        return Minecraft.getInstance().font.lineHeight + 10;
//    }
//
//    @Override
//    public void render(GuiGraphics context, int x, int y, int mouseX, int mouseY, float delta) {
//        if (button == null) {
//            x = x + 18;
//            this.button = Button.builder(Component.translatable("message.hemomancy.ritual.bound"), b -> onPress(ritualId)).bounds(x - 6, y, 100, 24).build();
//        } else {
//            button.render(context, mouseX, mouseY, delta);
//        }
//    }
//
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        onPress(ritualId);
//        return true;
//    }
//
//    private static void onPress(ResourceLocation id) {
//        Player player = Minecraft.getInstance().player;
//        boolean find = false;
//        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
//            ItemStack stack = player.getInventory().getItem(slot);
//            if (stack != null && stack.getItem() instanceof RitualStaffItem) {
//                CompoundTag nbt = stack.get(DataComponents.CUSTOM_DATA).copyTag();
//                nbt.putString("RitualId", id.toString());
//                ClientPlayNetworking.send(new UpdateNbtC2SPacket(slot, nbt));
//                find = true;
//            }
//        }
//
//        if (find) {
//            player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1f, 1f);
//        }
//    }
//}