package net.penumbra.enderscape.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.penumbra.enderscape.network.ClientboundSkipSongPayload;
import net.penumbra.enderscape.registry.server.EnderscapeCommands;

public final class SkipSongCommand {

    public static final String NAME = "skip_song";

    public static LiteralArgumentBuilder<CommandSourceStack> builder() {
        return Commands.literal(NAME).executes(context -> execute(context.getSource()));
    }

    private static int execute(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();

        if (player == null) {
            return EnderscapeCommands.failure(source, "no_player");
        } else {
            ServerPlayNetworking.send(player, new ClientboundSkipSongPayload());
            return EnderscapeCommands.success(source, NAME);
        }
    }
}