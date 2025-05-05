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
    public int getLightColor(float delta) {
        BlockPos pos = BlockPos.containing(x, y, z);
        int color = level.hasChunkAt(pos) ? LevelRenderer.getLightColor(level, pos) : 0;

        return Math.max(160, color);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new EnderPearlParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}