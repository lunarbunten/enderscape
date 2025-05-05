package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.bunten.enderscape.registry.EnderscapeMusic.STRUCTURE_TRACKS;

@Environment(EnvType.CLIENT)
@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {

    @Shadow @Final private Minecraft minecraft;

    @Shadow private @Nullable SoundInstance currentMusic;

    @Shadow private int nextSongDelay;

    @Shadow @Final private RandomSource random;

    @Shadow private float currentGain;

    @Shadow public abstract void stopPlaying();

    @Inject(method = "tick", at = @At("HEAD"))
    public void Enderscape$tick(CallbackInfo info) {
        Music situational = minecraft.getSituationalMusic().music();

        if (currentMusic != null) {
            boolean fadeToStructureMusic = EnderscapeClient.structureMusic.filter(music -> situational == music && currentMusic.getLocation() != music.getEvent().value().location()).isPresent();
            boolean fadeFromStructureMusic = EnderscapeClient.structureMusic.isEmpty() && STRUCTURE_TRACKS.contains(currentMusic.getLocation());

            if (fadeToStructureMusic || fadeFromStructureMusic) {
                if (!Enderscape$slowlyFadePlaying(-1.0F)) {
                    currentMusic = null;
                    nextSongDelay = Mth.nextInt(random, 0, situational.getMinDelay()) / 2;
                }
            }
        }
    }

    @Unique
    private boolean Enderscape$slowlyFadePlaying(float target) {
        if (currentMusic == null) {
            return false;
        } else if (currentGain == target) {
            return true;
        } else {
            if (currentGain < target) {
                currentGain = currentGain + Mth.clamp(currentGain, 5.0E-4F, 0.005F);
                if (currentGain > target) {
                    currentGain = target;
                }
            } else {
                currentGain = 0.0075F * target + 0.9925F * currentGain;
                if (Math.abs(currentGain - target) < 1.0E-4F || currentGain < target) {
                    currentGain = target;
                }
            }

            currentGain = Mth.clamp(currentGain, 0.0F, 1.0F);
            if (currentGain <= 1.0E-4F) {
                stopPlaying();
                return false;
            } else {
                minecraft.getSoundManager().setVolume(currentMusic, currentGain);
                return true;
            }
        }
    }
}