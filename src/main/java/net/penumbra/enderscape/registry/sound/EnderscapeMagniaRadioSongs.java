package net.penumbra.enderscape.registry.sound;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.EnderscapeRegistries;
import net.penumbra.enderscape.sound.MagniaRadioSong;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnderscapeMagniaRadioSongs {

    public static final List<ResourceKey<MagniaRadioSong>> MAGNIA_RADIO_SONGS = new ArrayList<>();

    public static final ResourceKey<MagniaRadioSong> FALLBACK = register("fallback");

    public static void bootstrap(BootstrapContext<MagniaRadioSong> context) {
        register(context, FALLBACK, EnderscapeMusic.MAGNIA_RADIO_FALLBACK, 32, Optional.empty(), Optional.empty());
    }

    private static void register(BootstrapContext<MagniaRadioSong> context, ResourceKey<MagniaRadioSong> key, Holder<SoundEvent> soundEvent, float length, int exclusiveSignal, TagKey<Biome> biomes) {
        register(context, key, soundEvent, length, Optional.of(exclusiveSignal), Optional.of(biomes));
    }


    private static void register(BootstrapContext<MagniaRadioSong> context, ResourceKey<MagniaRadioSong> key, Holder<SoundEvent> soundEvent, float length, Optional<Integer> exclusiveSignal, Optional<TagKey<Biome>> biomes) {
        context.register(key, new MagniaRadioSong(soundEvent, Component.translatable("magnia_radio_song.enderscape." + key.identifier().getPath()), length, exclusiveSignal, biomes));
    }

    private static ResourceKey<MagniaRadioSong> register(String name) {
        ResourceKey<MagniaRadioSong> key = ResourceKey.create(EnderscapeRegistries.MAGNIA_RADIO_SONG, Enderscape.id(name));
        MAGNIA_RADIO_SONGS.add(key);
        return key;
    }
}