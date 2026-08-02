package net.penumbra.enderscape.mixin.entity;

import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Armadillo.class)
public abstract class ArmadilloMixin {

    @Unique
    private final Armadillo armadillo = (Armadillo) (Object) this;

    @Inject(method = "rollUp", at = @At(value = "HEAD"), cancellable = true)
    public void Enderscape$cancelRollUp(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(armadillo)) info.cancel();
    }

    @Inject(method = "rollOut", at = @At(value = "HEAD"), cancellable = true)
    public void Enderscape$cancelRollOut(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(armadillo)) info.cancel();
    }
}