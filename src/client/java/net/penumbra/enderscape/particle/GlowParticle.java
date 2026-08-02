package net.penumbra.enderscape.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.penumbra.enderscape.util.ClientsideLightUtil;

@Environment(EnvType.CLIENT)
public class GlowParticle extends SimpleAnimatedParticle {
    GlowParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites, GlowParticleOptions options) {
        super(level, x, y, z, sprites, 0);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        gravity = 0.02F;
        friction = options.friction();
        quadSize = Mth.nextFloat(random, 0.06F, 0.12F);

        setSpriteFromAge(sprites);
        setLifetime(options.lifetime().sample(random));
        setAlpha(1.0F);

        hasPhysics = false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!removed) {
            setSpriteFromAge(sprites);
        }
    }

    @Override
    public int getLightCoords(float delta) {
        return ClientsideLightUtil.scaledLight(1.0F);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<GlowParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(GlowParticleOptions options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource random) {
            return new GlowParticle(level, x, y, z, xd, yd, zd, sprites, options);
        }
    }
}