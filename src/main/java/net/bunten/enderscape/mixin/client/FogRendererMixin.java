package net.bunten.enderscape.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.math.Vector3f;

import net.bunten.enderscape.client.SkyInfo;
import net.bunten.enderscape.world.biomes.EnderscapeBiome;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Holder;
import net.minecraft.util.CubicSampler;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "setupColor", at = @At("RETURN"))
    private static void setupColor( Camera camera, float tickDelta, ClientLevel world, int i, float f, CallbackInfo info) {
        BiomeManager manager = world.getBiomeManager();
        Vec3 pos = camera.getPosition().subtract(2, 2, 2).scale(0.25);

        Vec3 nebulaColor = CubicSampler.gaussianSampleVec3(pos, (x, y, z) -> {
            Holder<Biome> holder = manager.getNoiseBiomeAtQuart(x, y, z);
            EnderscapeBiome biome = EnderscapeBiome.getBiomeAt(holder);
            int color = biome != null ? biome.getNebulaColor() : EnderscapeBiome.DEFAULT_NEBULA_COLOR;
            return Vec3.fromRGB24(color);
        });

        Vec3 nebulaAlphaColor = CubicSampler.gaussianSampleVec3(pos, (x, y, z) -> {
            Holder<Biome> holder = manager.getNoiseBiomeAtQuart(x, y, z);
            EnderscapeBiome biome = EnderscapeBiome.getBiomeAt(holder);
            float color = biome != null ? biome.getNebulaAlpha() : EnderscapeBiome.DEFAULT_NEBULA_ALPHA;
            return new Vec3(color, color, color);
        });

        Vec3 starColor = CubicSampler.gaussianSampleVec3(pos, (x, y, z) -> {
            Holder<Biome> holder = manager.getNoiseBiomeAtQuart(x, y, z);
            EnderscapeBiome biome = EnderscapeBiome.getBiomeAt(holder);
            int color = biome != null ? biome.getStarColor() : EnderscapeBiome.DEFAULT_STAR_COLOR;
            return Vec3.fromRGB24(color);
        });
        
        Vec3 starAlphaColor = CubicSampler.gaussianSampleVec3(pos, (x, y, z) -> {
            Holder<Biome> holder = manager.getNoiseBiomeAtQuart(x, y, z);
            EnderscapeBiome biome = EnderscapeBiome.getBiomeAt(holder);
            float color = biome != null ? biome.getStarAlpha() : EnderscapeBiome.DEFAULT_STAR_ALPHA;
            return new Vec3(color, color, color);
        });

        Vec3 lightColor = CubicSampler.gaussianSampleVec3(pos, (x, y, z) -> {
            Holder<Biome> holder = manager.getNoiseBiomeAtQuart(x, y, z);
            EnderscapeBiome biome = EnderscapeBiome.getBiomeAt(holder);
            int color = biome != null ? biome.getLightColor() : EnderscapeBiome.DEFAULT_LIGHT_COLOR;
            return Vec3.fromRGB24(color);
        });

        Vec3 lightIntensity = CubicSampler.gaussianSampleVec3(pos, (x, y, z) -> {
            Holder<Biome> holder = manager.getNoiseBiomeAtQuart(x, y, z);
            EnderscapeBiome biome = EnderscapeBiome.getBiomeAt(holder);
            float color = biome != null ? biome.getLightIntensity() : EnderscapeBiome.DEFAULT_LIGHT_INTENSITY;
            return new Vec3(color, color, color);
        });

        SkyInfo.nebula[0] = (float) nebulaColor.x();
        SkyInfo.nebula[1] = (float) nebulaColor.y();
        SkyInfo.nebula[2] = (float) nebulaColor.z();
        SkyInfo.nebula[3] = (float) nebulaAlphaColor.x();
        
        SkyInfo.stars[0] = (float) starColor.x();
        SkyInfo.stars[1] = (float) starColor.y();
        SkyInfo.stars[2] = (float) starColor.z();
        SkyInfo.stars[3] = (float) starAlphaColor.x();

        SkyInfo.lightColor = new Vector3f(lightColor);
        SkyInfo.lightIntensity = (float) lightIntensity.x();
    }
}