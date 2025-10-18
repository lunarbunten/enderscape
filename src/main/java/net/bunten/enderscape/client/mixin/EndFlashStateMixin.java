package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.client.renderer.EndFlashState;
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