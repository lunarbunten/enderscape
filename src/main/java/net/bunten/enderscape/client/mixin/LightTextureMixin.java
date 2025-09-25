package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.LightingStyle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(LightTexture.class)
public abstract class LightTextureMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private boolean Enderscape$shouldUpdateLighting(LightingStyle style) {
        return minecraft.level != null && minecraft.level.dimension() == Level.END && EnderscapeConfig.getInstance().lightingStyle == style;
    }

    @ModifyArgs(
            method = "updateLightTexture(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Vector3f;set(FFF)Lorg/joml/Vector3f;"
            )
    )
    private void changeBlockLightColor(Args args) {
        if (Enderscape$shouldUpdateLighting(LightingStyle.IMPROVED) || Enderscape$shouldUpdateLighting(LightingStyle.MIDNIGHT)) {
            float r = args.get(0);
            float g = args.get(1);
            float b = args.get(2);

            g = Mth.clamp(g - r * 0.1f, 0.0f, 1.0f);
            b = Mth.clamp(b + r * 0.16f, 0.0f, 1.0f);

            args.set(0, r);
            args.set(1, g);
            args.set(2, b);
        }
    }

    @WrapOperation(
            method = "updateLightTexture",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Vector3f;lerp(Lorg/joml/Vector3fc;F)Lorg/joml/Vector3f;",
                    ordinal = 1
            )
    )
    private Vector3f changeEndLighting(Vector3f instance, Vector3fc other, float t, Operation<Vector3f> original) {
        if (Enderscape$shouldUpdateLighting(LightingStyle.IMPROVED)) return instance.lerp(new Vector3f(0.93f, 1.1f, 0.93f), 0.1F);
        if (Enderscape$shouldUpdateLighting(LightingStyle.MIDNIGHT)) return instance.lerp(new Vector3f(0.93f, 1.1f, 0.93f), 0.01F);
        return original.call(instance, other, t);
    }

    @ModifyArgs(
            method = "updateLightTexture",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Vector3f;lerp(Lorg/joml/Vector3fc;F)Lorg/joml/Vector3f;",
                    ordinal = 5
            )
    )
    private void fixGamma(Args args) {
        if (Enderscape$shouldUpdateLighting(LightingStyle.IMPROVED) || Enderscape$shouldUpdateLighting(LightingStyle.MIDNIGHT)) {
            Float value = args.get(1);
            args.set(1, value * 0.75F);
        }
    }
}