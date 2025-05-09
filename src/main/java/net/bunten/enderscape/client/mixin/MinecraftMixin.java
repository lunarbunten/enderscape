package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    static Minecraft instance;

    @Inject(method = "getSituationalMusic", at = @At("HEAD"), cancellable = true)
    public void getSituationalMusic(CallbackInfoReturnable<Music> info) {
        if (instance.screen instanceof WinScreen && !instance.getMusicManager().isPlayingMusic(Musics.CREDITS)) instance.getSoundManager().stop();

        LocalPlayer player = instance.player;
        ClientLevel level = instance.level;

        if (player != null && level.dimension() == Level.END) {
            Biome biome = level.getBiome(player.blockPosition()).value();
            Optional<Music> optional = EnderscapeClient.structureMusic.isPresent() ? EnderscapeClient.structureMusic : biome.getBackgroundMusic();

            if (optional.isPresent()) {
                Music music = optional.get();
                info.setReturnValue(music);
            }
        }
    }
}