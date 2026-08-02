package net.penumbra.enderscape.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.penumbra.enderscape.util.ClientsideLightUtil;

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
        super.tick();

        float progress = (float) age / lifetime;
        quadSize = (1.0F - Ease.inCubic(progress)) * 0.02F;

        float sin = Mth.sin(age * 0.4F) * 0.01F;
        xd += sin;
        zd += sin;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public int getLightCoords(float tint) {
        return Math.max(ClientsideLightUtil.scaledLight(0.1F), super.getLightCoords(tint));
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