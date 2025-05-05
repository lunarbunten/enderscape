package net.bunten.enderscape.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public record DashJumpShockwaveParticleOptions(Vector3f velocity, float scale) implements ParticleOptions {

    public static final MapCodec<DashJumpShockwaveParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.VECTOR3F.fieldOf("velocity").forGetter(options -> options.velocity),
                    Codec.FLOAT.fieldOf("scale").forGetter(options -> options.scale)
            ).apply(instance, DashJumpShockwaveParticleOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DashJumpShockwaveParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, options -> options.velocity,
            ByteBufCodecs.FLOAT, options -> options.scale,
            DashJumpShockwaveParticleOptions::new
    );

    @Override
    public ParticleType<DashJumpShockwaveParticleOptions> getType() {
        return EnderscapeParticles.DASH_JUMP_SHOCKWAVE;
    }
}
