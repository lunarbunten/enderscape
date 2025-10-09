package net.bunten.enderscape.client.particle;

import net.bunten.enderscape.particle.MagniaParticleOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class MagniaSproutParticle extends SingleQuadParticle {

    protected final SpriteSet sprites;

    private boolean hasFade;

    private float fadeR;
    private float fadeG;
    private float fadeB;

    protected float colorFadeRate;

    protected MagniaSproutParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites, MagniaParticleOptions options) {
        super(level, x, y, z, xd, yd, zd, sprites.first());

        this.sprites = sprites;

        hasPhysics = false;
        friction = 0.98F;
        lifetime = 30;
        quadSize = 0.075F;

        setAlpha(1.0F);
        setColor(options.color());
        setFadeColor(options.fadeColor());
        colorFadeRate = options.colorFadeRate();

        this.xd = xd / 5;
        this.yd = yd / 5;
        this.zd = zd / 5;

        setSpriteFromAge(sprites);
    }

    public void setColor(int i) {
        float f = ((i & 0xFF0000) >> 16) / 255.0F;
        float g = ((i & 0xFF00) >> 8) / 255.0F;
        float h = ((i & 0xFF)) / 255.0F;
        setColor(f, g, h);
    }

    public void setFadeColor(int i) {
        fadeR = ((i & 0xFF0000) >> 16) / 255.0F;
        fadeG = ((i & 0xFF00) >> 8) / 255.0F;
        fadeB = ((i & 0xFF)) / 255.0F;
        hasFade = true;
    }

    @Override
    public void tick() {
        setSpriteFromAge(sprites);

        if (alpha > 0.25F) alpha -= 0.015F;
        if (age >= (lifetime / 2)) quadSize *= 0.95F;
        if (alpha < 0.2F || quadSize < 0.005F) remove();

        if (age > lifetime / 2) {
            if (hasFade) {
                rCol = rCol + (fadeR - rCol) * colorFadeRate;
                gCol = gCol + (fadeG - gCol) * colorFadeRate;
                bCol = bCol + (fadeB - bCol) * colorFadeRate;
            }
        }

        super.tick();
    }

    @Override
    public int getLightColor(float f) {
        return 0xF000F0;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<MagniaParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(MagniaParticleOptions options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new MagniaSproutParticle(level, x, y, z, xd, yd, zd, sprites, options);
        }
    }
}