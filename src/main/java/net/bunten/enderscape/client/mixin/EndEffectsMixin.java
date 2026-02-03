package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@OnlyIn(Dist.CLIENT)
@Mixin(DimensionSpecialEffects.EndEffects.class)
public abstract class EndEffectsMixin {

    @ModifyReturnValue(method = "getBrightnessDependentFogColor", at = @At("RETURN"))
    public Vec3 Enderscape$getBrightnessDependentFogColor(Vec3 original) {
        if (EnderscapeConfig.getInstance().skyboxUpdateEnabled) return original.scale(EnderscapeSkybox.gammaFactor());
        return original;
    }
}