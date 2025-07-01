package net.bunten.enderscape.client.world;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.*;

import java.lang.Math;

/**
 *  Based on BetterEnd and Eden Ring skybox renderers
 *  Credits to paulevs!
 */
public class EnderscapeSkybox {

    public static final Axis SKY_ROTATION_AXIS = Axis.YP;

    public static float fogStartDensity = 1.0F;
    public static float fogEndDensity = 1.0F;
    public static Vector4f nebulaColor = new Vector4f(0, 0, 0, 0);
    public static Vector4f starColor = new Vector4f(0, 0, 0, 0);

    private static final VertexBuffer nebula1 = createNebulaeBuffer(16, 64, 60, 2);
    private static final VertexBuffer nebula2 = createNebulaeBuffer(16, 64, 60, 3);
    private static final VertexBuffer stars = createStarsBuffer();

    public static float gammaFactor() {
        if (!EnderscapeConfig.getInstance().skyboxScalesBrightnessWithGamma) return 1.0F;

        float gamma = Minecraft.getInstance().options.gamma().get().floatValue();
        float scale = ((float) EnderscapeConfig.getInstance().skyboxBrightnessScaleFactor) / 100.0F;

        return 1.0F + (gamma * scale);
    }

    private static Vector4f computeSkyColor(Camera camera, ClientLevel level) {
        Vec3 position = camera.getPosition().subtract(2.0, 2.0, 2.0).scale(0.25);
        Vec3 skyColor = CubicSampler.gaussianSampleVec3(
                position,
                (ix, jx, k) -> Vec3.fromRGB24(level.getBiomeManager().getNoiseBiomeAtQuart(ix, jx, k).value().getSkyColor())
        ).scale(gammaFactor());

        return new Vector4f((float) skyColor.x(), (float) skyColor.y(), (float) skyColor.z(), 1.0F);
    }

    public static void render(PoseStack pose, ClientLevel level, Camera camera, DeltaTracker tracker) {
        float gameTime = (level.getGameTime() + tracker.getGameTimeDeltaTicks()) % 360000;
        float baseSpeed = gameTime * 0.00003F;

        renderSkybox(pose, computeSkyColor(camera, level), baseSpeed);
        renderNebulae(pose, nebulaColor, baseSpeed * 5);
        renderStars(pose, starColor, baseSpeed * 10);
    }

    private static void renderSkybox(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(color.x, color.y, color.z, color.w);
        RenderSystem.setShaderTexture(0, Enderscape.id("textures/environment/sky.png"));
        Tesselator tesselator = Tesselator.getInstance();

        for (int i = 0; i < 6; i++) {
            pose.pushPose();

            switch (i) {
                case 1:
                    pose.mulPose(Axis.XP.rotationDegrees(90));
                    break;
                case 2:
                    pose.mulPose(Axis.XP.rotationDegrees(-90));
                    break;
                case 3:
                    pose.mulPose(Axis.XP.rotationDegrees(180));
                    break;
                case 4:
                    pose.mulPose(Axis.ZP.rotationDegrees(90));
                    break;
                case 5:
                    pose.mulPose(Axis.ZP.rotationDegrees(-90));
                    break;
            }

            Matrix4f matrix4f = pose.last().pose();
            BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            builder.addVertex(matrix4f, -100, -100, -100).setUv(0, 0).setColor(0xFFFFFFFF);
            builder.addVertex(matrix4f, -100, -100, 100).setUv(0, 16).setColor(0xFFFFFFFF);
            builder.addVertex(matrix4f, 100, -100, 100).setUv(16, 16).setColor(0xFFFFFFFF);
            builder.addVertex(matrix4f, 100, -100, -100).setUv(16, 0).setColor(0xFFFFFFFF);
            BufferUploader.drawWithShader(builder.buildOrThrow());

            pose.popPose();
        }

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();

        matrix.popMatrix();
    }

    private static VertexBuffer createNebulaeBuffer(double minSize, double maxSize, int count, long seed) {
        VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        buffer.bind();
        buffer.upload(drawNebulae(minSize, maxSize, count, seed, Tesselator.getInstance()));
        VertexBuffer.unbind();
        return buffer;
    }

    private static MeshData drawNebulae(double minSize, double maxSize, int count, long seed, Tesselator tesselator) {
        RandomSource random = new LegacyRandomSource(seed);
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        for (int i = 0; i < count; ++i) {
            double posX = random.nextDouble() * 2.0 - 1.0;
            double posY = random.nextDouble() - 0.5;
            double posZ = random.nextDouble() * 2.0 - 1.0;
            double size = Mth.nextDouble(random, minSize, maxSize);
            double length = posX * posX + posY * posY + posZ * posZ;
            double distance = 2.0;

            if (length < 1.0 && length > 0.001) {
                length = distance / Math.sqrt(length);
                size *= distance;
                posX *= length;
                posY *= length;
                posZ *= length;

                double px = posX * 100.0;
                double py = posY * 100.0;
                double pz = posZ * 100.0;

                double angle = Math.atan2(posX, posZ);
                double sin1 = Math.sin(angle);
                double cos1 = Math.cos(angle);
                angle = Math.atan2(Math.sqrt(posX * posX + posZ * posZ), posY);
                double sin2 = Math.sin(angle);
                double cos2 = Math.cos(angle);
                angle = random.nextDouble() * Math.PI * 2.0;
                double sin3 = Math.sin(angle);
                double cos3 = Math.cos(angle);

                for (int index = 0; index < 4; ++index) {
                    double x = (double) ((index & 2) - 1) * size;
                    double y = (double) ((index + 1 & 2) - 1) * size;
                    double aa = x * cos3 - y * sin3;
                    double ab = y * cos3 + x * sin3;
                    double dy = aa * sin2 + 0.0 * cos2;
                    double ae = 0.0 * sin2 - aa * cos2;
                    double dx = ae * sin1 - ab * cos1;
                    double dz = ab * sin1 + ae * cos1;
                    float texU = (index >> 1) & 1;
                    float texV = ((index + 1) >> 1) & 1;
                    builder.addVertex((float) (px + dx), (float) (py + dy), (float) (pz + dz)).setUv(texU, texV);
                }
            }
        }

        return builder.buildOrThrow();
    }

    private static void renderNebulae(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(color.x, color.y, color.z, color.w);
        RenderSystem.enableBlend();
        //RenderSystem.setShaderFogShape(FogParameters.NO_FOG);
        RenderSystem.setShaderTexture(0, Enderscape.id("textures/environment/nebula1.png"));

        nebula1.bind();
        nebula1.drawWithShader(matrix, RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
        VertexBuffer.unbind();

        RenderSystem.setShaderTexture(0, Enderscape.id("textures/environment/nebula2.png"));

        nebula2.bind();
        nebula2.drawWithShader(matrix, RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
        VertexBuffer.unbind();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        matrix.popMatrix();
    }

    private static VertexBuffer createStarsBuffer() {
        VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        buffer.bind();
        buffer.upload(drawStars(1500, 0.05F, 0.25F, Tesselator.getInstance()));
        VertexBuffer.unbind();
        return buffer;
    }

    private static MeshData drawStars(int count, float minSize, float maxSize, Tesselator tesselator) {
        RandomSource random = RandomSource.create(10842L);
        float scale = 100.0F;
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for (int i = 0; i < count; i++) {
            float size = Mth.randomBetween(random, minSize, maxSize);

            float x = random.nextFloat() * 2.0F - 1.0F;
            float y = random.nextFloat() * 2.0F - 1.0F;
            float z = random.nextFloat() * 2.0F - 1.0F;
            float lengthSquared = Mth.lengthSquared(x, y, z);

            if (!(lengthSquared <= 0.010000001F) && !(lengthSquared >= 1.0F)) {

                Vector3f direction = new Vector3f(x, y, z).normalize(scale);
                float rotation = (float)(random.nextDouble() * (float) Math.PI * 2.0);
                Matrix3f matrix = new Matrix3f().rotateTowards(new Vector3f(direction).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-rotation);

                builder.addVertex(new Vector3f(size, -size, 0.0F).mul(matrix).add(direction));
                builder.addVertex(new Vector3f(size, size, 0.0F).mul(matrix).add(direction));
                builder.addVertex(new Vector3f(-size, size, 0.0F).mul(matrix).add(direction));
                builder.addVertex(new Vector3f(-size, -size, 0.0F).mul(matrix).add(direction));
            }
        }

        return builder.buildOrThrow();
    }


    private static void renderStars(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(color.x, color.y, color.z, color.w);
        RenderSystem.enableBlend();
        //RenderSystem.setShaderFog(FogParameters.NO_FOG);

        stars.bind();
        stars.drawWithShader(matrix, RenderSystem.getProjectionMatrix(), RenderSystem.getShader());

        VertexBuffer.unbind();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);

        matrix.popMatrix();
    }
}