package net.penumbra.enderscape.mixin.entity.minecart;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import net.penumbra.enderscape.entity.magnia.MagniaAffected;
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
    public void Enderscape$tick(CallbackInfo info) {
        if (MagniaAffected.wasMoved(minecart) && minecart.level() instanceof ServerLevel server) {
            AbstractMinecartAccessor accessor = (AbstractMinecartAccessor) minecart;
            accessor.callComeOffTrack(server);
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/minecart/OldMinecartBehavior;moveAlongTrack(Lnet/minecraft/server/level/ServerLevel;)V"))
    public void Enderscape$tick(OldMinecartBehavior instance, ServerLevel level, Operation<Void> original) {
        if (!MagniaAffected.wasMoved(minecart)) original.call(instance, level);
    }
}