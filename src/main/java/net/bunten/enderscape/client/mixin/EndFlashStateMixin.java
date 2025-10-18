package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.client.world.EndFlashParameters;
import net.minecraft.client.renderer.EndFlashState;
import net.minecraft.util.RandomSource;
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

    @WrapOperation(method = "calculateFlashParameters", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;randomBetweenInclusive(Lnet/minecraft/util/RandomSource;II)I", ordinal = 1))
    private int Enderscape$changeDuration(RandomSource random, int min, int max, Operation<Integer> original) {
        if (EndFlashParameters.updatedVisuals()) return original.call(random, min, max) * 3;
        return original.call(random, min, max);
    }

    @Inject(method = "calculateIntensity", at = @At("HEAD"), cancellable = true)
    public void Enderscape$changeIntensityCurve(long l, CallbackInfoReturnable<Float> info) {
        if (EndFlashParameters.updatedVisuals()) info.setReturnValue(EndFlashParameters.getIntensity(offset, duration, l % EndFlashParameters.frequencyInTicks()));
    }

    @ModifyConstant(method = "calculateIntensity", constant = @Constant(longValue = 600L))
    private long Enderscape$changeVanillaCurveFrequency(long original) {
        return EndFlashParameters.frequencyInTicks();
    }

    @ModifyReturnValue(method = "getIntensity", at = @At("RETURN"))
    public float Enderscape$disableFlashIntensity(float original) {
        if (!EndFlashParameters.enabled()) return 0.0F;
        return original;
    }

    @ModifyReturnValue(method = "flashStartedThisTick", at = @At("RETURN"))
    public boolean Enderscape$disableFlashSound(boolean original) {
        if (!EndFlashParameters.enabled()) return false;
        return original;
    }
}