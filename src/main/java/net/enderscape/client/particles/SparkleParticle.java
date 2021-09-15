package net.enderscape.client.particles;

import net.enderscape.util.EndMath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class SparkleParticle extends SpriteBillboardParticle {

    protected SparkleParticle(ClientWorld world, double x, double y, double z, SpriteProvider sprite) {
        super(world, x, y, z);
        setSpriteForAge(sprite);

        scale = EndMath.nextFloat(random, 0.02F, 0.2F);
        maxAge = EndMath.nextInt(random, 60, 120);

        velocityX = EndMath.nextFloat(random, -1, 1) * 0.05;
        velocityY = EndMath.nextFloat(random, -1, 1) * 0.05;
        velocityZ = EndMath.nextFloat(random, -1, 1) * 0.05;
    }

    public void tick() {
        if (colorAlpha > 0.25F) {
            colorAlpha *= 0.98;
            scale *= 0.98;
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
        private final SpriteProvider sprite;

        public DefaultFactory(SpriteProvider sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new SparkleParticle(world, x, y, z, sprite);
        }
    }
}