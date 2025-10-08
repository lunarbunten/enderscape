package net.bunten.enderscape.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class AlluringMagniaParticle extends AbstractMagniaParticle {
 
    protected AlluringMagniaParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(world, x, y, z, xd, yd, zd, sprites);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new AlluringMagniaParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}
