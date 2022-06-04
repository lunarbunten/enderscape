package net.bunten.enderscape.registry;

import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class EnderscapeMusic {
    public static final MusicSound CELESTIAL_PLAINS = create(EnderscapeSounds.MUSIC_CELESTIAL_PLAINS);
    public static final MusicSound CELESTIAL_ISLANDS = create(EnderscapeSounds.MUSIC_CELESTIAL_ISLANDS);

    public static final MusicSound NORMAL_END = new MusicSound(SoundEvents.MUSIC_END, 6000, 24000, false);
    public static final MusicSound SKY = new MusicSound(EnderscapeSounds.MUSIC_SKY, 20, 600, false);
    public static final MusicSound VOID = create(EnderscapeSounds.MUSIC_VOID);

    public static MusicSound create(SoundEvent event) {
        return new MusicSound(event, 12000, 24000, false);
    }
}