package net.bunten.enderscape.client.particle;

import net.bunten.enderscape.client.registry.EnderscapeParticleProviders;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class CorruptSporesParticle extends SingleQuadParticle {

    private static final float MINIMUM_OPACITY = 0.125F;

    protected CorruptSporesParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites.first());
        setSpriteFromAge(sprites);

        alpha = MINIMUM_OPACITY;

        hasPhysics = true;
        gravity = -0.005F;
        lifetime = 120;

        quadSize = 0.015F;
    }

    public void tick() {
        xd += Mth.sin(age * 0.4F) * 0.01F;
        zd += Mth.sin(age * 0.4F + Mth.PI) * 0.01F;

        int light = EnderscapeParticleProviders.blockLightAt(level, BlockPos.containing(x, y, z), 0);

        float delta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
        float flashIntensity = level.endFlashState() != null ? level.endFlashState().getIntensity(delta) : 0;

        float newAlpha = Math.min(1, (light / 15.0F) + (flashIntensity / 2.0F));

        alpha = Mth.lerp(0.15F, alpha, Math.max(MINIMUM_OPACITY, newAlpha));

        super.tick();
    }

    public int getLightColor(float tint) {
        return EnderscapeParticleProviders.scaledLight(1.0F);
    }

    @Override
    public void extract(QuadParticleRenderState quadParticleRenderState, Camera camera, float f) {
        if (alpha >= 0.1F) super.extract(quadParticleRenderState, camera, f);
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