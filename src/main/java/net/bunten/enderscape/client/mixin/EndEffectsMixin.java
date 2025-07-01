package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.EndEffects.class)
public abstract class EndEffectsMixin {

    @Inject(method = "getBrightnessDependentFogColor", at = @At("HEAD"), cancellable = true)
    public void Enderscape$getBrightnessDependentFogColor(Vec3 vec3, float f, CallbackInfoReturnable<Vec3> info) {
        if (EnderscapeConfig.getInstance().skyboxUpdateEnabled) {
            info.setReturnValue(vec3.scale(EnderscapeSkybox.gammaFactor()));
        }
    }
}