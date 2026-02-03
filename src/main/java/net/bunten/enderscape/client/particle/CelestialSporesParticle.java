package net.bunten.enderscape.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CelestialSporesParticle extends TextureSheetParticle {

    protected CelestialSporesParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
        setSpriteFromAge(sprites);

        hasPhysics = true;
        gravity = -0.05F;
        lifetime = 40;

        quadSize = 0.02F;
    }

    public void tick() {
        float sin = Mth.sin(age * 0.4F) * 0.01F;
        xd += sin;
        zd += sin;
        if (alpha > 0.25F) {
            alpha -= 0.0075F;
        } else {
            remove();
        }
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float delta) {
        return Math.max(20, super.getLightColor(delta));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new CelestialSporesParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}