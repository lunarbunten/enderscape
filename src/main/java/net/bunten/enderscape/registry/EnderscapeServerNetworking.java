package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeClientNetworking;
import net.bunten.enderscape.item.component.value.LodestoneTeleportationVisuals;
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

    public static void sendLodestoneTeleportationInfoPayload(ServerPlayer player, boolean isDifferentDimension, LodestoneTeleportationVisuals visuals) {
        player.connection.send( new ClientboundLodestoneTeleportationInfoPayload(isDifferentDimension, visuals.overlayTexture().asset().withPrefix("textures/").withSuffix(".png"), visuals.vignetteTexture().asset().withPrefix("textures/").withSuffix(".png")));
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

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToClient(ClientboundMagniaDataPayload.TYPE, ClientboundMagniaDataPayload.STREAM_CODEC, ClientboundMagniaDataPayload::handle);
        registrar.playToClient(ClientboundDashJumpDataPayload.TYPE, ClientboundDashJumpDataPayload.STREAM_CODEC, ClientboundDashJumpDataPayload::handle);
        registrar.playToClient(ClientboundEndTrialSpawnableDataPayload.TYPE, ClientboundEndTrialSpawnableDataPayload.STREAM_CODEC, ClientboundEndTrialSpawnableDataPayload::handle);

        registrar.playToClient(ClientboundDashJumpSoundPayload.TYPE, ClientboundDashJumpSoundPayload.STREAM_CODEC, EnderscapeClientNetworking::receiveDashJumpSoundPayload);
        registrar.playToClient(ClientboundDashJumpPayload.TYPE, ClientboundDashJumpPayload.STREAM_CODEC, EnderscapeClientNetworking::receiveDashJumpPayload);
        registrar.playToClient(ClientboundNebuliteOreSoundPayload.TYPE, ClientboundNebuliteOreSoundPayload.STREAM_CODEC, EnderscapeClientNetworking::receiveNebuliteOreSoundPayload);
        registrar.playToClient(ClientboundLodestoneTeleportationInfoPayload.TYPE, ClientboundLodestoneTeleportationInfoPayload.STREAM_CODEC, EnderscapeClientNetworking::receiveLodestoneTeleportationInfoPayload);
        registrar.playToClient(ClientboundRubbleShieldCooldownSoundPayload.TYPE, ClientboundRubbleShieldCooldownSoundPayload.STREAM_CODEC, EnderscapeClientNetworking::receiveRubbleShieldCooldownSoundPayload);
        registrar.playToClient(ClientboundStareOverlayPayload.TYPE, ClientboundStareOverlayPayload.STREAM_CODEC, EnderscapeClientNetworking::receiveStareOverlayPayload);
        registrar.playToClient(ClientboundStareSoundPayload.TYPE, ClientboundStareSoundPayload.STREAM_CODEC, EnderscapeClientNetworking::receiveStareSoundPayload);
        registrar.playToClient(ClientboundStructureChangedPayload.TYPE, ClientboundStructureChangedPayload.STREAM_CODEC, EnderscapeClientNetworking::receiveStructureChangedPayload);
        registrar.playToClient(ClientboundTransdimensionalTravelSoundPayload.TYPE, ClientboundTransdimensionalTravelSoundPayload.STREAM_CODEC, EnderscapeClientNetworking::receiveTransdimensionalTravelSoundPayload);
    }
}