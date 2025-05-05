package net.bunten.enderscape.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class VeiledLeavesParticle extends TextureSheetParticle {
    private float rotSpeed;
    private final float spinAcceleration;
    private final float windBig;
    private final boolean swirl;
    private final boolean flowAway;
    private final double xaFlowScale;
    private final double zaFlowScale;
    private final double swirlPeriod;

    protected VeiledLeavesParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float gravity, float windBig, boolean swirl, boolean flowAway, float i) {
        super(level, x, y, z);

        setSprite(sprites.get(random.nextInt(12), 12));

        this.flowAway = flowAway;
        this.gravity = gravity * 1.2F * 0.0025F;
        this.swirl = swirl;
        this.windBig = windBig;

        friction = 1.0F;
        lifetime = 300;
        rotSpeed = (float) Math.toRadians(random.nextBoolean() ? -30.0 : 30.0);
        spinAcceleration = (float)Math.toRadians(random.nextBoolean() ? -5.0 : 5.0);

        float particleRandom = random.nextFloat();
        swirlPeriod = Math.toRadians(1000.0F + particleRandom * 3000.0F);
        xaFlowScale = Math.cos(Math.toRadians(particleRandom * 60.0F)) * (double) this.windBig;
        zaFlowScale = Math.sin(Math.toRadians(particleRandom * 60.0F)) * (double) this.windBig;

        float size = i * (random.nextBoolean() ? 0.05F : 0.075F);
        quadSize = size;
        setSize(size, size);

        xd = 0.05;
        yd = random.nextGaussian() * 0.025 + 0.03;
        zd = 0.05;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (lifetime-- <= 0) {
            remove();
        }

        if (!removed) {
            float f = (float)(300 - lifetime);
            float g = Math.min(f / 300.0F, 1.0F);
            double d = 0.0;
            double e = 0.0;
            if (flowAway) {
                d += xaFlowScale * Math.pow(g, 1.25);
                e += zaFlowScale * Math.pow(g, 1.25);
            }

            if (swirl) {
                d += (double)g * Math.cos((double)g * swirlPeriod) * (double)windBig;
                e += (double)g * Math.sin((double)g * swirlPeriod) * (double)windBig;
            }

            xd += d * 0.0025F;
            zd += e * 0.0025F;
            yd = yd - (double)gravity;
            rotSpeed = rotSpeed + spinAcceleration / 20.0F;
            oRoll = roll;
            roll = roll + rotSpeed / 20.0F;
            move(xd, yd, zd);
            if (onGround || lifetime < 299 && (xd == 0.0 || zd == 0.0)) {
                remove();
            }

            if (!removed) {
                xd = xd * (double)friction;
                yd = yd * (double)friction;
                zd = zd * (double)friction;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new VeiledLeavesParticle(level, x, y, z, sprites, 0.25F, 2.0F, false, true, 1.0F);
        }
    }
}