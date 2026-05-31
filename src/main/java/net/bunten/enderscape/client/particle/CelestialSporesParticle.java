package net.bunten.enderscape.client.particle;

import net.bunten.enderscape.client.registry.EnderscapeParticleProviders;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class CelestialSporesParticle extends SingleQuadParticle {

    protected CelestialSporesParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites.first());
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
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public int getLightCoords(float tint) {
        return Math.max(EnderscapeParticleProviders.scaledLight(0.1F), super.getLightCoords(tint));
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new CelestialSporesParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}