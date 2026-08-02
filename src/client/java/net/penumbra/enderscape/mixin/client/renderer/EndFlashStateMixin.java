package net.penumbra.enderscape.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.EndFlashState;
import net.penumbra.enderscape.renderer.value.EndFlashParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndFlashState.class)
public abstract class EndFlashStateMixin {

    @Shadow
    private int offset;

    @Shadow
    private int duration;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void Enderscape$disableFlashTick(long l, CallbackInfo info) {
        if (!EndFlashParameters.enabled()) info.cancel();
    }

    @ModifyConstant(method = "calculateFlashParameters", constant = @Constant(longValue = 600L))
    private long Enderscape$changeFlashFrequency(long original) {
        return EndFlashParameters.frequencyInTicks();
    }

    @ModifyExpressionValue(
            method = "calculateFlashParameters",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;randomBetweenInclusive(Lnet/minecraft/util/RandomSource;II)I",
                    ordinal = 1
            )
    )
    private int changeDuration(int original) {
        return EndFlashParameters.improved() ? original * 3 : original;
    }

    @Inject(method = "calculateIntensity", at = @At("HEAD"), cancellable = true)
    public void Enderscape$changeIntensityCurve(long clockTime, CallbackInfoReturnable<Float> info) {
        if (EndFlashParameters.improved()) {
            long remainder = clockTime % EndFlashParameters.frequencyInTicks();
            if (remainder < 0) remainder += EndFlashParameters.frequencyInTicks();
            info.setReturnValue(EndFlashParameters.getIntensity(offset, duration, remainder));
        }
    }

    @ModifyConstant(method = "calculateIntensity", constant = @Constant(longValue = 600L))
    private long Enderscape$changeVanillaCurveFrequency(long original) {
        return EndFlashParameters.frequencyInTicks();
    }

    @ModifyReturnValue(method = "getIntensity", at = @At("RETURN"))
    public float Enderscape$disableFlashIntensity(float original) {
        return EndFlashParameters.enabled() ? original : 0.0F;
    }

    @ModifyReturnValue(method = "flashStartedThisTick", at = @At("RETURN"))
    public boolean Enderscape$disableFlashSound(boolean original) {
        return EndFlashParameters.enabled() && original;
    }
}