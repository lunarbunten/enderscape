package net.bunten.enderscape.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DriftJellyDrippingParticle extends TextureSheetParticle {

    protected DriftJellyDrippingParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
        setSpriteFromAge(sprites);

        hasPhysics = true;
        gravity = 0.04F;
        lifetime = 40;

        this.xd = 0;
        this.yd = -0.04F;
        this.zd = 0;

        quadSize = 0.1F;
    }

    @Override
    public void tick() {
        if (alpha > 0.25F) {
            alpha -= 0.005F;
        } else {
            remove();
        }
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new DriftJellyDrippingParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}