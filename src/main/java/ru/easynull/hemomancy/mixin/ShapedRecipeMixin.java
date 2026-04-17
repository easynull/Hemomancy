package ru.easynull.hemomancy.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.recipe.ShapedRecipe.getItem;

@Mixin(ShapedRecipe.class)
public final class ShapedRecipeMixin {
    @Inject(method = "outputFromJson", at = @At("HEAD"), cancellable = true)
    private static void readItemStack(JsonObject json, CallbackInfoReturnable<ItemStack> cir) {
        Item item = getItem(json);
        NbtCompound nbt = null;
        if (json.has("data")) {
            String nbtString = JsonHelper.getString(json, "data");
            try {
                NbtCompound element = NbtHelper.fromNbtProviderString(nbtString);
                if (element instanceof NbtCompound) {
                    nbt = element;
                } else {
                    throw new JsonSyntaxException("NBT must be a compound tag");
                }
            } catch (Exception e) {
                throw new JsonSyntaxException("Invalid NBT: " + nbtString, e);
            }
        }
        int count = JsonHelper.getInt(json, "count", 1);
        if (count < 1) {
            throw new JsonSyntaxException("Invalid output count: " + count);
        } else {
            ItemStack stack = new ItemStack(item, count);
            if (nbt != null) {
                stack.setNbt(nbt);
            }
            cir.setReturnValue(stack);
        }
    }
}
