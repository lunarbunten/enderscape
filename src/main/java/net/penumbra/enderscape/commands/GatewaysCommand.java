package net.penumbra.enderscape.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import net.penumbra.enderscape.mixin.level.EnderDragonFightAccessor;
import net.penumbra.enderscape.registry.server.EnderscapeCommands;

public final class GatewaysCommand {

    public static final String NAME = "gateways";

    public static LiteralArgumentBuilder<CommandSourceStack> builder() {
        return Commands.literal(NAME).executes(
                context -> execute(
                        context.getSource(),
                        20
                ))
                .then(Commands.argument(
                        "count",
                        IntegerArgumentType.integer(1)
                ).executes(context -> execute(
                        context.getSource(),
                        IntegerArgumentType.getInteger(context, "count")
                )));
    }

    private static int execute(CommandSourceStack source, int count) {
        ServerLevel level = source.getServer().getLevel(Level.END);

        if (level == null) {
            return EnderscapeCommands.failure(source, "no_end");
        } else {
            EnderDragonFight fight = level.getDragonFight();

            if (fight == null) {
                return gatewaysFailure(source, "no_dragon_fight");
            } else {
                EnderDragonFightAccessor accessor = (EnderDragonFightAccessor) fight;
                int placed = Math.min(count, accessor.getGateways().size());

                for (int index = 0; index < placed; index++) {
                    accessor.callSpawnNewGateway();
                }

                if (placed == 0) {
                    return gatewaysFailure(source, "none");
                } else {
                    return gatewaysSuccess(source, placed);
                }
            }
        }
    }

    private static int gatewaysFailure(CommandSourceStack source, String string) {
        return EnderscapeCommands.failure(source, NAME, string);
    }

    private static int gatewaysSuccess(CommandSourceStack source, int placed) {
        return EnderscapeCommands.successWithCount(source, NAME, placed);
    }
}
