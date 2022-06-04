package net.bunten.enderscape.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class DrippingJellyParticle extends SpriteBillboardParticle {

    protected DrippingJellyParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, SpriteProvider provider) {
        super(world, x, y, z, vx, vy, vz);
        setSpriteForAge(provider);

        collidesWithWorld = true;
        gravityStrength = 0.04F;
        maxAge = 40;

        velocityX = 0;
        velocityY = -0.04F;
        velocityZ = 0;

        scale = 0.1F;
    }

    public void tick() {
        if (alpha > 0.25F) {
            alpha -= 0.005F;
        } else {
            markDead();
        }
        super.tick();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider provider;

        public DefaultFactory(SpriteProvider provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
            return new DrippingJellyParticle(world, x, y, z, vx, vy, vz, provider);
        }
    }
}