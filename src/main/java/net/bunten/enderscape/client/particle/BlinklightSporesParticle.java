package net.bunten.enderscape.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class BlinklightSporesParticle extends TextureSheetParticle {

    protected BlinklightSporesParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
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

        if (alpha > 0.25F) {
            alpha -= 0.0075F;
        } else {
            remove();
        }
        super.tick();
    }

    @Override
    public int getLightColor(float delta) {
        return 0xF000F0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new BlinklightSporesParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}