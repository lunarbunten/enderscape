package net.penumbra.enderscape.mixin.entity.minecart;

import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.penumbra.enderscape.entity.magnia.MagniaAffected;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {

    @Unique
    private final AbstractMinecart self = (AbstractMinecart) (Object) this;

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void Enderscape$tick(CallbackInfo info) {
        MagniaAffected.tickCooldown(self);
    }
}