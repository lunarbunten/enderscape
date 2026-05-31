package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.world.EndFlashParameters;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.Level;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    @Inject(method = "computeFogColor", at = @At("RETURN"))
    public void Enderscape$getBrightnessDependentFogColor(Camera camera, float partialTicks, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f dest, CallbackInfo ci) {
        if (level != null && level.dimension() == Level.END) {
            if (EnderscapeConfig.getInstance().skyboxUpdateEnabled) {
                float gamma = EnderscapeSkybox.gammaFactor();
                dest.mul(gamma, gamma, gamma, 1.0F);
            }

            float brightness = EndFlashParameters.skyboxBrightness();
            dest.mul(brightness, brightness, brightness, 1.0F);
        }
    }
}