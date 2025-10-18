package net.bunten.enderscape.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.biome.util.BiomeParameters;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.bunten.enderscape.util.RGBA;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = FogRenderer.class, priority = 2000)
public class FogRendererMixin {

    @Inject(method = "setupColor", at = @At("RETURN"))
    private static void setupColor(Camera camera, float f, ClientLevel level, int i, float g, CallbackInfo ci) {
        BiomeManager manager = level.getBiomeManager();
        Vec3 pos = camera.getPosition().subtract(2, 2, 2).scale(0.25);
        float gamma = EnderscapeSkybox.gammaFactor();

        EnderscapeSkybox.fogStartDensity = RGBA.sampleFloat(manager, pos, BiomeParameters::fogStartDensity, BiomeParameters.DEFAULT_FOG_START_DENSITY);
        EnderscapeSkybox.fogEndDensity = RGBA.sampleFloat(manager, pos, BiomeParameters::fogEndDensity, BiomeParameters.DEFAULT_FOG_END_DENSITY);
        EnderscapeSkybox.nebulaColor = RGBA.sampleVector4f(manager, pos, BiomeParameters::nebulaRGBA, BiomeParameters.DEFAULT_NEBULA_COLOR).mul(gamma, gamma, gamma, 1);
        EnderscapeSkybox.starColor = RGBA.sampleVector4f(manager, pos, BiomeParameters::starRGBA, BiomeParameters.DEFAULT_STAR_COLOR).mul(gamma, gamma, gamma, 1);
        EnderscapeSkybox.flashColor = RGBA.sampleVector4f(manager, pos, BiomeParameters::flashRGBA, BiomeParameters.DEFAULT_FLASH_COLOR);
    }

    @Inject(at = @At("RETURN"), method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V")
    private static void afterSetupFog(Camera camera, FogRenderer.FogMode mode, float viewDistance, boolean thickFog, float partialTick, CallbackInfo info) {
        ClientLevel level = Minecraft.getInstance().level;

        if (EnderscapeConfig.getInstance().skyboxAddDynamicFogDensity && level != null && level.dimension() == Level.END && mode == FogRenderer.FogMode.FOG_TERRAIN && !thickFog) {
            float fogStart = (viewDistance * 30.0F * 0.01F) / EnderscapeSkybox.fogStartDensity, fogEnd = viewDistance * 95.0F * 0.01F / EnderscapeSkybox.fogEndDensity;

            RenderSystem.setShaderFogStart(fogStart);
            RenderSystem.setShaderFogEnd(fogEnd);
        }
    }
}