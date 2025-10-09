package net.bunten.enderscape.client.particle;

import net.bunten.enderscape.particle.MagniaParticleOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class EntityAffectedByMagniaParticle extends SimpleAnimatedParticle {

    private EntityAffectedByMagniaParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, MagniaParticleOptions options) {
        super(level, x, y, z, sprites, 0.0125F);

        gravity = 0;
        hasPhysics = false;

        quadSize = 0.05F;
        lifetime = 15;

        setColor(options.color());
        setFadeColor(options.fadeColor());

        setSpriteFromAge(sprites);
    }

    @Override
    public void move(double d, double e, double f) {
        setBoundingBox(getBoundingBox().move(d, e, f));
        setLocationFromBoundingbox();
    }

    @Override
    public int getLightColor(float tint) {
        return Math.max(150, super.getLightColor(tint));
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<MagniaParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(MagniaParticleOptions options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new EntityAffectedByMagniaParticle(level, x, y, z, sprites, options);
        }
    }
}
