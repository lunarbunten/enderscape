package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.sound.MagniaRadioSong;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeMagniaRadioSongs {

    public static final List<ResourceKey<MagniaRadioSong>> MAGNIA_RADIO_SONGS = new ArrayList<>();

    public static void bootstrap(BootstrapContext<MagniaRadioSong> context) {
    }

    private static void register(BootstrapContext<MagniaRadioSong> context, ResourceKey<MagniaRadioSong> key, Holder<SoundEvent> soundEvent, float length, int exclusiveSignal, TagKey<Biome> biomes) {
        context.register(key, new MagniaRadioSong(soundEvent, Component.translatable("magnia_radio_song.enderscape." + key.identifier().getPath()), length, exclusiveSignal, biomes));
    }

    private static ResourceKey<MagniaRadioSong> register(String name) {
        ResourceKey<MagniaRadioSong> key = ResourceKey.create(EnderscapeRegistries.MAGNIA_RADIO_SONG, Enderscape.id(name));
        MAGNIA_RADIO_SONGS.add(key);
        return key;
    }
}
