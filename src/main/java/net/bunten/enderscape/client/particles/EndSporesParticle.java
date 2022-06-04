package net.bunten.enderscape.client.particles;

import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class EndSporesParticle extends SpriteBillboardParticle {

    protected EndSporesParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, SpriteProvider provider) {
        super(world, x, y, z, vx, vy, vz);
        setSpriteForAge(provider);

        collidesWithWorld = true;
        gravityStrength = -0.05F;
        maxAge = 40;

        scale = 0.02F;
    }

    public void tick() {
        float sin = MathUtil.sin(age * 0.4F) * 0.01F;
        velocityX += sin;
        velocityZ += sin;
        if (alpha > 0.25F) {
            alpha -= 0.0075F;
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
            return new EndSporesParticle(world, x, y, z, vx, vy, vz, provider);
        }
    }
}