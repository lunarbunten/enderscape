package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void Enderscape$tick(CallbackInfo info) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && !player.isSpectator() && EnderscapeMobEffects.isStunned(player)) info.cancel();
    }
}