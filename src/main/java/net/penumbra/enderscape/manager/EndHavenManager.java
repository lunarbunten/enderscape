package net.penumbra.enderscape.manager;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.block.EndHavenCoreBlock;
import net.penumbra.enderscape.network.ClientboundSystemMessagePayload;
import net.penumbra.enderscape.registry.entity.EnderscapeAttachments;
import net.penumbra.enderscape.registry.level.EnderscapeEnvironmentAttributes;

import java.util.Optional;

import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.PENDING_END_HAVEN_RESPAWN;

public class EndHavenManager {

    private static final ImmutableList<Vec3i> RESPAWN_HORIZONTAL_OFFSETS = ImmutableList.of(
            new Vec3i(0, 0, -1),
            new Vec3i(-1, 0, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(1, 0, 0),
            new Vec3i(-1, 0, -1),
            new Vec3i(1, 0, -1),
            new Vec3i(-1, 0, 1),
            new Vec3i(1, 0, 1)
    );

    private static final ImmutableList<Vec3i> RESPAWN_OFFSETS = new ImmutableList.Builder<Vec3i>()
            .addAll(RESPAWN_HORIZONTAL_OFFSETS)
            .addAll(RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::below).iterator())
            .addAll(RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::above).iterator())
            .add(new Vec3i(0, 1, 0))
            .build();

    public static boolean endHavenCoreWorks(final Level level) {
        return level.environmentAttributes().getDimensionValue(EnderscapeEnvironmentAttributes.END_HAVEN_CORE_WORKS).equals(true);
    }

    public static boolean promptHavenRespawnChoice(Player player) {
        return hasHavenRespawnData(player) && endHavenCoreWorks(player.level());
    }

    public static void setPendingRespawn(Player player) {
        player.setAttached(PENDING_END_HAVEN_RESPAWN, Unit.INSTANCE);
    }

    public static boolean shouldRespawnFromHaven(Player player) {
        return player.hasAttached(PENDING_END_HAVEN_RESPAWN) && hasHavenRespawnData(player);
    }

    public static void clearPendingRespawn(Player player) {
        player.removeAttached(PENDING_END_HAVEN_RESPAWN);
    }

    public static void sendInvalidRespawnPayload(ServerGamePacketListenerImpl instance, Packet<?> packet, Operation<Void> original) {
        ServerPlayer player = instance.getPlayer();

        if (shouldRespawnFromHaven(player)) {
            ServerPlayNetworking.send(player, new ClientboundSystemMessagePayload("block.enderscape.end_haven_core.invalid_respawn_point"));
        } else {
            original.call(instance, packet);
        }
    }

    public static void updateConnection(Entity entity) {
        if (entity instanceof ServerPlayer player && player.hasAttached(EnderscapeAttachments.END_HAVEN_RESPAWN_DATA) && !endHavenCoreWorks(player.level())) {
            player.removeAttached(EnderscapeAttachments.END_HAVEN_RESPAWN_DATA);
            player.removeAttached(PENDING_END_HAVEN_RESPAWN);

            ServerPlayNetworking.send(player, new ClientboundSystemMessagePayload("block.enderscape.end_haven_core.severed_respawn_point"));
        }
    }

    public static Optional<Vec3> findRespawnPosition(final EntityType<?> type, final CollisionGetter level, final BlockPos pos) {
        Optional<Vec3> safePosition = findRespawnPosition(type, level, pos, true);
        return safePosition.isPresent() ? safePosition : findRespawnPosition(type, level, pos, false);
    }

    public static Optional<ServerPlayer.RespawnPosAngle> respawnPosition(ServerLevel level, ServerPlayer.RespawnConfig config) {
        BlockPos pos = config.respawnData().pos();

        if (level.getBlockState(pos).getBlock() instanceof EndHavenCoreBlock && endHavenWorksAndEndHavenActive(level, Optional.of(config))) {
            return findRespawnPosition(EntityType.PLAYER, level, pos).map(safe -> ServerPlayer.RespawnPosAngle.of(safe, pos, 0.0F));
        } else {
            return Optional.empty();
        }
    }

    private static boolean endHavenWorksAndEndHavenActive(final ServerLevel level, final Optional<ServerPlayer.RespawnConfig> config) {
        if (endHavenCoreWorks(level) && !config.isEmpty()) {
            LevelData.RespawnData data = config.get().respawnData();
            ServerLevel respawnLevel = level.getServer().getLevel(data.dimension());

            if (respawnLevel != null) {
                BlockState state = respawnLevel.getBlockState(data.pos());
                return state.getBlock() instanceof EndHavenCoreBlock && EndHavenCoreBlock.isActive(state);
            }
        }

        return false;
    }

    public static Optional<ServerPlayer.RespawnConfig> respawnConfig(ServerPlayer player) {
        return havenRespawnData(player).map(data -> new ServerPlayer.RespawnConfig(data, false));
    }

    public static Optional<ServerPlayer.RespawnConfig> respawnConfigIfPending(ServerPlayer player) {
        return shouldRespawnFromHaven(player) ? respawnConfig(player) : Optional.empty();
    }

    public static Optional<LevelData.RespawnData> havenRespawnData(Player player) {
        return hasHavenRespawnData(player) ? Optional.of(player.getAttachedOrThrow(EnderscapeAttachments.END_HAVEN_RESPAWN_DATA)) : Optional.empty();
    }

    private static boolean hasHavenRespawnData(Player player) {
        return player.hasAttached(EnderscapeAttachments.END_HAVEN_RESPAWN_DATA);
    }

    private static Optional<Vec3> findRespawnPosition(final EntityType<?> type, final CollisionGetter level, final BlockPos pos, final boolean checkDangerous) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

        for (Vec3i offset : RESPAWN_OFFSETS) {
            blockPos.set(pos).move(offset);
            Vec3 position = DismountHelper.findSafeDismountLocation(type, level, blockPos, checkDangerous);
            if (position != null) {
                return Optional.of(position);
            }
        }

        return Optional.empty();
    }
}