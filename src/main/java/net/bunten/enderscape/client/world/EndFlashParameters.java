package net.bunten.enderscape.client.world;

import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.joml.Vector3f;

public class EndFlashParameters {

    public static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    public static boolean enabled() {
        return EnderscapeConfig.getInstance().flashEnabled;
    }

    public static boolean updatedVisuals() {
        return EnderscapeConfig.getInstance().flashUpdatedVisuals;
    }

    public static long frequencyInTicks() {
        return Math.max(1L, (long) (CONFIG.flashFrequency * 60F * 20F));
    }

    public static Vector3f getSkyLightColor(Minecraft minecraft) {
        Camera camera = minecraft.gameRenderer.mainCamera();
        DeltaTracker tracker = minecraft.getDeltaTracker();
        int color = camera.attributeProbe().getValue(EnvironmentAttributes.SKY_LIGHT_COLOR, tracker.getGameTimeDeltaPartialTick(false));

        return ARGB.vector3fFromRGB24(color);
    }

    public static float getIntensity(int offset, int duration, long remainder) {
        if (remainder < offset || remainder > offset + duration) {
            return 0.0F;
        } else {
            return intensityFromCurve(offset, duration, remainder);
        }
    }

    private static float intensityFromCurve(int offset, int duration, long remainder) {
        float attack = 0.03F;
        float sustain = 0.15F;
        float decay = 0.9F;
        float intensity;

        float time = (float) (remainder - offset) / duration;
        time = Mth.clamp(time, 0.0F, 1.0F);

        if (time < attack) {
            intensity = time / attack;
        } else if (time < attack + sustain) {
            intensity = 1.0F;
        } else {
            float progress = (time - attack - sustain) / decay;
            intensity = (float) Math.exp(-5.0 * progress);
        }

        return Mth.clamp(intensity, 0.0F, 1.0F);
    }

    public static float skyboxBrightness() {
        Minecraft client = Minecraft.getInstance();
        ClientLevel level = client.level;

        float value = 1.0F;

        if (level != null && EnderscapeConfig.getInstance().flashInfluencesSkybox) {
            float intensity = level.endFlashState() != null ? level.endFlashState().getIntensity(client.getDeltaTracker().getGameTimeDeltaPartialTick(false)) : 0;
            value -= (intensity / 2);
        }

        return value;
    }
}
