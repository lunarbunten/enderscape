package net.bunten.enderscape.mixin;

import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartBehavior;
import net.minecraft.world.entity.vehicle.OldMinecartBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OldMinecartBehavior.class)
public abstract class OldMinecartBehaviorMixin extends MinecartBehavior {

    protected OldMinecartBehaviorMixin(AbstractMinecart abstractMinecart) {
        super(abstractMinecart);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void tick(CallbackInfo ci) {
        if (MagniaMoveable.wasMovedByMagnia(minecart) && minecart.level() instanceof ServerLevel serverLevel) {
            AbstractMinecartAccessor accessor = (AbstractMinecartAccessor) minecart;
            accessor.callComeOffTrack(serverLevel);
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/OldMinecartBehavior;moveAlongTrack(Lnet/minecraft/server/level/ServerLevel;)V"))
    public void tick(OldMinecartBehavior instance, ServerLevel level) {
        if (!MagniaMoveable.wasMovedByMagnia(minecart)) moveAlongTrack(level);
    }
}