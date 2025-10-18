package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.client.renderer.EndFlashState;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndFlashState.class)
public abstract class EndFlashStateMixin {
    
    @Unique
    private static final EnderscapeConfig Enderscape$config = EnderscapeConfig.getInstance();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void Enderscape$disableFlashTick(long l, CallbackInfo info) {
        if (!Enderscape$config.flashEnabled) info.cancel();
    }

    @ModifyReturnValue(method = "getIntensity", at = @At("RETURN"))
    public float Enderscape$disableFlashIntensity(float original) {
        if (!Enderscape$config.flashEnabled) return 0.0F;
        return original;
    }

    @WrapOperation(method = "calculateFlashParameters", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;randomBetweenInclusive(Lnet/minecraft/util/RandomSource;II)I", ordinal = 1))
    private int Enderscape$changeDuration(RandomSource random, int min, int max, Operation<Integer> original) {
        long interval = Enderscape$config.flashFrequencyInTicks();

        double scale = 600L / (double) interval;

        int newMin = (int) (min * scale);
        int newMax = (int) (max * scale);

        return original.call(random, newMin, newMax);
    }

    @ModifyReturnValue(method = "flashStartedThisTick", at = @At("RETURN"))
    public boolean Enderscape$disableFlashSound(boolean original) {
        if (!Enderscape$config.flashEnabled) return false;
        return original;
    }

    @ModifyConstant(method = "calculateFlashParameters", constant = @Constant(longValue = 600L))
    private long Enderscape$changeFlashFrequency(long original) {
        return Enderscape$config.flashFrequencyInTicks();
    }

    @ModifyConstant(method = "calculateIntensity", constant = @Constant(longValue = 600L))
    private long Enderscape$changeFlashFrequencyAgain(long original) {
        return Enderscape$config.flashFrequencyInTicks();
    }
}