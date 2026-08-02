package net.penumbra.enderscape.registry.server;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.penumbra.enderscape.item.component.value.LodestoneTeleportationVisuals;
import net.penumbra.enderscape.manager.EndHavenManager;
import net.penumbra.enderscape.network.*;

import java.util.function.Predicate;

public class EnderscapeServerNetworking {

    public static void sendLodestoneTeleportationInfoPayload(ServerPlayer player, boolean isDifferentDimension, LodestoneTeleportationVisuals visuals) {
        ServerPlayNetworking.send(player, new ClientboundLodestoneTeleportationInfoPayload(isDifferentDimension, visuals.overlayTexture().asset().texturePath(), visuals.vignetteTexture().asset().texturePath()));
    }

    public static void sendNebuliteOreSoundPayload(ServerLevel world, BlockPos pos) {
        Predicate<ServerPlayer> dimension = (player) -> player.level().dimension() == world.dimension() && player.blockPosition().closerThan(pos, 32);
        world.players().stream().filter((dimension)).forEach((player) -> ServerPlayNetworking.send(player, new ClientboundNebuliteOreSoundPayload(GlobalPos.of(world.dimension(), pos))));
    }

    public static void sendTargetedByEndermanPayload(ServerPlayer player, int endermanId, Boolean playStereoStareSound) {
        ServerPlayNetworking.send(player, new ClientboundTargetedByEndermanPayload(endermanId, playStereoStareSound));
    }

    private static void receiveServerboundRespawnFromEndHavenPayload(ServerboundRespawnFromEndHavenPayload payload, ServerPlayNetworking.Context context) {
        MinecraftServer server = context.server();
        ServerPlayer player = context.player();

        server.execute(() -> EndHavenManager.setPendingRespawn(player));
    }

    private static void receiveServerboundSpawnTotemParticlesPayload(ServerboundSpawnTotemParticlesPayload payload, ServerPlayNetworking.Context context) {
        MinecraftServer server = context.server();
        ServerPlayer player = context.player();

        server.execute(() -> player.level().sendParticles(
                new ItemParticleOption(ParticleTypes.ITEM, Items.TOTEM_OF_UNDYING),
                player.getX(),
                player.getY(),
                player.getZ(),
                8,
                0.2,
                0.1,
                0.2,
                0.1
        ));
    }

    static {
        PayloadTypeRegistry.clientboundPlay().register(ClientboundDashJumpPayload.TYPE, ClientboundDashJumpPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundDashJumpSoundPayload.TYPE, ClientboundDashJumpSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundHealthVoidPurifySoundPayload.TYPE, ClientboundHealthVoidPurifySoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundLodestoneTeleportationInfoPayload.TYPE, ClientboundLodestoneTeleportationInfoPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundNebuliteOreSoundPayload.TYPE, ClientboundNebuliteOreSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundRubbleShieldCooldownSoundPayload.TYPE, ClientboundRubbleShieldCooldownSoundPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundSkipSongPayload.TYPE, ClientboundSkipSongPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundStructureChangedPayload.TYPE, ClientboundStructureChangedPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundSystemMessagePayload.TYPE, ClientboundSystemMessagePayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundTargetedByEndermanPayload.TYPE, ClientboundTargetedByEndermanPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientboundTransdimensionalTravelSoundPayload.TYPE, ClientboundTransdimensionalTravelSoundPayload.STREAM_CODEC);

        PayloadTypeRegistry.serverboundPlay().register(ServerboundRespawnFromEndHavenPayload.TYPE, ServerboundRespawnFromEndHavenPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ServerboundSpawnTotemParticlesPayload.TYPE, ServerboundSpawnTotemParticlesPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ServerboundRespawnFromEndHavenPayload.TYPE, EnderscapeServerNetworking::receiveServerboundRespawnFromEndHavenPayload);
        ServerPlayNetworking.registerGlobalReceiver(ServerboundSpawnTotemParticlesPayload.TYPE, EnderscapeServerNetworking::receiveServerboundSpawnTotemParticlesPayload);
    }
}