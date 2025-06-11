package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeClientNetworking;
import net.bunten.enderscape.network.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.function.Predicate;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EnderscapeServerNetworking {

    public static void sendMirrorInfoPayload(ServerPlayer player, boolean isDifferentDimension) {
        player.connection.send(new ClientboundMirrorTeleportInfoPayload(isDifferentDimension));
    }

    public static void sendNebuliteOreSoundPayload(ServerLevel world, BlockPos pos) {
        Predicate<ServerPlayer> dimension = (player) -> player.level().dimension() == world.dimension() && player.blockPosition().closerThan(pos, 32);
        world.players().stream().filter((dimension)).forEach((player) -> player.connection.send(new ClientboundNebuliteOreSoundPayload(GlobalPos.of(world.dimension(), pos))));
    }

    public static void sendStareOverlayPayload(ServerPlayer player) {
        player.connection.send(new ClientboundStareOverlayPayload());
    }

    public static void sendStareSoundPayload(ServerPlayer player, int entityId) {
        player.connection.send(new ClientboundStareSoundPayload(entityId));
    }

    @SuppressWarnings("Convert2MethodRef")
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        // Use lambdas instead of method refs, as method refs will load methods in the target class that cannot be safely loaded
        registrar.playToClient(ClientboundDashJumpSoundPayload.TYPE, ClientboundDashJumpSoundPayload.STREAM_CODEC, (payload, ctx) -> EnderscapeClientNetworking.receiveDashJumpSoundPayload(payload, ctx));
        registrar.playToClient(ClientboundDashJumpPayload.TYPE, ClientboundDashJumpPayload.STREAM_CODEC, (payload, ctx) -> EnderscapeClientNetworking.receiveDashJumpPayload(payload, ctx));
        registrar.playToClient(ClientboundMirrorTeleportInfoPayload.TYPE, ClientboundMirrorTeleportInfoPayload.STREAM_CODEC, (payload, ctx) -> EnderscapeClientNetworking.receiveMirrorTeleportPayload(payload, ctx));
        registrar.playToClient(ClientboundNebuliteOreSoundPayload.TYPE, ClientboundNebuliteOreSoundPayload.STREAM_CODEC, (payload, ctx) -> EnderscapeClientNetworking.receiveNebuliteOreSoundPayload(payload, ctx));
        registrar.playToClient(ClientboundRubbleShieldCooldownSoundPayload.TYPE, ClientboundRubbleShieldCooldownSoundPayload.STREAM_CODEC, (payload, ctx) -> EnderscapeClientNetworking.receiveRubbleShieldCooldownSoundPayload(payload, ctx));
        registrar.playToClient(ClientboundStareOverlayPayload.TYPE, ClientboundStareOverlayPayload.STREAM_CODEC, (payload, ctx) -> EnderscapeClientNetworking.receiveStareOverlayPayload(payload, ctx));
        registrar.playToClient(ClientboundStareSoundPayload.TYPE, ClientboundStareSoundPayload.STREAM_CODEC, (payload, ctx) -> EnderscapeClientNetworking.receiveStareSoundPayload(payload, ctx));
        registrar.playToClient(ClientboundStructureChangedPayload.TYPE, ClientboundStructureChangedPayload.STREAM_CODEC, (payload, ctx) -> EnderscapeClientNetworking.receiveStructureChangedPayload(payload, ctx));
        registrar.playToClient(ClientboundTransdimensionalTravelSoundPayload.TYPE, ClientboundTransdimensionalTravelSoundPayload.STREAM_CODEC, (payload, ctx) -> EnderscapeClientNetworking.receiveTransdimensionalTravelSoundPayload(payload, ctx));
        
        registrar.playToClient(ClientboundMagniaDataPayload.TYPE, ClientboundMagniaDataPayload.STREAM_CODEC, (payload, ctx) -> ClientboundMagniaDataPayload.handle(payload, ctx));
        registrar.playToClient(ClientboundDashJumpDataPayload.TYPE, ClientboundDashJumpDataPayload.STREAM_CODEC, (payload, ctx) -> ClientboundDashJumpDataPayload.handle(payload, ctx));
        registrar.playToClient(ClientboundEndTrialSpawnableDataPayload.TYPE, ClientboundEndTrialSpawnableDataPayload.STREAM_CODEC, (payload, ctx) -> ClientboundEndTrialSpawnableDataPayload.handle(payload, ctx));
    }
}