package net.bunten.enderscape.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class EndTrialSpawnerExhaleParticle extends PortalParticle {
    EndTrialSpawnerExhaleParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite textureAtlasSprite) {
        super(level, x, y, z, xd, yd, zd, textureAtlasSprite);
        quadSize *= 1.5F;
        lifetime = (int)(Math.random() * 2.0) + 60;
    }

    @Override
    public void tick() {
        if (age >= (lifetime / 4)) quadSize *= 0.96F;

        xo = x;
        yo = y;
        zo = z;

        if (age++ >= lifetime || quadSize < 0.005F) {
            remove();
        } else {
            float f = (float)age / (float) lifetime;
            x = x + xd * (double) f;
            y = y + yd * (double) f;
            z = z + zd * (double) f;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new EndTrialSpawnerExhaleParticle(level, x, y, z, xd, yd, zd, sprites.get(randomSource));
        }
    }
}