package net.bunten.enderscape.client.particle;

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
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class NebuliteOreParticle extends SimpleAnimatedParticle {
    NebuliteOreParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, sprites, 0);
        setSpriteFromAge(sprites);

        gravity = 0.02F;
        friction = 0.9F;
        quadSize = Mth.nextFloat(random, 0.06F, 0.12F);
        lifetime = Mth.nextInt(random, 30, 60);
        setColor(0.55F, 0.2F, 0.7F);
        alpha = Mth.nextFloat(random, 0.75F, 1);

        hasPhysics = false;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
    }

    @Override
    public void tick() {
        super.tick();
        if (!removed) {
            setSpriteFromAge(sprites);
            if (age > lifetime / 2) {
                setAlpha(1 - (float) (age - (lifetime / 2)) / lifetime);
            }

            rCol += Mth.sin(age * 0.12F) * 0.02F;
        }
    }

    @Override
    public int getLightColor(float tint) {
        int color = 0;
        BlockPos pos = BlockPos.containing(x, y, z);
        if (level.getChunk(pos) != null) {
            color = LevelRenderer.getLightColor(level, pos);
        }
        return Math.max(150, color);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new NebuliteOreParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}