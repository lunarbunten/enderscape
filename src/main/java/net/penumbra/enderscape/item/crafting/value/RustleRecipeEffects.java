package net.penumbra.enderscape.item.crafting.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;

public record RustleRecipeEffects(
        int swellDuration,
        float swellSoundPitch,
        Holder<SoundEvent> swellSound,
        Holder<SoundEvent> spitSound,
        ParticleOptions convertParticle
) {

    public static final int MAX_SWELL_DURATION = 200;

    private static final int DEFAULT_SWELL_DURATION = 20;
    private static final float DEFAULT_SWELL_SOUND_PITCH = 1.0F;
    private static final Holder<SoundEvent> DEFAULT_SWELL_SOUND = EnderscapeEntitySounds.RUSTLE_SWELL;
    private static final Holder<SoundEvent> DEFAULT_SPIT_SOUND = EnderscapeEntitySounds.RUSTLE_SPIT;
    private static final ParticleOptions DEFAULT_CONVERT_PARTICLE = EnderscapeParticles.RUSTLE_CONVERTING;

    public static final Codec<RustleRecipeEffects> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExtraCodecs.intRange(0, MAX_SWELL_DURATION).optionalFieldOf("swell_duration", DEFAULT_SWELL_DURATION).forGetter(RustleRecipeEffects::swellDuration),
                    ExtraCodecs.floatRange(0.0F, 2.0F).optionalFieldOf("swell_sound_pitch", DEFAULT_SWELL_SOUND_PITCH).forGetter(RustleRecipeEffects::swellSoundPitch),
                    SoundEvent.CODEC.optionalFieldOf("swell_sound", DEFAULT_SWELL_SOUND).forGetter(RustleRecipeEffects::swellSound),
                    SoundEvent.CODEC.optionalFieldOf("spit_sound", DEFAULT_SPIT_SOUND).forGetter(RustleRecipeEffects::spitSound),
                    ParticleTypes.CODEC.optionalFieldOf("convert_particle", DEFAULT_CONVERT_PARTICLE).forGetter(RustleRecipeEffects::convertParticle)
            ).apply(instance, RustleRecipeEffects::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RustleRecipeEffects> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            RustleRecipeEffects::swellDuration,
            ByteBufCodecs.FLOAT,
            RustleRecipeEffects::swellSoundPitch,
            SoundEvent.STREAM_CODEC,
            RustleRecipeEffects::swellSound,
            SoundEvent.STREAM_CODEC,
            RustleRecipeEffects::spitSound,
            ParticleTypes.STREAM_CODEC,
            RustleRecipeEffects::convertParticle,
            RustleRecipeEffects::new
    );

    public static final RustleRecipeEffects DEFAULT = Builder.create().build();

    public static class Builder {
        private int swellDuration = DEFAULT_SWELL_DURATION;
        private float swellSoundPitch = DEFAULT_SWELL_SOUND_PITCH;
        private Holder<SoundEvent> swellSound = DEFAULT_SWELL_SOUND;
        private Holder<SoundEvent> spitSound = DEFAULT_SPIT_SOUND;
        private Holder<SoundEvent> convertSound = DEFAULT_SPIT_SOUND;
        private ParticleOptions convertParticle = DEFAULT_CONVERT_PARTICLE;

        public static Builder create() {
            return new Builder();
        }

        public Builder swellDuration(int value) {
            this.swellDuration = value;
            return this;
        }

        public Builder swellSoundPitch(float value) {
            this.swellSoundPitch = value;
            return this;
        }

        public Builder swellSound(Holder<SoundEvent> value) {
            this.swellSound = value;
            return this;
        }

        public Builder spitSound(Holder<SoundEvent> value) {
            this.spitSound = value;
            return this;
        }

        public Builder convertParticle(ParticleOptions value) {
            this.convertParticle = value;
            return this;
        }

        public RustleRecipeEffects build() {
            return new RustleRecipeEffects(
                    swellDuration,
                    swellSoundPitch,
                    swellSound,
                    spitSound,
                    convertParticle
            );
        }
    }
}