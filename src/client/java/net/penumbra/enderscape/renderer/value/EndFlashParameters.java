package net.penumbra.enderscape.renderer.value;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.config.value.EndFlashStyle;
import org.joml.Vector3f;

public class EndFlashParameters {

    public static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    public static boolean enabled() {
        return !EnderscapeConfig.getInstance().endFlashStyle.equals(EndFlashStyle.DISABLED);
    }

    public static boolean improved() {
        return EnderscapeConfig.getInstance().endFlashStyle.equals(EndFlashStyle.IMPROVED);
    }

    public static long frequencyInTicks() {
        return Math.max(1L, (long) (CONFIG.endFlashFrequency * 60F * 20F));
    }

    public static Vector3f getSkyLightColor() {
        Minecraft minecraft = Minecraft.getInstance();

        Camera camera = minecraft.gameRenderer.getMainCamera();
        DeltaTracker tracker = minecraft.getDeltaTracker();
        int color = camera.attributeProbe().getValue(EnvironmentAttributes.SKY_LIGHT_COLOR, tracker.getGameTimeDeltaPartialTick(false));

        return ARGB.vector3fFromRGB24(color);
    }

    public static float getIntensity(int offset, int duration, long remainder) {
        if (remainder < offset || remainder >= offset + duration) {
            return 0.0F;
        } else {
            return intensityFromCurve(offset, duration, remainder);
        }
    }

    private static float intensityFromCurve(int offset, int duration, long remainder) {
        float attack = 0.03F;
        float sustain = 0.15F;
        float intensity;

        float time = (float) (remainder - offset) / duration;
        time = Mth.clamp(time, 0.0F, 1.0F);

        if (time < attack) {
            intensity = time / attack;
        } else if (time < attack + sustain) {
            intensity = 1.0F;
        } else {
            float decayStart = attack + sustain;
            float progress = (time - decayStart) / (1.0F - decayStart);
            intensity = (float) Math.exp(-5.0 * progress);
        }

        return Mth.clamp(intensity, 0.0F, 1.0F);
    }

    public static float skyboxBrightness() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;

        float value = 1.0F;

        if (level != null && EnderscapeConfig.getInstance().endFlashInfluencesSkybox) {
            float intensity = level.endFlashState() != null ? level.endFlashState().getIntensity(minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false)) : 0;
            value -= (intensity / 2);
        }

        return value;
    }
}
