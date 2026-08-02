package net.penumbra.enderscape.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class MirrorTeleportOutParticle extends NebuliteOreParticle {
    MirrorTeleportOutParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites);

        gravity = 0;
        quadSize = Mth.nextFloat(random, 0.02F, 0.12F);
        lifetime = Mth.nextInt(random, 20, 40);
        
        this.xd = Mth.nextFloat(random, -0.1F, 0.1F);
        this.yd = Mth.nextFloat(random, -0.1F, 0.1F);
        this.zd = Mth.nextFloat(random, -0.1F, 0.1F);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double d, double e, double f, double g, double h, double i, RandomSource randomSource) {
            return new MirrorTeleportOutParticle(level, d, e, f, g, h, i, sprites);
        }
    }
}