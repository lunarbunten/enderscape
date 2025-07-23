package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.sounds.MusicInfo;
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
    public void getSituationalMusic(CallbackInfoReturnable<MusicInfo> info) {
        Music music = Optionull.map(instance.screen, Screen::getBackgroundMusic);

        if (music != null) {
            if (instance.screen instanceof WinScreen && !instance.getMusicManager().isPlayingMusic(Musics.CREDITS)) instance.getSoundManager().stop();
            return;
        }

        LocalPlayer player = instance.player;

        if (player != null && !instance.gui.getBossOverlay().shouldPlayMusic()) {
            EnderscapeClient.structureMusic.ifPresentOrElse((value) -> {
                info.setReturnValue(new MusicInfo(value, 1.0F));
            }, () -> {
                Level level = player.level();
                if (level.dimension() == Level.END) {
                    Biome biome = level.getBiome(player.blockPosition()).value();
                    float volume = biome.getBackgroundMusicVolume();

                    biome.getBackgroundMusic().ifPresent(list -> {
                        Optional<Music> value = list.getRandomValue(level.random);
                        info.setReturnValue(new MusicInfo(value.orElse(null), volume));
                    });
                }
            });
        }
    }
}