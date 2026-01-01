package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.registry.tag.EnderscapeSoundEventTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow private @Nullable SoundInstance currentMusic;
    @Shadow protected abstract boolean fadePlaying(float f);

    @Unique private int fadeState = 0;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void Enderscape$tick(CallbackInfo info) {
        ClientLevel level = minecraft.level;
        Music situational = minecraft.getSituationalMusic();

        if (level != null && currentMusic != null && EnderscapeConfig.getInstance().structureMusicFadingEnabled) {
            boolean fadeToStructure = EnderscapeClient.structureMusic.stream().anyMatch(music -> situational == music && currentMusic.getIdentifier() != music.sound().value().location());
            boolean fadeFromStructure = EnderscapeClient.structureMusic.isEmpty() && Enderscape$playingStructureMusic();

            if (fadeToStructure || fadeFromStructure) {
                fadeState = 1;
                if (!fadePlaying(-1.0F)) info.cancel();
            }
        }
    }

    @Inject(method = "stopPlaying()V", at = @At("TAIL"))
    public void Enderscape$stopPlaying(CallbackInfo info) {
        fadeState = 2;
    }

    @Inject(method = "startPlaying", at = @At("TAIL"))
    public void Enderscape$resetFadeState(CallbackInfo info) {
        fadeState = 0;
    }

    @ModifyConstant(method = "fadePlaying", constant = @Constant(floatValue = 0.03F))
    private float Enderscape$modifyFadeRate(float original) {
        return fadeState == 1 ? 0.0075F : original;
    }

    @Unique
    private boolean Enderscape$playingStructureMusic() {
        if (currentMusic == null || minecraft.level == null) return false;

        Holder.Reference<SoundEvent> reference = minecraft.level.registryAccess()
                .lookupOrThrow(Registries.SOUND_EVENT)
                .get(currentMusic.getIdentifier())
                .orElse(null);

        return reference != null && reference.is(EnderscapeSoundEventTags.STRUCTURE_MUSIC);
    }
}