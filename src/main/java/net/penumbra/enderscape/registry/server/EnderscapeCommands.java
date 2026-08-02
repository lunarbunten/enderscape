package net.penumbra.enderscape.registry.server;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.penumbra.enderscape.commands.GatewaysCommand;
import net.penumbra.enderscape.commands.KillEnderDragonCommand;
import net.penumbra.enderscape.commands.SkipSongCommand;
import net.penumbra.enderscape.commands.WarpCommands;

public class EnderscapeCommands {

    public static int failure(CommandSourceStack source, String error) {
        return sendFailure(source, "failure." + error);
    }

    public static int failure(CommandSourceStack source, String prefix, String error) {
        return sendFailure(source, prefix + ".failure." + error);
    }

    private static int sendFailure(CommandSourceStack source, String key) {
        source.sendFailure(Component.translatable("commands.enderscape." + key));
        return 0;
    }

    public static int success(CommandSourceStack source, String prefix, Object... arguments) {
        MutableComponent text = Component.translatable("commands.enderscape." + prefix + ".success", arguments);
        source.sendSuccess(() -> text, true);
        return 1;
    }

    public static int successWithCount(CommandSourceStack source, String prefix, int count) {
        return successWithCount(source, prefix, count, count);
    }

    public static int successWithCount(CommandSourceStack source, String prefix, int count, Object... arguments) {
        MutableComponent text = Component.translatable("commands.enderscape." + prefix + ".success." + (count == 1 ? "single" : "multiple"), arguments);
        source.sendSuccess(() -> text, true);
        return count;
    }

    static {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registries,
                environment
        ) -> dispatcher.register(
                Commands.literal("enderscape").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(GatewaysCommand.builder())
                        .then(KillEnderDragonCommand.builder())
                        .then(WarpCommands.RandomLocation.builder())
                        .then(WarpCommands.CenterIsland.builder())
                        .then(SkipSongCommand.builder())
        ));
    }
}