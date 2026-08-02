package net.penumbra.enderscape.mixin.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WinScreen.class)
public class WinScreenMixin {

    @Inject(method = "<init>", at = @At("HEAD"))
    private static void Enderscape$cancelAllSounds(boolean poem, Runnable onFinished, CallbackInfo ci) {
        Minecraft.getInstance().getSoundManager().stop();
    }
}