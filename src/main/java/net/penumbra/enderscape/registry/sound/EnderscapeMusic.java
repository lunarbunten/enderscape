package net.penumbra.enderscape.registry.sound;

import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.penumbra.enderscape.Enderscape;
import org.jetbrains.annotations.NotNull;

public class EnderscapeMusic {

    public static final Music STRUCTURE_END_CITY = createStructureMusic("end_city");
    public static final Music STRUCTURE_STRONGHOLD = createStructureMusic("stronghold");

    @NotNull
    private static Music createStructureMusic(String name) {
        return new Music(Enderscape.registerSoundEventHolder("music.enderscape.structure." + name), 0, 6000, false);
    }

    public static final Holder.Reference<SoundEvent> MAGNIA_RADIO_FALLBACK = Enderscape.registerSoundEventHolder("magnia_radio.fallback");

    public static final Holder.Reference<SoundEvent> MUSIC_DISC_GLARE = Enderscape.registerSoundEventHolder("music_disc.glare");
    public static final Holder.Reference<SoundEvent> MUSIC_DISC_DECAY = Enderscape.registerSoundEventHolder("music_disc.decay");
    public static final Holder.Reference<SoundEvent> MUSIC_DISC_BLISS = Enderscape.registerSoundEventHolder("music_disc.bliss");

}