package net.penumbra.enderscape.mixin.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.penumbra.enderscape.manager.EndHavenManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V",
                    ordinal = 0
            ),
            method = "respawn"
    )
    private void Enderscape$sendEndHavenInvalidSpawnpoint(ServerGamePacketListenerImpl instance, Packet<?> packet, Operation<Void> original) {
        EndHavenManager.sendInvalidRespawnPayload(instance, packet, original);

    }

    @Inject(at = @At(value = "RETURN"), method = "respawn")
    private void Enderscape$finishRespawn(ServerPlayer player, boolean keepAllPlayerData, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> info) {
        EndHavenManager.clearPendingRespawn(player);
    }
}