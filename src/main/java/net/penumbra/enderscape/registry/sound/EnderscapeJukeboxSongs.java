package net.penumbra.enderscape.registry.sound;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.penumbra.enderscape.Enderscape;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeJukeboxSongs {

    public static final List<ResourceKey<JukeboxSong>> JUKEBOX_SONGS = new ArrayList<>();

    public static final ResourceKey<JukeboxSong> GLARE = register("glare");
    public static final ResourceKey<JukeboxSong> DECAY = register("decay");
    public static final ResourceKey<JukeboxSong> BLISS = register("bliss");

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        register(context, GLARE, EnderscapeMusic.MUSIC_DISC_GLARE, 235, 10);
        register(context, DECAY, EnderscapeMusic.MUSIC_DISC_DECAY, 97, 1);
        register(context, BLISS, EnderscapeMusic.MUSIC_DISC_BLISS, 272, 15);
    }

    private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, Holder<SoundEvent> soundEvent, float length, int output) {
        context.register(key, new JukeboxSong(soundEvent, Component.translatable("jukebox_song.enderscape." + key.identifier().getPath()), length, output));
    }

    private static ResourceKey<JukeboxSong> register(String name) {
        ResourceKey<JukeboxSong> key = ResourceKey.create(Registries.JUKEBOX_SONG, Enderscape.id(name));
        JUKEBOX_SONGS.add(key);
        return key;
    }
}
