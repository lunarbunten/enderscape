package net.bunten.enderscape.client.particle;

import net.bunten.enderscape.particle.DashJumpShockwaveParticleOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class DashJumpShockwaveParticle extends SingleQuadParticle {
    private final SpriteSet sprites;
    private final DashJumpShockwaveParticleOptions options;

    protected float decayRate;
    protected float spinSpeed;

    public DashJumpShockwaveParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, DashJumpShockwaveParticleOptions options) {
        super(level, x, y, z, sprites.first());

        this.sprites = sprites;
        this.options = options;

        Vector3f velocity = options.velocity();
        xd = velocity.x();
        yd = velocity.y();
        zd = velocity.z();

        hasPhysics = true;
        gravity = 0.0F;
        friction = 0.9F;

        alpha = 1;

        Vec3 color = Vec3.fromRGB24(0xA3FFFF);

        rCol = (float) color.x;
        gCol = (float) color.y;
        bCol = (float) color.z;

        lifetime = 30;
        quadSize = 3 * options.scale();

        decayRate = 0.09F;
        spinSpeed = 0.0F;

        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();

        alpha = Mth.lerp(decayRate, alpha, 0);
        if (alpha < 0.01F) remove();

        setSpriteFromAge(sprites);
    }

    @Override
    public void extract(QuadParticleRenderState quadParticleRenderState, Camera camera, float delta) {
        super.extract(quadParticleRenderState, camera, delta);
        float spinAngle = (age + delta) * spinSpeed;

        Vector3f velocity = new Vector3f(options.velocity()).normalize();
        Vector3f up = new Vector3f(0, 1, 0);

        if (Math.abs(velocity.dot(up)) > 0.99f) up.set(1, 0, 0);

        Quaternionf rotation = new Quaternionf().rotationTo(new Vector3f(0, 0, 1), velocity);
        rotation.rotateZ(spinAngle);

        extractRotatedQuad(quadParticleRenderState, camera, rotation, delta);
        extractRotatedQuad(quadParticleRenderState, camera, new Quaternionf(rotation).rotateY((float) Math.PI), delta);

    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float tint) {
        return Math.max(50, super.getLightColor(tint));
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<DashJumpShockwaveParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(DashJumpShockwaveParticleOptions options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource randomSource) {
            return new DashJumpShockwaveParticle(level, x, y, z, sprites, options);
        }
    }
}