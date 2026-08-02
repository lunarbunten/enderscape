package net.penumbra.enderscape.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;

import static net.penumbra.enderscape.registry.particle.EnderscapeParticleProviders.DRIFT_JELLY_PARTICLE_COLOR;

public class DriftJellyLandParticle extends DripParticle.DripLandParticle {

    public DriftJellyLandParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, Fluids.EMPTY, sprite);

        setColor(DRIFT_JELLY_PARTICLE_COLOR.x(), DRIFT_JELLY_PARTICLE_COLOR.y(), DRIFT_JELLY_PARTICLE_COLOR.z());
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(final SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(
                final SimpleParticleType options,
                final ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final RandomSource random
        ) {
            return new DriftJellyLandParticle(level, x, y, z, sprite.get(random));
        }
    }
}
