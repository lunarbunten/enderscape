package net.penumbra.enderscape.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.manager.DashJumpManager;
import net.penumbra.enderscape.manager.ElytraManager;
import net.penumbra.enderscape.manager.EndHavenManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Shadow
    private static boolean didNotMove(double d, double e, double f) {
        return false;
    }

    @Unique
    private final ServerPlayer player = (ServerPlayer) (Object) this;

    @Inject(at = @At("HEAD"), method = "jumpFromGround")
    public void Enderscape$jumpFromGround(CallbackInfo info) {
        DashJumpManager.tryApply(player);
    }

    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V",
                    ordinal = 0
            ),
            method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;"
    )
    private void Enderscape$sendEndHavenInvalidSpawnpoint(ServerGamePacketListenerImpl instance, Packet<?> packet, Operation<Void> original) {
        EndHavenManager.sendInvalidRespawnPayload(instance, packet, original);
    }

    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;getRespawnConfig()Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;"
            ),
            method = "findRespawnPositionAndUseSpawnBlock"
    )
    private static ServerPlayer.RespawnConfig Enderscape$changeRespawnConfig(ServerPlayer player, Operation<ServerPlayer.RespawnConfig> original) {
        return EndHavenManager.respawnConfigIfPending(player).orElse(original.call(player));
    }

    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;findRespawnAndUseSpawnBlock(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Z)Ljava/util/Optional;"),
            method = "findRespawnPositionAndUseSpawnBlock"
    )
    private Optional<ServerPlayer.RespawnPosAngle> Enderscape$useTemporarySpawnPoint(ServerLevel level, ServerPlayer.RespawnConfig config, boolean consumeSpawnBlock, Operation<Optional<ServerPlayer.RespawnPosAngle>> original) {
        return EndHavenManager.respawnPosition(level, config).or(() -> original.call(level, config, consumeSpawnBlock));
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/Identifier;I)V", ordinal = 7), index = 1, method = "checkMovementStatistics")
    public int Enderscape$applyGlidingExhaustion(int distance) {
        if (EnderscapeConfig.getInstance().elytraHungerExhaustion) {
            ElytraManager.applyGlidingExhaustion(player, distance);
        }
        return distance;
    }

    @Inject(at = @At("TAIL"), method = "checkMovementStatistics")
    public void Enderscape$checkMovementStatistics(double x, double y, double z, CallbackInfo ci) {
        DashJumpManager.tryAwardStatistics(player, didNotMove(x, y, z), x, z);
    }
}