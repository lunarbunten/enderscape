package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeMusic {

    public static final List<ResourceLocation> STRUCTURE_TRACKS = new ArrayList<>();

    public static final Music MUSIC_END_CITY = createStructureMusic("end_city");

    @NotNull
    private static Music createStructureMusic(String name) {
        Music music = new Music(Enderscape.registerSoundEventHolder("music.the_end.structure." + name), 0, 6000, false);
        STRUCTURE_TRACKS.add(music.event().value().location());
        return music;
    }

    public static final Holder.Reference<SoundEvent> MUSIC_DISC_GLARE = Enderscape.registerSoundEventHolder("music_disc.glare");
    public static final Holder.Reference<SoundEvent> MUSIC_DISC_DECAY = Enderscape.registerSoundEventHolder("music_disc.decay");
    public static final Holder.Reference<SoundEvent> MUSIC_DISC_BLISS = Enderscape.registerSoundEventHolder("music_disc.bliss");

}