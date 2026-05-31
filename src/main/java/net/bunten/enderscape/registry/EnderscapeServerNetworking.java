package net.bunten.enderscape.registry;

import net.bunten.enderscape.item.component.value.LodestoneTeleportationVisuals;
import net.bunten.enderscape.network.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Predicate;

public class EnderscapeServerNetworking {

    public static void sendLodestoneTeleportationInfoPayload(ServerPlayer player, boolean isDifferentDimension, LodestoneTeleportationVisuals visuals) {
        ServerPlayNetworking.send(player, new ClientboundLodestoneTeleportationInfoPayload(isDifferentDimension, visuals.overlayTexture().asset().texturePath(), visuals.vignetteTexture().asset().texturePath()));
    }

    public static void sendNebuliteOreSoundPayload(ServerLevel world, BlockPos pos) {
        Predicate<ServerPlayer> dimension = (player) -> player.level().dimension() == world.dimension() && player.blockPosition().closerThan(pos, 32);
        world.players().stream().filter((dimension)).forEach((player) -> ServerPlayNetworking.send(player, new ClientboundNebuliteOreSoundPayload(GlobalPos.of(world.dimension(), pos))));
    }

    public static void sendStareOverlayPayload(ServerPlayer player) {
        ServerPlayNetworking.send(player, new ClientboundStareOverlayPayload());
    }

    public static void sendStareSoundPayload(ServerPlayer player, int entityId) {
        ServerPlayNetworking.send(player, new ClientboundStareSoundPayload(entityId));
    }

    static {
        PayloadTypeRegistry.clientboundPlay().register(ClientboundDashJumpSoundPayload.TYPE, ClientboundDashJumpSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundDashJumpPayload.TYPE, ClientboundDashJumpPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundLodestoneTeleportationInfoPayload.TYPE, ClientboundLodestoneTeleportationInfoPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundNebuliteOreSoundPayload.TYPE, ClientboundNebuliteOreSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundRubbleShieldCooldownSoundPayload.TYPE, ClientboundRubbleShieldCooldownSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundStareOverlayPayload.TYPE, ClientboundStareOverlayPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundStareSoundPayload.TYPE, ClientboundStareSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundStructureChangedPayload.TYPE, ClientboundStructureChangedPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundTransdimensionalTravelSoundPayload.TYPE, ClientboundTransdimensionalTravelSoundPayload.STREAM_CODEC);
    }
}