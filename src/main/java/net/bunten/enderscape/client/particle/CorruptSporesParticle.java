package net.bunten.enderscape.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class CorruptSporesParticle extends SingleQuadParticle {

    protected CorruptSporesParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites.first());
        setSpriteFromAge(sprites);

        alpha = 0;

        hasPhysics = true;
        gravity = -0.005F;
        lifetime = 120;

        quadSize = 0.015F;
    }

    public void tick() {
        xd += Mth.sin(age * 0.4F) * 0.01F;
        zd += Mth.sin(age * 0.4F + Mth.PI) * 0.01F;

        float newAlpha = 0;
        BlockPos pos = BlockPos.containing(x, y, z);
        if (level.getChunk(pos) != null) {
            newAlpha = level.getChunkSource().getLightEngine().getRawBrightness(pos, 0) / 15.0F;
        }

        alpha = Mth.lerp(0.15F, alpha, newAlpha);

        super.tick();
    }

    @Override
    public int getLightColor(float delta) {
        return 255;
    }

    @Override
    public void extract(QuadParticleRenderState quadParticleRenderState, Camera camera, float f) {
        if (alpha >= 0.1F) {
            super.extract(quadParticleRenderState, camera, f);
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new CorruptSporesParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}