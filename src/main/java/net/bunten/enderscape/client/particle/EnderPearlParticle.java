package net.bunten.enderscape.client.particle;

import net.bunten.enderscape.client.registry.EnderscapeParticleProviders;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class EnderPearlParticle extends SimpleAnimatedParticle {

    EnderPearlParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
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
        if (age >= (lifetime / 2)) {
            quadSize *= 0.95F;
        }

        if (quadSize < 0.005F) remove();

        super.tick();
    }

    @Override
    public int getLightCoords(float delta) {
        return Math.max(EnderscapeParticleProviders.scaledLight(0.65F), super.getLightCoords(delta));
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