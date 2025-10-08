package net.bunten.enderscape.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;

@Environment(EnvType.CLIENT)
public abstract class AbstractMagniaParticle extends SingleQuadParticle {

    protected AbstractMagniaParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites.first());
        setSpriteFromAge(sprites);

        hasPhysics = false;
        friction = 0.98F;
        lifetime = 30;
        quadSize = 0.1F;

        this.xd = xd / 5;
        this.yd = yd / 5;
        this.zd = zd / 5;
    }

    @Override
    public void tick() {
        if (alpha > 0.25F) {
            alpha -= 0.015F;
        } else {
            remove();
        }
        super.tick();
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }
}
