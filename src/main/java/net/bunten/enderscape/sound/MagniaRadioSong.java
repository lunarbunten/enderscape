package net.bunten.enderscape.sound;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

public record MagniaRadioSong(Holder<SoundEvent> soundEvent, Component description, float lengthInSeconds, int exclusiveSignal, TagKey<Biome> permittedBiomes) {
    public static final Codec<MagniaRadioSong> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    SoundEvent.CODEC.fieldOf("sound_event").forGetter(MagniaRadioSong::soundEvent),
                    ComponentSerialization.CODEC.fieldOf("description").forGetter(MagniaRadioSong::description),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("length_in_seconds").forGetter(MagniaRadioSong::lengthInSeconds),
                    ExtraCodecs.intRange(0, 15).fieldOf("exclusive_signal").forGetter(MagniaRadioSong::exclusiveSignal),
                    TagKey.codec(Registries.BIOME).fieldOf("permitted_biomes").forGetter(MagniaRadioSong::permittedBiomes)
            ).apply(instance, MagniaRadioSong::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MagniaRadioSong> DIRECT_STREAM_CODEC = StreamCodec.composite(
            SoundEvent.STREAM_CODEC,
            MagniaRadioSong::soundEvent,
            ComponentSerialization.STREAM_CODEC,
            MagniaRadioSong::description,
            ByteBufCodecs.FLOAT,
            MagniaRadioSong::lengthInSeconds,
            ByteBufCodecs.VAR_INT,
            MagniaRadioSong::exclusiveSignal,
            TagKey.streamCodec(Registries.BIOME),
            MagniaRadioSong::permittedBiomes,
            MagniaRadioSong::new
    );

    public static final Codec<Holder<MagniaRadioSong>> CODEC = RegistryFixedCodec.create(EnderscapeRegistries.MAGNIA_RADIO_SONG);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MagniaRadioSong>> STREAM_CODEC = ByteBufCodecs.holder(EnderscapeRegistries.MAGNIA_RADIO_SONG, DIRECT_STREAM_CODEC);

    public int lengthInTicks() {
        return Mth.ceil(lengthInSeconds * 20.0F);
    }

    public boolean hasFinished(long l) {
        return l >= lengthInTicks() + 20;
    }
}
