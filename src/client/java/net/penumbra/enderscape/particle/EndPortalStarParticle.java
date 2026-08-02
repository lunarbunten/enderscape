package net.penumbra.enderscape.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.penumbra.enderscape.util.ClientsideLightUtil;

@Environment(EnvType.CLIENT)
public class EndPortalStarParticle extends RisingParticle {

    private EndPortalStarParticle(ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, TextureAtlasSprite textureAtlasSprite) {
        super(level, x, y, z, velocityX, velocityY, velocityZ, textureAtlasSprite);

        float darkeningFactor = Mth.nextFloat(level.getRandom(), 0.4F, 1.0F);

        rCol *= darkeningFactor;
        gCol *= darkeningFactor;
        bCol *= darkeningFactor;

        alpha = 0.75F;
        friction = 1;
        hasPhysics = false;
        lifetime *= level.getRandom().nextInt(2, 4);
        quadSize = 0.02F;
    }

    @Override
    public void tick() {
        super.tick();

        float progress = (float) age / lifetime;
        quadSize = (1.0F - Ease.inQuart(progress)) * 0.02F;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public void move(double dx, double dy, double dz) {
        setBoundingBox(getBoundingBox().move(dx, dy, dz));
        setLocationFromBoundingbox();
    }

    @Override
    public int getLightCoords(float delta) {
        return Math.max(ClientsideLightUtil.scaledLight(0.65F), super.getLightCoords(delta));
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double vx, double vy, double vz, RandomSource randomSource) {
            return new EndPortalStarParticle(world, x, y, z, vx, vy, vz, sprites.get(randomSource));
        }
    }
}