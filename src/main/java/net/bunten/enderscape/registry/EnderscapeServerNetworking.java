package net.bunten.enderscape.registry;

import net.bunten.enderscape.network.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Predicate;

public class EnderscapeServerNetworking {

    public static void sendMirrorInfoPayload(ServerPlayer player, boolean isDifferentDimension) {
        ServerPlayNetworking.send(player, new ClientboundMirrorTeleportInfoPayload(isDifferentDimension));
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
        PayloadTypeRegistry.playS2C().register(ClientboundDashJumpSoundPayload.TYPE, ClientboundDashJumpSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClientboundDashJumpPayload.TYPE, ClientboundDashJumpPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClientboundMirrorTeleportInfoPayload.TYPE, ClientboundMirrorTeleportInfoPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClientboundNebuliteOreSoundPayload.TYPE, ClientboundNebuliteOreSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClientboundRubbleShieldCooldownSoundPayload.TYPE, ClientboundRubbleShieldCooldownSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClientboundStareOverlayPayload.TYPE, ClientboundStareOverlayPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClientboundStareSoundPayload.TYPE, ClientboundStareSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClientboundStructureChangedPayload.TYPE, ClientboundStructureChangedPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClientboundTransdimensionalTravelSoundPayload.TYPE, ClientboundTransdimensionalTravelSoundPayload.STREAM_CODEC);
    }
}