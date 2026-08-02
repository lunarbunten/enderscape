package net.penumbra.enderscape.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.penumbra.enderscape.registry.EnderscapeRegistries;
import net.penumbra.enderscape.sound.MagniaRadioSong;

public record MagniaRadioSongPredicate(ResourceKey<MagniaRadioSong> song) {

    public static final Codec<MagniaRadioSongPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceKey.codec(EnderscapeRegistries.MAGNIA_RADIO_SONG).fieldOf("song").forGetter(MagniaRadioSongPredicate::song)).apply(instance, MagniaRadioSongPredicate::new));

    public static MagniaRadioSongPredicate of(ResourceKey<MagniaRadioSong> song) {
        return new MagniaRadioSongPredicate(song);
    }

    public boolean matches(ResourceKey<MagniaRadioSong> song) {
        return this.song == song;
    }
}