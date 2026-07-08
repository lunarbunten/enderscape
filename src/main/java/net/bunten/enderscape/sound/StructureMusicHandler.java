package net.bunten.enderscape.sound;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.network.ClientboundStructureChangedPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
    Credit to FusionSwarly for developing the original functionality
    https://github.com/FusionSwarly/structure-music
 */
@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class StructureMusicHandler {

    private static final Map<ServerPlayer, Integer> playerTimers = new HashMap<>();
    private static final Map<ServerPlayer, ResourceLocation> playerStructures = new HashMap<>();

    private static int structureChangeTimer = 0;

    private static void tryStructureChange(ServerPlayer player, ResourceLocation structure) {
        ResourceLocation currentStructure = playerStructures.get(player);
        if (currentStructure == null || !currentStructure.equals(structure)) {
            playerStructures.put(player, structure);
            playerTimers.put(player, 60 * 20);
        }
    }

    private static ResourceLocation getStructure(ServerLevel level, ServerPlayer player) {
        HolderLookup.RegistryLookup<Structure> registry = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
        List<Holder.Reference<Structure>> structures = registry.listElements().toList();

        for (Holder.Reference<Structure> structure : structures) {
            if (structure == null) continue;
            if (level.structureManager().getStructureAt(BlockPos.containing(player.position()), structure.value()).isValid()) return structure.key().location();
        }

        return ResourceLocation.withDefaultNamespace("none");
    }

    @SubscribeEvent
    public static void onServerUnload(ServerStoppedEvent event) {
        playerStructures.clear();
        playerTimers.clear();
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            playerStructures.remove(player);
            playerTimers.remove(player);
        }
    }

    @SubscribeEvent
    public static void onLevelTickStart(LevelTickEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!playerTimers.isEmpty()) {
            Iterator<Map.Entry<ServerPlayer, Integer>> iterator = playerTimers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ServerPlayer, Integer> entry = iterator.next();
                int newTimer = entry.getValue() - 1;
                if (newTimer <= 0) {
                    ServerPlayer player = entry.getKey();
                    if (playerStructures.containsKey(player)) {
                        player.connection.send(new ClientboundStructureChangedPayload(playerStructures.get(player)));
                    }
                    iterator.remove();
                } else {
                    entry.setValue(newTimer);
                }
            }
        }

        if (structureChangeTimer-- <= 0) {
            for (ServerPlayer player : level.getPlayers(LivingEntity::isAlive)) {
                tryStructureChange(player, getStructure(level, player));
            }
            structureChangeTimer = 240;
        }
    }
}
