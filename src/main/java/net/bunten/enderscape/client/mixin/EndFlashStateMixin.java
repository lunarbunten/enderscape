package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bunten.enderscape.Enderscape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EndFlashState;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Environment(EnvType.CLIENT)
@Mixin(EndFlashState.class)
public abstract class EndFlashStateMixin {

    @ModifyReturnValue(method = "getIntensity", at = @At("RETURN"))
    private float getFieldOfViewModifier(float original, float f) {
        return original / 2;
    }

    // only meatbags can code. wtf

    @Shadow
    private long flashSeed;
    @Shadow
    private int offset;
    @Shadow
    private int duration;
    @Shadow
    private float xAngle;
    @Shadow
    private float yAngle;

    private long nextFlashTime = 0L;
    private long flashStartTime = -1L; // ⬅ when the current flash started
    private final Random random = new Random();

    @Inject(method = "calculateFlashParameters", at = @At("HEAD"), cancellable = true)
    private void onCalculateFlashParameters(long l, CallbackInfo ci) {
        // ⬅ check if current flash is still active
        boolean flashActive = flashStartTime != -1 && l < (flashStartTime + duration);

        if (!flashActive && l >= nextFlashTime) {
            // ⬅ new flash can begin
            RandomSource randomSource = RandomSource.create(l);
            randomSource.nextFloat(); // consume like vanilla

            this.offset = Mth.randomBetweenInclusive(randomSource, 0, 200);
            this.duration = Mth.randomBetweenInclusive(randomSource, 100, Math.min(380, 600 - this.offset));
            this.xAngle = Mth.randomBetween(randomSource, -60.0F, 10.0F);
            this.yAngle = Mth.randomBetween(randomSource, -180.0F, 180.0F);
            this.flashSeed = l;

            this.flashStartTime = l; // ⬅ mark start of new flash
            this.nextFlashTime = l + 100L + random.nextInt(1901); // 100–2000 ticks
        }

        ci.cancel();
    }

    @ModifyReturnValue(method = "calculateIntensity", at = @At("RETURN"))
    private float modifyFlashCurve(float original, long l) {
        long m = l % 600L;

        if (m >= offset && m <= offset + duration) {
            float t = (float) (m - offset) / duration;
            return 0.5F * (1 + (float) Math.cos(t * Math.PI));
        }

        return 0.0F;
    }

    @Shadow
    private float intensity;
    @Shadow
    private float oldIntensity;

    @ModifyReturnValue(method = "shouldProduceSoundThisTick", at = @At("RETURN"))
    private boolean shouldProduceSoundThisTick (boolean original) {
        float threshold = 0.7f;
        return this.oldIntensity > threshold && this.intensity <= threshold;
    }
}

