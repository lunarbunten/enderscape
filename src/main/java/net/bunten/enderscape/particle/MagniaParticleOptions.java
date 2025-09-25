package net.bunten.enderscape.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record MagniaParticleOptions(ParticleType<MagniaParticleOptions> particle, int color, int fadeColor, float colorFadeRate) implements ParticleOptions {

    public static final float DEFAULT_FADE_RATE = 0.3F;

    public static final int ALLURING_START_COLOR = 0xA5BCFF;
    public static final int ALLURING_FADE_COLOR = 0xF2FDFF;

    public static final int REPULSIVE_START_COLOR = 0xFFA5C3;
    public static final int REPULSIVE_FADE_COLOR = 0x382f3d;

    public static final MagniaParticleOptions SPROUT_ALLURING = new MagniaParticleOptions(EnderscapeParticles.MAGNIA_SPROUT, ALLURING_START_COLOR, ALLURING_FADE_COLOR, DEFAULT_FADE_RATE);
    public static final MagniaParticleOptions SPROUT_REPULSIVE = new MagniaParticleOptions(EnderscapeParticles.MAGNIA_SPROUT, REPULSIVE_START_COLOR, REPULSIVE_FADE_COLOR, DEFAULT_FADE_RATE);

    public static final MagniaParticleOptions ENTITY_ALLURED = new MagniaParticleOptions(EnderscapeParticles.ENTITY_EFFECTED_BY_MAGNIA, ALLURING_START_COLOR, ALLURING_FADE_COLOR, DEFAULT_FADE_RATE);
    public static final MagniaParticleOptions ENTITY_REPULSED = new MagniaParticleOptions(EnderscapeParticles.ENTITY_EFFECTED_BY_MAGNIA, REPULSIVE_START_COLOR, REPULSIVE_FADE_COLOR, DEFAULT_FADE_RATE);

    public static final MagniaParticleOptions MAGNIA_ATTRACTOR = new MagniaParticleOptions(EnderscapeParticles.ENTITY_EFFECTED_BY_MAGNIA, ALLURING_START_COLOR, ALLURING_FADE_COLOR, DEFAULT_FADE_RATE);

    public static MapCodec<MagniaParticleOptions> codec(ParticleType<MagniaParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("color").forGetter(options -> options.color),
                ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("fade_color").forGetter(options -> options.fadeColor),
                Codec.FLOAT.fieldOf("color_fade_rate").forGetter(options -> options.colorFadeRate)
        ).apply(instance, (color, fadeColor, colorFadeRate) -> new MagniaParticleOptions(type, color, fadeColor, colorFadeRate)));
    }

    public static StreamCodec<? super ByteBuf, MagniaParticleOptions> streamCodec(ParticleType<MagniaParticleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.INT, options -> options.color,
                ByteBufCodecs.INT, options -> options.fadeColor,
                ByteBufCodecs.FLOAT, options -> options.colorFadeRate,
                (color, fadeColor, colorFadeRate) -> new MagniaParticleOptions(type, color, fadeColor, colorFadeRate)
        );
    }

    @Override
    public ParticleType<MagniaParticleOptions> getType() {
        return particle;
    }
}
