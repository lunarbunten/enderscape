package net.bunten.enderscape.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "levelEvent", at = @At(value = "HEAD"), cancellable = true)
    public void levelEvent(int i, BlockPos pos, int j, CallbackInfo info) {
        if (i == 1032 && EnderscapeClient.playTransdimensionalSound) {
            info.cancel();
            minecraft.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(EnderscapeSounds.MIRROR_TRANSDIMENSIONAL_TRAVEL, 1, 0.38F));
            EnderscapeClient.playTransdimensionalSound = false;
        }
    }
}