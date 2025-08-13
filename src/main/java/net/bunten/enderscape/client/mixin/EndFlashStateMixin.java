package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.EndFlashState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(EndFlashState.class)
public class EndFlashStateMixin {

    @ModifyReturnValue(method = "getIntensity", at = @At("RETURN"))
    private float getFieldOfViewModifier(float original, float f) {
        return original / 2;
    }
}