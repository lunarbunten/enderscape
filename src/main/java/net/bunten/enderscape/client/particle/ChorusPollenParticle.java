package net.bunten.enderscape.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class ChorusPollenParticle extends TextureSheetParticle {

    protected ChorusPollenParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z);
        setSpriteFromAge(sprites);

        hasPhysics = true;
        lifetime = Mth.nextInt(random, 80, 100);
        quadSize = Mth.nextFloat(random, 0.12F, 0.18F);
        
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float tint) {
        return Math.max(60, super.getLightColor(tint));
    }

    @Override
    public void tick() {
        if (age > (lifetime / 2)) {
            if (alpha > 0.1F) {
                alpha -= 0.015F;
            } else {
                remove();
            }
        }
        super.tick();
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new ChorusPollenParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}