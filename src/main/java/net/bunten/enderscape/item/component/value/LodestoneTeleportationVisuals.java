package net.bunten.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record LodestoneTeleportationVisuals(
        ParticleOptions teleportOutParticle,
        ParticleOptions teleportInParticle,
        AssetInfo overlayTexture,
        AssetInfo vignetteTexture
) {

    public static final ParticleOptions DEFAULT_TELEPORT_OUT_PARTICLE = EnderscapeParticles.MIRROR_TELEPORT_OUT.get();
    public static final ParticleOptions DEFAULT_TELEPORT_IN_PARTICLE = EnderscapeParticles.MIRROR_TELEPORT_IN.get();
    public static final AssetInfo DEFAULT_OVERLAY_TEXTURE = assetOf(Enderscape.id("misc/overlay"));
    public static final AssetInfo DEFAULT_VIGNETTE_TEXTURE = assetOf(Enderscape.id("misc/vignette"));

    public static final Codec<LodestoneTeleportationVisuals> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ParticleTypes.CODEC.optionalFieldOf("teleport_out_particle", DEFAULT_TELEPORT_OUT_PARTICLE).forGetter(LodestoneTeleportationVisuals::teleportOutParticle),
                    ParticleTypes.CODEC.optionalFieldOf("teleport_in_particle", DEFAULT_TELEPORT_IN_PARTICLE).forGetter(LodestoneTeleportationVisuals::teleportInParticle),
                    AssetInfo.CODEC.optionalFieldOf("overlay_texture", DEFAULT_OVERLAY_TEXTURE).forGetter(LodestoneTeleportationVisuals::overlayTexture),
                    AssetInfo.CODEC.optionalFieldOf("vignette_texture", DEFAULT_VIGNETTE_TEXTURE).forGetter(LodestoneTeleportationVisuals::vignetteTexture)
            ).apply(instance, LodestoneTeleportationVisuals::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LodestoneTeleportationVisuals> STREAM_CODEC = StreamCodec.composite(
            ParticleTypes.STREAM_CODEC,
            LodestoneTeleportationVisuals::teleportOutParticle,
            ParticleTypes.STREAM_CODEC,
            LodestoneTeleportationVisuals::teleportInParticle,
            AssetInfo.STREAM_CODEC,
            LodestoneTeleportationVisuals::overlayTexture,
            AssetInfo.STREAM_CODEC,
            LodestoneTeleportationVisuals::vignetteTexture,
            LodestoneTeleportationVisuals::new
    );

    public static final LodestoneTeleportationVisuals DEFAULT = Builder.create().build();

    public static class Builder {
        private ParticleOptions teleportOutParticle = DEFAULT_TELEPORT_OUT_PARTICLE;
        private ParticleOptions teleportInParticle = DEFAULT_TELEPORT_IN_PARTICLE;
        private AssetInfo overlayTexture = DEFAULT_OVERLAY_TEXTURE;
        private AssetInfo vignetteTexture = DEFAULT_VIGNETTE_TEXTURE;

        public static Builder create() {
            return new Builder();
        }

        public Builder link(ParticleOptions teleportOutParticle) {
            this.teleportOutParticle = teleportOutParticle;
            return this;
        }

        public Builder teleportFailure(ParticleOptions teleportInParticle) {
            this.teleportInParticle = teleportInParticle;
            return this;
        }

        public Builder overlayTexture(AssetInfo overlayTexture) {
            this.overlayTexture = overlayTexture;
            return this;
        }

        public Builder vignetteTexture(AssetInfo vignetteTexture) {
            this.vignetteTexture = vignetteTexture;
            return this;
        }

        public LodestoneTeleportationVisuals build() {
            return new LodestoneTeleportationVisuals(
                    teleportOutParticle,
                    teleportInParticle,
                    overlayTexture,
                    vignetteTexture
            );
        }
    }

    public record AssetInfo(ResourceLocation asset) {
        public static final Codec<AssetInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("asset").forGetter(AssetInfo::asset)).apply(instance, AssetInfo::new));
        public static final StreamCodec<ByteBuf, AssetInfo> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(AssetInfo::new, AssetInfo::asset);
    }

    private static AssetInfo assetOf(ResourceLocation id) {
        return new AssetInfo(id);
    }
}