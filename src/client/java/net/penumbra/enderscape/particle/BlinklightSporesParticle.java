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
import net.minecraft.util.RandomSource;
import net.penumbra.enderscape.util.ClientsideLightUtil;

@Environment(EnvType.CLIENT)
public class BlinklightSporesParticle extends SingleQuadParticle {

    protected BlinklightSporesParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites.first());
        setSpriteFromAge(sprites);

        hasPhysics = true;
        gravity = -0.005F;
        lifetime = 60;

        quadSize = 0.06F;

        this.xd *= 0.1F;
        this.zd *= 0.1F;
        this.yd *= 0.12F;
    }

    public void tick() {
        super.tick();

        float progress = (float) age / lifetime;
        quadSize = (1.0F - Ease.inCubic(progress)) * 0.06F;
    }

    @Override
    public int getLightCoords(float delta) {
        return ClientsideLightUtil.scaledLight(1.0F);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new BlinklightSporesParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}