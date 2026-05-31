package net.bunten.enderscape.sound;

import net.bunten.enderscape.network.ClientboundStructureChangedPayload;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
    Credit to FusionSwarly for developing the original functionality
    https://github.com/FusionSwarly/structure-music
 */
public class StructureMusicHandler {

    private static final Map<ServerPlayer, Integer> playerTimers = new HashMap<>();
    private static final Map<ServerPlayer, Identifier> playerStructures = new HashMap<>();

    public static final Identifier NONE = Identifier.withDefaultNamespace("none");

    private static int structureChangeTimer = 0;

    private static void tryStructureChange(ServerPlayer player, Identifier structure) {
        Identifier currentStructure = playerStructures.get(player);

        if (currentStructure == null || !currentStructure.equals(structure)) {
            playerStructures.put(player, structure);
            playerTimers.put(player, 60 * 20);
        }
    }

    private static Identifier getStructure(ServerLevel level, ServerPlayer player) {
        if (level != null && player != null) {
            Registry<Structure> registry = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
            List<Structure> structures = registry.stream().toList();

            for (Structure structure : structures) {
                if (structure == null) continue;

                if (level.structureManager().getStructureWithPieceAt(BlockPos.containing(player.position()), structure).isValid()) return registry.getKey(structure);
            }
        }

        return NONE;
    }

    private static void sendStructureToClient(ServerPlayer player, Identifier structure) {
        if (player != null) {
            ServerPlayNetworking.send(player, new ClientboundStructureChangedPayload(structure));
        }
    }

    private static void removeFromAll(ServerPlayer player) {
        sendStructureToClient(player, NONE);
        playerStructures.remove(player);
        playerTimers.remove(player);
    }

    static {
        ServerLevelEvents.UNLOAD.register((server, level) -> {
            for (ServerPlayer player : level.players()) {
                removeFromAll(player);
            }
        });

        ServerTickEvents.START_LEVEL_TICK.register((level) -> {
            if (!playerTimers.isEmpty()) {
                Iterator<Map.Entry<ServerPlayer, Integer>> iterator = playerTimers.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<ServerPlayer, Integer> entry = iterator.next();
                    int newTimer = entry.getValue() - 1;

                    if (newTimer <= 0) {
                        ServerPlayer player = entry.getKey();
                        Identifier queued = playerStructures.get(player);

                        if (player.isAlive() && getStructure(level, player).equals(queued)) {
                            sendStructureToClient(player, queued);
                        }

                        iterator.remove();
                    } else {
                        entry.setValue(newTimer);
                    }
                }
            }

            if (structureChangeTimer-- <= 0) {
                for (ServerPlayer player : level.getPlayers(LivingEntity::isAlive)) tryStructureChange(player, getStructure(level, player));
                structureChangeTimer = 240;
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            removeFromAll(handler.getPlayer());
        });

        ServerEntityEvents.ENTITY_UNLOAD.register((entity, level) -> {
            if (entity instanceof ServerPlayer player) removeFromAll(player);
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            removeFromAll(oldPlayer);
            sendStructureToClient(newPlayer, NONE);
        });
    }
}