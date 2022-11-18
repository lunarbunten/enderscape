package net.bunten.enderscape.client.particles;

import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class CelestialSporesParticle extends TextureSheetParticle {

    protected CelestialSporesParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(world, x, y, z, vx, vy, vz);
        setSpriteFromAge(sprites);

        hasPhysics = true;
        gravity = -0.05F;
        lifetime = 40;

        quadSize = 0.02F;
    }

    public void tick() {
        float sin = MathUtil.sin(age * 0.4F) * 0.01F;
        xd += sin;
        zd += sin;
        if (alpha > 0.25F) {
            alpha -= 0.0075F;
        } else {
            remove();
        }
        super.tick();
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
        public Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double vx, double vy, double vz) {
            return new CelestialSporesParticle(world, x, y, z, vx, vy, vz, sprites);
        }
    }
}