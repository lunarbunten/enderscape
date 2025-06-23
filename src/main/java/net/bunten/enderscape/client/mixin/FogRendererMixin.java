package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.biome.util.SkyParameters;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.bunten.enderscape.util.RGBA;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "computeFogColor", at = @At("RETURN"))
    private void setupColor(Camera camera, float f, ClientLevel level, int i, float g, boolean bl, CallbackInfoReturnable<Vector4f> cir) {
        BiomeManager manager = level.getBiomeManager();
        Vec3 pos = camera.getPosition().subtract(2, 2, 2).scale(0.25);
        float gamma = EnderscapeSkybox.gammaFactor();

        EnderscapeSkybox.fogStartDensity = RGBA.sampleFloat(manager, pos, SkyParameters::fogStartDensity, SkyParameters.DEFAULT_FOG_START_DENSITY);
        EnderscapeSkybox.fogEndDensity = RGBA.sampleFloat(manager, pos, SkyParameters::fogEndDensity, SkyParameters.DEFAULT_FOG_END_DENSITY);
        EnderscapeSkybox.nebulaColor = RGBA.sampleVector4f(manager, pos, SkyParameters::nebulaRGBA, SkyParameters.DEFAULT_NEBULA_COLOR).mul(gamma, gamma, gamma, 1);
        EnderscapeSkybox.starColor = RGBA.sampleVector4f(manager, pos, SkyParameters::starRGBA, SkyParameters.DEFAULT_STAR_COLOR).mul(gamma, gamma, gamma, 1);
    }
}