package net.bunten.enderscape.biome.util;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

import static net.bunten.enderscape.Enderscape.registerSoundEventHolder;

public record BiomeSounds(String name, Holder<SoundEvent> loop, Holder<SoundEvent> additions, Holder<SoundEvent> mood, Holder<SoundEvent> music) {

    public static BiomeSounds of(String name) {
        return new BiomeSounds(name, loop(name), additions(name), mood(name), biomeMusic(name));
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
        return registerSoundEventHolder("music.the_end.biome." + name);
    }
}