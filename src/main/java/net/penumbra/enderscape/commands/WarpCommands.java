package net.penumbra.enderscape.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.server.EnderscapeCommands;
import net.penumbra.enderscape.registry.tag.EnderscapeBiomeTags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public final class WarpCommands {

    private static LiteralArgumentBuilder<CommandSourceStack> builder(String name, BiFunction<ServerPlayer, ServerLevel, TeleportTransition> destination) {
        return Commands.literal(name).executes(
                context -> teleport(
                        context.getSource(),
                        name,
                        context.getSource().getPlayer(),
                        destination
                ))
                .then(Commands.argument(
                        "players",
                        EntityArgument.players()
                ).executes(context -> teleport(
                        context.getSource(),
                        name,
                        EntityArgument.getPlayers(context, "players"),
                        destination
                )));
    }

    private static int teleport(CommandSourceStack source, String name, ServerPlayer player, BiFunction<ServerPlayer, ServerLevel, TeleportTransition> destination) {
        if (player == null) {
            return EnderscapeCommands.failure(source, "no_player");
        } else {
            return teleport(source, name, List.of(player), destination);
        }
    }

    private static int teleport(CommandSourceStack source, String name, Collection<ServerPlayer> players, BiFunction<ServerPlayer, ServerLevel, TeleportTransition> destination) {
        ServerLevel level = source.getServer().getLevel(Level.END);

        if (level == null) {
            return EnderscapeCommands.failure(source, "no_end");
        } else {
            List<ServerPlayer> teleported = new ArrayList<>();
            TeleportTransition transition = null;
            Component coordinates = Component.empty();

            for (ServerPlayer player : players) {
                if (transition == null) {
                    transition = destination.apply(player, level);

                    if (transition == null) break;

                    coordinates = coordinatesOf(transition.position());
                }

                player.teleport(transition);
                teleported.add(player);
            }

            if (teleported.isEmpty()) {
                return EnderscapeCommands.failure(source, "no_destination");
            } else if (teleported.size() == 1) {
                return EnderscapeCommands.successWithCount(source, name, 1, teleported.getFirst().getDisplayName(), coordinates);
            } else {
                return EnderscapeCommands.successWithCount(source, name, teleported.size(), teleported.size(), coordinates);
            }
        }
    }

    private static Component coordinatesOf(Vec3 position) {
        int x = Mth.floor(position.x());
        int y = Mth.floor(position.y());
        int z = Mth.floor(position.z());

        return ComponentUtils.wrapInSquareBrackets(
                Component.translatable("chat.coordinates", x, y, z)
        ).withStyle(style -> style.withColor(ChatFormatting.GREEN)
                .withClickEvent(new ClickEvent.SuggestCommand("/tp @s " + x + " " + y + " " + z))
                .withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.coordinates.tooltip")))
        );
    }

    public static final class RandomLocation {

        public static final String NAME = "random_location";

        private static final int MINIMUM_DISTANCE = 1000;
        private static final int MAXIMUM_DISTANCE = 3000;

        public static LiteralArgumentBuilder<CommandSourceStack> builder() {
            return WarpCommands.builder(NAME, RandomLocation::teleportTransition);
        }

        private static TeleportTransition teleportTransition(ServerPlayer player, ServerLevel level) {
            return new TeleportTransition(
                    level,
                    teleportPosition(player, level).getBottomCenter(),
                    Vec3.ZERO,
                    0,
                    0,
                    Relative.union(Relative.ROTATION, Relative.DELTA),
                    TeleportTransition.DO_NOTHING
            );
        }

        private static int randomDistance(RandomSource random) {
            int distance = random.nextInt(MINIMUM_DISTANCE, MAXIMUM_DISTANCE);
            return random.nextBoolean() ? distance : -distance;
        }

        private static BlockPos teleportPosition(ServerPlayer player, ServerLevel level) {
            boolean inEnd = player.level().dimension().equals(level.dimension());
            RandomSource random = level.getRandom();

            BlockPos randomOffset = (inEnd ? player.blockPosition() : BlockPos.ZERO).offset(
                    randomDistance(random),
                    0,
                    randomDistance(random)
            );

            level.getChunkAt(randomOffset);

            BlockPos heightmap = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, randomOffset);
            BlockPos position = heightmap;

            if (heightmap.getY() <= level.getMinY() || level.getBiome(heightmap).is(EnderscapeBiomeTags.VOID_BIOMES)) {
                position = new BlockPos(
                        heightmap.getX(),
                        (level.getMinY() + level.getMaxY()) / 2,
                        heightmap.getZ()
                );

                level.setBlock(
                        position.below(),
                        EnderscapeBlocks.END_LAMP.defaultBlockState(),
                        2
                );
            }

            return position;
        }
    }

    public static final class CenterIsland {

        public static final String NAME = "center_island";

        public static LiteralArgumentBuilder<CommandSourceStack> builder() {
            return WarpCommands.builder(NAME, CenterIsland::teleportTransition);
        }

        private static TeleportTransition teleportTransition(ServerPlayer player, ServerLevel level) {
            return ((EndPortalBlock) Blocks.END_PORTAL).getPortalDestination(level.getServer().overworld(), player, player.blockPosition());
        }
    }
}