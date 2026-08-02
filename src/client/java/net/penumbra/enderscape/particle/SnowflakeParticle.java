package net.penumbra.enderscape.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SnowflakeParticle extends ExplodeParticle {

    protected SnowflakeParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
        super(level, x, y, z, xa, ya, za, sprites);

        setPos(x, y + 0.1F, z);

        xd = Mth.nextFloat(random, -0.025F, 0.025F);
        yd = 0.03F;
        zd = Mth.nextFloat(random, -0.025F, 0.025F);

        lifetime = Mth.nextInt(random, 90, 120);
        quadSize = Mth.nextFloat(random, 0.07F, 0.10F);
        gravity = 0.1F;
    }

    @Override
    public void tick() {
        super.tick();

        float progress = (float) age / (float) lifetime;
        alpha = 1 - Ease.outQuart(progress);
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            sprites = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new SnowflakeParticle(level, x, y, z, xAux, yAux, zAux, sprites);
        }
    }
}