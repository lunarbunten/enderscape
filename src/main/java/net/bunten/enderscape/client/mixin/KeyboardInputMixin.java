package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void Enderscape$tick(CallbackInfo info) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && !player.isSpectator() && EnderscapeMobEffects.isStunned(player)) info.cancel();
    }
}