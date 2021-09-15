package net.enderscape.registry;

import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;

public class EndMusic {
    public static final MusicSound NORMAL = create(EndSounds.MUSIC_END);

    public static final MusicSound CELESTIAL = create(EndSounds.MUSIC_CELESTIAL);
    public static final MusicSound CORRUPTION = create(EndSounds.MUSIC_CORRUPTION);

    public static final MusicSound ELYTRA = new MusicSound(EndSounds.MUSIC_ELYTRA, 20, 600, false);
    public static final MusicSound VOID = create(EndSounds.MUSIC_VOID);

    public static MusicSound create(SoundEvent event) {
        return new MusicSound(event, 12000, 24000, false);
    }
}