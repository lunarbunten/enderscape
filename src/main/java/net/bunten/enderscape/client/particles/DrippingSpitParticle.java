package net.bunten.enderscape.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class DrippingSpitParticle extends TextureSheetParticle {
    protected DrippingSpitParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(world, x, y, z, vx, vy, vz);
        setSpriteFromAge(sprites);

        hasPhysics = true;
        gravity = 0.18F;
        lifetime = 30;

        xd = 0;
        yd = -0.04F;
        zd = 0;

        quadSize = 0.2F;
    }

    @Override
    public void tick() {
        if (age > 10) {
            if (alpha > 0.1F) {
                alpha -= 0.045F;
            } else {
                remove();
            }
        }
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double vx, double vy, double vz) {
            return new DrippingSpitParticle(world, x, y, z, vx, vy, vz, sprites);
        }
    }
}