package net.bunten.enderscape.client.particles;

import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor.ARGB32;

@Environment(EnvType.CLIENT)
public class VanishingNebuliteCloudParticle extends NebuliteCloudParticle {
    VanishingNebuliteCloudParticle(ClientLevel world, double d, double e, double f, double g, double h, double i, int j, SpriteSet sprites) {
        super(world, d, e, f, g, h, i, j, sprites);

        gravity = 0;
        quadSize = MathUtil.nextFloat(random, 0.02F, 0.12F);
        lifetime = MathUtil.nextInt(random, 20, 40);
        
        xd = MathUtil.nextFloat(random, -0.1F, 0.1F);
        yd = MathUtil.nextFloat(random, -0.1F, 0.1F);
        zd = MathUtil.nextFloat(random, -0.1F, 0.1F);
    }

    @Environment(value=EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double d, double e, double f, double g, double h, double i) {
            return new VanishingNebuliteCloudParticle(world, d, e, f, g, h, i, ARGB32.color(255, 182, 28, 204), sprites);
        }
    }
}