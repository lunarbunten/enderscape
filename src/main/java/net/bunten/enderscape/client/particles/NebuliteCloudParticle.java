package net.bunten.enderscape.client.particles;

import net.bunten.enderscape.util.MathUtil;
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
import net.minecraft.util.FastColor.ARGB32;

@Environment(value=EnvType.CLIENT)
public class NebuliteCloudParticle extends SimpleAnimatedParticle {
    NebuliteCloudParticle(ClientLevel world, double d, double e, double f, double g, double h, double i, int j, SpriteSet sprites) {
        super(world, d, e, f, sprites, 0);
        setSpriteFromAge(sprites);

        gravity = 0.02F;
        friction = 0.6F;
        quadSize = MathUtil.nextFloat(random, 0.06F, 0.12F);
        lifetime = MathUtil.nextInt(random, 40, 100);
        setColor(0.55F, 0.2F, 0.7F);
        alpha = MathUtil.nextFloat(random, 0.75F, 1);

        hasPhysics = false;
        xd = g;
        yd = h;
        zd = i;
    }

    @Override
    public void tick() {
        super.tick();
        if (!removed) {
            setSpriteFromAge(sprites);
            if (age > lifetime / 2) {
                setAlpha(1 - (age - (lifetime / 2)) / lifetime);
            }

            rCol += MathUtil.sin(age * 0.12F) * 0.02F;
        }
    }

    @Override
    public int getLightColor(float tint) {
        int color = 0;
        BlockPos pos = new BlockPos(x, y, z);
        if (level.getChunk(pos) != null) {
            color = LevelRenderer.getLightColor(level, pos);
        }
        return Math.max(150, color);
    }

    @Environment(value=EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double d, double e, double f, double g, double h, double i) {
            return new NebuliteCloudParticle(world, d, e, f, g, h, i, ARGB32.color(255, 182, 28, 204), sprites);
        }
    }
}