package net.penumbra.enderscape.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Ease;
import net.minecraft.util.RandomSource;
import net.penumbra.enderscape.util.ClientsideLightUtil;

@Environment(EnvType.CLIENT)
public class VanillaEnderPearlParticle extends SimpleAnimatedParticle {

    VanillaEnderPearlParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, sprites, 0.1F);

        float color = random.nextFloat() * 0.6F + 0.4F;
        this.rCol = color * 0.9F;
        this.gCol = color * 0.3F;
        this.bCol = color;

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        quadSize = 0.1F;
        lifetime = 25 + random.nextInt(15);
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();

        float progress = (float) age / lifetime;
        alpha = 1.0F - Ease.inCubic(progress);
    }

    @Override
    public int getLightCoords(float delta) {
        return Math.max(ClientsideLightUtil.scaledLight(0.65F), super.getLightCoords(delta));
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new VanillaEnderPearlParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}