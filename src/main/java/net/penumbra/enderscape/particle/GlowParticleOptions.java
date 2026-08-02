package net.penumbra.enderscape.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;

public record GlowParticleOptions(
        ParticleType<GlowParticleOptions> particle,
        IntProvider lifetime,
        float friction
) implements ParticleOptions {

    public static final GlowParticleOptions CHARGING = new GlowParticleOptions(
            EnderscapeParticles.GLOW_CHARGING,
            UniformInt.of(5, 10),
            0.6F
    );

    public static final GlowParticleOptions DEFAULT = new GlowParticleOptions(
            EnderscapeParticles.GLOW,
            UniformInt.of(15, 30),
            0.9F
    );

    public static MapCodec<GlowParticleOptions> codec(ParticleType<GlowParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                IntProviders.CODEC.fieldOf("lifetime").forGetter(options -> options.lifetime),
                Codec.FLOAT.fieldOf("friction").forGetter(options -> options.friction)
        ).apply(instance, (lifetime, friction) -> new GlowParticleOptions(type, lifetime, friction)));
    }

    public static StreamCodec<? super ByteBuf, GlowParticleOptions> streamCodec(ParticleType<GlowParticleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.fromCodec(IntProviders.CODEC), options -> options.lifetime,
                ByteBufCodecs.FLOAT, options -> options.friction,
                (lifetime, friction) -> new GlowParticleOptions(type, lifetime, friction)
        );
    }

    @Override
    public ParticleType<GlowParticleOptions> getType() {
        return particle;
    }
}
