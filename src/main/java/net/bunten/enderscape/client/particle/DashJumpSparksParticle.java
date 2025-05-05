package net.bunten.enderscape.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class DashJumpSparksParticle extends SimpleAnimatedParticle {

    DashJumpSparksParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, sprites, 0.1F);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        quadSize = 0.02F;
        lifetime = 48 + random.nextInt(12);
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
    public void render(VertexConsumer vertexConsumer, Camera camera, float f) {
        if (age < lifetime / 3 || (age + lifetime) / 3 % 2 == 0) {
            super.render(vertexConsumer, camera, f);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double d, double xd, double yd, double zd) {
            return new DashJumpSparksParticle(level, x, y, d, xd, yd, zd, sprites);
        }
    }
}