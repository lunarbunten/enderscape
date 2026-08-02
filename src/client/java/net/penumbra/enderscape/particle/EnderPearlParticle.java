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
public class EnderPearlParticle extends SimpleAnimatedParticle {

    EnderPearlParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, sprites, 0.1F);

        rCol *= (1 - (level.getRandom().nextFloat() * 0.6F));

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        quadSize = 0.02F;
        lifetime = random.nextInt(25, 40);
    }

    @Override
    public void tick() {
        super.tick();

        float progress = (float) age / lifetime;
        quadSize = (1.0F - Ease.inQuart(progress)) * 0.02F;
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
            return new EnderPearlParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}