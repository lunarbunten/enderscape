package net.penumbra.enderscape.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.penumbra.enderscape.registry.server.EnderscapeCommands;

import java.util.List;

public final class KillEnderDragonCommand {

    public static final String NAME = "kill_ender_dragon";

    public static LiteralArgumentBuilder<CommandSourceStack> builder() {
        return Commands.literal(NAME).executes(context -> execute(context.getSource()));
    }

    private static int execute(CommandSourceStack source) {
        ServerLevel level = source.getLevel();

        List<? extends EnderDragon> dragons = level.getEntities(EntityType.ENDER_DRAGON, EnderDragon::isAlive);

        if (dragons.isEmpty()) {
            return EnderscapeCommands.failure(source, NAME, "none");
        } else {
            for (EnderDragon dragon : dragons) dragon.kill(level);
            return EnderscapeCommands.successWithCount(source, NAME, dragons.size());
        }
    }
}