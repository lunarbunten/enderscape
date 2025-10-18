package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(DimensionSpecialEffects.EndEffects.class)
public abstract class EndEffectsMixin {

    @ModifyReturnValue(method = "getBrightnessDependentFogColor", at = @At("RETURN"))
    public Vec3 Enderscape$getBrightnessDependentFogColor(Vec3 original) {
        if (EnderscapeConfig.getInstance().skyboxUpdateEnabled) original = original.scale(EnderscapeSkybox.gammaFactor());
        return original.scale(EnderscapeSkybox.getSkyboxLightScale());
    }
}