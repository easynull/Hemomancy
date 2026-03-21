package ru.easynull.hemomancy.registry.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.net.UpdateMageS2CPacket;

public final class MageCommands {
    public static void onInit(CommandDispatcher<ServerCommandSource> dis) {
        dis.register(CommandManager.literal("hemomancy")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("level")
                        .then(CommandManager.literal("set").then(CommandManager.argument("player", EntityArgumentType.player()).then(CommandManager.argument("value", IntegerArgumentType.integer(0, 10)).executes(ctx -> set(ctx, EntityArgumentType.getPlayer(ctx, "player"), IntegerArgumentType.getInteger(ctx, "value"))))))
                        .then(CommandManager.literal("get").then(CommandManager.argument("player", EntityArgumentType.player()).executes(ctx -> get(ctx, EntityArgumentType.getPlayer(ctx, "player")))))));

    }

    private static int get(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player) {
        var data = MagePlayer.of(player);
        ctx.getSource().sendFeedback(()-> Text.translatable("message.hemomancy.level.get", player.getName(), data.getLevel()), false);
        return 1;
    }

    private static int set(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player, int value) {
        var data = MagePlayer.of(player);
        data.setLevel(value);
        ServerPlayNetworking.send(player, new UpdateMageS2CPacket(data.data));
        ctx.getSource().sendFeedback(()-> Text.translatable("message.hemomancy.level.set", player.getName(), value), false);
        return 1;
    }
}
