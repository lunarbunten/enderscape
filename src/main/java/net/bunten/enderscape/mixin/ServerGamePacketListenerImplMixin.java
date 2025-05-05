package net.bunten.enderscape.mixin;

import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow public ServerPlayer player;

    @Shadow private int aboveGroundTickCount;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;getMaximumFlyingTicks(Lnet/minecraft/world/entity/Entity;)I"), method = "tick")
    private void E$tick(CallbackInfo ci) {
        Entity entity = this.player.getRootVehicle();
        if (MagniaMoveable.is(entity) && MagniaMoveable.wasMovedByMagnia(entity)) {
            aboveGroundTickCount = 0;
        }
    }

}
