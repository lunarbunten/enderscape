package net.penumbra.enderscape.util;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.LevelAccessor;

public class ClientsideLightUtil {

    public static int scaledLight(float factor) {
        return (15 << 20) | ((int) (15 * factor) << 4);
    }

    public static int lightCoordsAt(LevelAccessor level, BlockPos pos) {
        return effectiveLightCoords(LevelRenderer.getLightCoords(level, pos), level.environmentAttributes().getValue(EnvironmentAttributes.SKY_LIGHT_FACTOR, pos));
    }

    public static float lightBrightnessAt(LevelAccessor level, BlockPos pos) {
        return lightBrightness(LevelRenderer.getLightCoords(level, pos), level.environmentAttributes().getValue(EnvironmentAttributes.SKY_LIGHT_FACTOR, pos));
    }

    public static float lightBrightness(int lightCoords, float skyLightFactor) {
        int packed = effectiveLightCoords(lightCoords, skyLightFactor);
        int block = (packed >> 4)  & 0xF;
        int sky = (packed >> 20) & 0xF;
        return Math.max(block / 15.0F, sky / 15.0F);
    }

    public static int effectiveLightCoords(int lightCoords, float skyLightFactor) {
        int block = (lightCoords >> 4) & 0xF;
        int sky = (lightCoords >> 20) & 0xF;
        int finalSky = (int) (sky * skyLightFactor);
        return (finalSky << 20) | (block << 4);
    }
}