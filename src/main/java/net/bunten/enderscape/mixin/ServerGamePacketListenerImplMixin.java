package net.bunten.enderscape.mixin;

import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
        Entity entity = this.player.getRootVehicle();
        if (MagniaMoveable.is(entity) && MagniaMoveable.wasMovedByMagnia(entity)) {
            aboveGroundTickCount = 0;
        }
    }
}
