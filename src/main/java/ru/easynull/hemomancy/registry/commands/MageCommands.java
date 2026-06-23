package ru.easynull.hemomancy.registry.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.net.UpdateMageS2CPacket;

public final class MageCommands {
    public static void onInit(CommandDispatcher<CommandSourceStack> dis) {
        dis.register(Commands.literal("hemomancy")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("level")
                        .then(Commands.literal("set").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("value", IntegerArgumentType.integer(0, 10)).executes(ctx -> set(ctx, EntityArgument.getPlayer(ctx, "player"), IntegerArgumentType.getInteger(ctx, "value"))))))
                        .then(Commands.literal("get").then(Commands.argument("player", EntityArgument.player()).executes(ctx -> get(ctx, EntityArgument.getPlayer(ctx, "player")))))));

    }

    private static int get(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        var data = MagePlayer.of(player);
        ctx.getSource().sendSuccess(()-> Component.translatable("message.hemomancy.level.get", player.getName(), data.getLevel()), false);
        return 1;
    }

    private static int set(CommandContext<CommandSourceStack> ctx, ServerPlayer player, int value) {
        var data = MagePlayer.of(player);
        data.setLevel(value);
        ServerPlayNetworking.send(player, new UpdateMageS2CPacket(data.data));
        ctx.getSource().sendSuccess(()-> Component.translatable("message.hemomancy.level.set", player.getName(), value), false);
        return 1;
    }
}
