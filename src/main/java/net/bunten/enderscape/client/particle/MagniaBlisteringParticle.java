package net.bunten.enderscape.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class MagniaBlisteringParticle extends TextureSheetParticle {
    public MagniaBlisteringParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);
        float j = random.nextFloat() * 0.1F + 0.2F;

        rCol = j;
        gCol = j;
        bCol = j;

        setSize(0.02F, 0.02F);
        quadSize = quadSize * (random.nextFloat() * 0.6F + 0.5F);

        this.xd *= 0.02F;
        this.yd *= 0.02F;
        this.zd *= 0.02F;

        lifetime = (int)(20.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double d, double e, double f) {
        setBoundingBox(getBoundingBox().move(d, e, f));
        setLocationFromBoundingbox();
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (lifetime-- <= 0) {
            remove();
        } else {
            move(xd, yd, zd);
            xd *= 0.99;
            yd *= 0.99;
            zd *= 0.99;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            MagniaBlisteringParticle particle = new MagniaBlisteringParticle(level, x, y, z, xd, yd, zd);
            particle.pickSprite(sprites);
            particle.setColor(1.0F, 1.0F, 1.0F);
            return particle;
        }
    }
}