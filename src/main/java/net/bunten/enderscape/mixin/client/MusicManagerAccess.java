package net.bunten.enderscape.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;

@Environment(EnvType.CLIENT)
@Mixin(MusicManager.class)
public interface MusicManagerAccess {

    @Accessor
    SoundInstance getCurrentMusic();

    @Accessor
    int getNextSongDelay();
}