package net.penumbra.enderscape.mixin.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.penumbra.enderscape.entity.magnia.MagniaAffected;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow public ServerPlayer player;

    @Shadow private int aboveGroundTickCount;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;getMaximumFlyingTicks(Lnet/minecraft/world/entity/Entity;)I", ordinal = 0), method = "tickPlayer")
    private void Enderscape$tick1(CallbackInfoReturnable<Boolean> cir) {
        Enderscape$updateMagniaTicks();
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;getMaximumFlyingTicks(Lnet/minecraft/world/entity/Entity;)I", ordinal = 1), method = "tickPlayer")
    private void Enderscape$tick2(CallbackInfoReturnable<Boolean> cir) {
        Enderscape$updateMagniaTicks();
    }

    @Unique
    private void Enderscape$updateMagniaTicks() {
        Entity entity = player.getRootVehicle();

        if (MagniaAffected.wasMoved(entity)) {
            aboveGroundTickCount = 0;
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "handleInteract", cancellable = true)
    private void Enderscape$handleInteract(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(player)) info.cancel();
    }

    @WrapWithCondition(method = "handlePlayerCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/HasCustomInventoryScreen;openCustomInventoryScreen(Lnet/minecraft/world/entity/player/Player;)V"))
    private boolean Enderscape$cancelCustomInventory(HasCustomInventoryScreen vehicle, Player rider) {
        return !EnderscapeMobEffects.isStunned(player);
    }

    @ModifyExpressionValue(method = "handlePlayerInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundPlayerInputPacket;input()Lnet/minecraft/world/entity/player/Input;"))
    private Input Enderscape$cancelPlayerInput(Input original) {
        return EnderscapeMobEffects.isStunned(player) ? Input.EMPTY : original;
    }

    @Inject(
            method = "handleContainerClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;resetLastActionTime()V", shift = At.Shift.AFTER),
            cancellable = true
    )
    private void Enderscape$cancelContainerClick(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(player)) {
            player.containerMenu.sendAllDataToRemote();
            info.cancel();
        }
    }
}
