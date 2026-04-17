package ru.easynull.hemomancy.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.net.UpdateMageS2CPacket;

@Mixin(ServerPlayerEntity.class)
public final class ServerPlayerMixin {
    @Inject(method = "onSpawn", at = @At("TAIL"))
    private void syncData(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        MagePlayer.Data data = MagePlayer.of(player);
        ServerPlayNetworking.send(player, new UpdateMageS2CPacket(data.data));
    }
}
