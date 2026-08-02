package net.penumbra.enderscape.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class VoidPoofParticle extends ExplodeParticle {

    protected VoidPoofParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites);

        float color = Mth.nextFloat(random, 0.025F, 0.15F);
        rCol = color * 1.2F;
        gCol = color;
        bCol = color * 1.4F;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double d, double e, double f, double g, double h, double i, RandomSource randomSource) {
            return new VoidPoofParticle(level, d, e, f, g, h, i, sprites);
        }
    }
}