package net.penumbra.enderscape.sound;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

import static net.penumbra.enderscape.Enderscape.registerSoundEventHolder;

public record BiomeSounds(String name, Holder<SoundEvent> loop, Holder<SoundEvent> additions, Holder<SoundEvent> mood, @Nullable Holder<SoundEvent> music) {

    public static BiomeSounds of(String name) {
        return new BiomeSounds(name, loop(name), additions(name), mood(name), biomeMusic(name));
    }

    public static BiomeSounds noMusic(String name) {
        return new BiomeSounds(name, loop(name), additions(name), mood(name), null);
    }

    public static Holder<SoundEvent> additions(String name) {
        return registerSoundEventHolder("ambient." + name + ".additions");
    }

    public static Holder<SoundEvent> loop(String name) {
        return registerSoundEventHolder("ambient." + name + ".loop");
    }

    public static Holder<SoundEvent> mood(String name) {
        return registerSoundEventHolder("ambient." + name + ".mood");
    }

    public static Holder<SoundEvent> biomeMusic(String name) {
        return registerSoundEventHolder("music.enderscape.biome." + name);
    }
}