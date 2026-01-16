package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/minecart/OldMinecartBehavior;moveAlongTrack(Lnet/minecraft/server/level/ServerLevel;)V"))
    public void tick(OldMinecartBehavior instance, ServerLevel level, Operation<Void> original) {
        if (!MagniaMoveable.wasMovedByMagnia(minecart)) original.call(instance, level);
    }
}