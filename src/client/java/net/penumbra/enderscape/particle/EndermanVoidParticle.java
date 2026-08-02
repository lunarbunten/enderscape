package net.penumbra.enderscape.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Ease;
import net.minecraft.util.RandomSource;
import net.penumbra.enderscape.util.ClientsideLightUtil;

@Environment(EnvType.CLIENT)
public class EndermanVoidParticle extends SingleQuadParticle {
    private final double xStart;
    private final double yStart;
    private final double zStart;

    private final float alphaStart;

    protected EndermanVoidParticle(final ClientLevel level, final double x, final double y, final double z, final double xd, final double yd, final double zd, final TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        this.x = x;
        this.y = y;
        this.z = z;

        xStart = x;
        yStart = y;
        zStart = z;

        alphaStart = alpha;

        quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
        rCol *= (1 - (level.getRandom().nextFloat() * 0.6F));
        lifetime = random.nextInt(40, 50);
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void move(final double xa, final double ya, final double za) {
        setBoundingBox(getBoundingBox().move(xa, ya, za));
        setLocationFromBoundingbox();
    }

    @Override
    public float getQuadSize(final float partialTicks) {
        float progress = (age + partialTicks) / lifetime;
        float thingy = 1.0F;

        progress = thingy - progress;
        progress *= progress;
        progress = thingy - progress;

        return (quadSize * progress) * 0.3F;
    }

    @Override
    public int getLightCoords(float delta) {
        return Math.max(ClientsideLightUtil.scaledLight(0.65F), super.getLightCoords(delta));
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        if (age++ >= lifetime) {
            remove();
        } else {
            float progress = (float) age / lifetime;
            float var3 = -progress + progress * progress * 2.0F;
            float var4 = 1.0F - var3;

            x = xStart + xd * var4;
            y = yStart + yd * var4 + (1.0F - progress);
            z = zStart + zd * var4;

            alpha = 1.0F - Ease.inCubic(progress);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(final SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(
                final SimpleParticleType options,
                final ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final RandomSource random
        ) {
            return new EndermanVoidParticle(level, x, y, z, xAux, yAux, zAux, sprite.get(random));
        }
    }
}