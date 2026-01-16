package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.world.EndFlashParameters;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.Level;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    @ModifyReturnValue(method = "computeFogColor", at = @At("RETURN"))
    public Vector4f Enderscape$getBrightnessDependentFogColor(Vector4f original) {
        ClientLevel level = Minecraft.getInstance().level;

        if (level != null && level.dimension() == Level.END) {
            if (EnderscapeConfig.getInstance().skyboxUpdateEnabled) {
                float gamma = EnderscapeSkybox.gammaFactor();
                original = original.mul(gamma, gamma, gamma, 1.0F);
            }

            float brightness = EndFlashParameters.skyboxBrightness();
            return original.mul(brightness, brightness, brightness, 1.0F);
        }

        return original;
    }
}