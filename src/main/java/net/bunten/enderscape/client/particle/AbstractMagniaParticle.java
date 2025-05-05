package net.bunten.enderscape.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

@Environment(EnvType.CLIENT)
public abstract class AbstractMagniaParticle extends TextureSheetParticle {

    protected AbstractMagniaParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}