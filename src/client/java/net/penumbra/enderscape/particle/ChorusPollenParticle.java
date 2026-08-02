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
public class ChorusPollenParticle extends SingleQuadParticle {

    protected ChorusPollenParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        setSpriteFromAge(sprites);

        hasPhysics = true;
        lifetime = Mth.nextInt(random, 80, 100);
        quadSize = Mth.nextFloat(random, 0.12F, 0.18F);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public int getLightCoords(float tint) {
        return Math.max(ClientsideLightUtil.scaledLight(0.25F), super.getLightCoords(tint));
    }

    @Override
    public void tick() {
        super.tick();

        float progress = (float) age / lifetime;
        alpha = 1.0F - Ease.inQuart(progress);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new ChorusPollenParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}