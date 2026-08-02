package net.penumbra.enderscape.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class RustleSleepingBubblePopParticle extends SingleQuadParticle {
    private final SpriteSet sprites;

    public RustleSleepingBubblePopParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        this.sprites = sprites;

        lifetime = 4;
        gravity = 0.008F;

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        if (age++ >= lifetime) {
            remove();
        } else {
            yd = yd - (double) gravity;
            move(xd, yd, zd);
            setSpriteFromAge(sprites);
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new RustleSleepingBubblePopParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}