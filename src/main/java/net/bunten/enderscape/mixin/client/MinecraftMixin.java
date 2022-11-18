package net.bunten.enderscape.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.Level;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    static Minecraft instance;

    @Inject(method = "getSituationalMusic", at = @At("HEAD"), cancellable = true)
    public void getSituationalMusic(CallbackInfoReturnable<Music> info) {
        var player = instance.player;

        if (instance.screen instanceof WinScreen && !instance.getMusicManager().isPlayingMusic(Musics.CREDITS)) {
            instance.getSoundManager().stop();
        }

        if (player != null && instance.level.dimension() == Level.END && !instance.gui.getBossOverlay().shouldPlayMusic() && !(instance.screen instanceof WinScreen)) {
            info.setReturnValue(player.level.getBiome(player.blockPosition()).value().getBackgroundMusic().orElse(Musics.END));
        }
    }
}