package net.bunten.enderscape.client.particle;

import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RustleSleepingBubbleParticle extends TextureSheetParticle {

    public RustleSleepingBubbleParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z);

        setSize(0.02F, 0.02F);
        setSpriteFromAge(sprites);

        quadSize = quadSize * (random.nextFloat() * 0.6F + 0.2F);
        lifetime = 60;

        this.xd = xd * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.yd = yd * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.zd = zd * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        if (age++ >= lifetime) {
            remove();
        } else {
            yd += 0.002;
            move(xd, yd, zd);
            xd *= 0.85F;
            yd *= 0.85F;
            zd *= 0.85F;
        }
    }

    @Override
    public void remove() {
        super.remove();
        pop();
    }

    private void pop() {
        level.addParticle(EnderscapeParticles.RUSTLE_SLEEPING_BUBBLE_POP.get(), x, y, z, 0, 0, 0);
        level.playLocalSound(x, y, z, EnderscapeEntitySounds.RUSTLE_SLEEPING_BUBBLE_POP.get(), SoundSource.AMBIENT, 1, Mth.nextFloat(random, 0.9F, 1.1F), false);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new RustleSleepingBubbleParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}