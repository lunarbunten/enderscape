package net.bunten.enderscape.client.world;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.*;

import java.lang.Math;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import static net.minecraft.client.renderer.RenderPipelines.MATRICES_PROJECTION_SNIPPET;

/**
 * Based on BetterEnd and Eden Ring skybox renderers
 * Credits to paulevs!
 */
@Environment(EnvType.CLIENT)
public class EnderscapeSkybox {

    public static final Axis SKY_ROTATION_AXIS = Axis.YP;

    private record NebulaData(GpuBuffer buffer, int indexCount) {
    }

    public static float fogStartDensity = 1.0F;
    public static float fogEndDensity = 1.0F;
    public static Vector4f nebulaColor = new Vector4f(0, 0, 0, 0);
    public static Vector4f starColor = new Vector4f(0, 0, 0, 0);

    public static final RenderPipeline NEBULAE_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(MATRICES_PROJECTION_SNIPPET)
                    .withLocation(Enderscape.id("pipeline/nebulae"))
                    .withVertexShader("core/position_tex")
                    .withFragmentShader("core/position_tex")
                    .withSampler("Sampler0")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withDepthWrite(false)
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
                    .build()
    );

    private static final RenderSystem.AutoStorageIndexBuffer starIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
    private static int starIndexCount;

    private static final NebulaData sky = createSkyBuffer();
    private static final NebulaData nebula1 = createNebulaeBuffer(16, 64, 60, 2);
    private static final NebulaData nebula2 = createNebulaeBuffer(16, 64, 60, 3);
    private static final GpuBuffer stars = createStarsBuffer(1500, 0.05F, 0.25F);

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
        ).scale(EndFlashParameters.skyboxBrightness()).scale(gammaFactor());

        return new Vector4f((float) skyColor.x(), (float) skyColor.y(), (float) skyColor.z(), 1.0F);
    }

    public static void render(PoseStack pose, ClientLevel level, Camera camera, DeltaTracker tracker) {
        float gameTime = (level.getGameTime() + tracker.getGameTimeDeltaTicks()) % 360000;
        float baseSpeed = gameTime * 0.00003F;

        renderSkybox(pose, computeSkyColor(camera, level), baseSpeed);
        renderNebulae(pose, nebulaColor, baseSpeed * 5);
        renderStars(pose, starColor, baseSpeed * 10);
    }

    private static NebulaData createSkyBuffer() {
        GpuBuffer buffer;
        try (ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(24 * DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

            for (int i = 0; i < 6; i++) {
                Matrix4f matrix4f = new Matrix4f();
                switch (i) {
                    case 1:
                        matrix4f.rotationX((float) (Math.PI / 2));
                        break;
                    case 2:
                        matrix4f.rotationX((float) (-Math.PI / 2));
                        break;
                    case 3:
                        matrix4f.rotationX((float) Math.PI);
                        break;
                    case 4:
                        matrix4f.rotationZ((float) (Math.PI / 2));
                        break;
                    case 5:
                        matrix4f.rotationZ((float) (-Math.PI / 2));
                }

                bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F).setColor(0xFFFFFFFF);
                bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, 100.0F).setUv(0.0F, 16.0F).setColor(0xFFFFFFFF);
                bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, 100.0F).setUv(16.0F, 16.0F).setColor(0xFFFFFFFF);
                bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, -100.0F).setUv(16.0F, 0.0F).setColor(0xFFFFFFFF);
            }

            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                buffer = RenderSystem.getDevice().createBuffer(() -> "End sky vertex buffer", 40, mesh.vertexBuffer());
                return new NebulaData(buffer, mesh.drawState().indexCount());
            }
        }
    }

    private static void renderSkybox(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(Enderscape.id("textures/environment/sky.png"));
        texture.setUseMipmaps(false);

        RenderSystem.AutoStorageIndexBuffer buffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);

        GpuTextureView colorView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
        GpuTextureView textureView = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
        GpuBufferSlice slice = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrix(), color, new Vector3f(), new Matrix4f(), 0.0F);

        GpuBuffer skyBuffer = buffer.getBuffer(sky.indexCount());
        VertexFormat.IndexType type = buffer.type();

        try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky box", colorView, OptionalInt.empty(), textureView, OptionalDouble.empty())) {
            pass.setPipeline(RenderPipelines.END_SKY);
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", slice);
            pass.bindSampler("Sampler0", texture.getTextureView());
            pass.setVertexBuffer(0, sky.buffer());
            pass.setIndexBuffer(skyBuffer, type);
            pass.drawIndexed(0, 0, sky.indexCount(), 1);
        }

        matrix.popMatrix();
    }

    private static NebulaData createNebulaeBuffer(double minSize, double maxSize, int count, long seed) {
        RandomSource random = new LegacyRandomSource(seed);

        GpuBuffer buffer;
        try (ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(DefaultVertexFormat.POSITION_TEX.getVertexSize() * count * 4)) {
            BufferBuilder builder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

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

            try (MeshData mesh = builder.buildOrThrow()) {
                buffer = RenderSystem.getDevice().createBuffer(() -> "Nebulae vertex buffer", 40, mesh.vertexBuffer());
                return new NebulaData(buffer, mesh.drawState().indexCount());
            }
        }
    }

    private static void renderNebulae(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        drawIndividualNebulae(nebula1, color, Enderscape.id("textures/environment/nebula1.png"));
        drawIndividualNebulae(nebula2, color, Enderscape.id("textures/environment/nebula2.png"));

        matrix.popMatrix();
    }

    private static void drawIndividualNebulae(NebulaData data, Vector4f color, ResourceLocation id) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        GpuTextureView colorView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
        GpuTextureView depthView = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(id);
        texture.setUseMipmaps(false);

        GpuBufferSlice slice = RenderSystem.getDynamicUniforms().writeTransform(matrix, color, new Vector3f(), new Matrix4f(), 0.0F);

        RenderSystem.AutoStorageIndexBuffer buffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        GpuBuffer nebulaeBuffer = buffer.getBuffer(data.indexCount());
        VertexFormat.IndexType type = buffer.type();

        try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky nebulae", colorView, OptionalInt.empty(), depthView, OptionalDouble.empty())) {
            pass.setPipeline(NEBULAE_PIPELINE);
            pass.setUniform("DynamicTransforms", slice);
            pass.bindSampler("Sampler0", texture.getTextureView());
            pass.setVertexBuffer(0, data.buffer());
            pass.setIndexBuffer(nebulaeBuffer, type);
            pass.drawIndexed(0, 0, data.indexCount(), 1);
        }
    }

    private static GpuBuffer createStarsBuffer(int count, float minSize, float maxSize) {
        RandomSource random = RandomSource.create(10842L);
        float distance = 100.0F;

        GpuBuffer buffer;
        try (ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * count * 4)) {
            BufferBuilder buf = new BufferBuilder(builder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

            for (int i = 0; i < count; i++) {
                float x = random.nextFloat() * 2.0F - 1.0F;
                float y = random.nextFloat() * 2.0F - 1.0F;
                float z = random.nextFloat() * 2.0F - 1.0F;
                float size = Mth.randomBetween(random, minSize, maxSize);
                float lenSq = Mth.lengthSquared(x, y, z);

                if (!(lenSq <= 0.010000001F) && !(lenSq >= 1.0F)) {
                    Vector3f dir = new Vector3f(x, y, z).normalize(distance);
                    float rotation = (float) (random.nextDouble() * Math.PI * 2.0);

                    Matrix3f mat = new Matrix3f().rotateTowards(new Vector3f(dir).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-rotation);

                    buf.addVertex(new Vector3f(size, -size, 0.0F).mul(mat).add(dir));
                    buf.addVertex(new Vector3f(size, size, 0.0F).mul(mat).add(dir));
                    buf.addVertex(new Vector3f(-size, size, 0.0F).mul(mat).add(dir));
                    buf.addVertex(new Vector3f(-size, -size, 0.0F).mul(mat).add(dir));
                }
            }

            try (MeshData mesh = buf.buildOrThrow()) {
                starIndexCount = mesh.drawState().indexCount();
                buffer = RenderSystem.getDevice().createBuffer(() -> "Stars vertex buffer", 40, mesh.vertexBuffer());
            }
        }

        return buffer;
    }

    private static void renderStars(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        GpuTextureView colorView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
        GpuTextureView depthView = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
        GpuBufferSlice slice = RenderSystem.getDynamicUniforms().writeTransform(matrix, color, new Vector3f(), new Matrix4f(), 0.0F);

        GpuBuffer starBuffer = starIndices.getBuffer(starIndexCount);
        VertexFormat.IndexType type = starIndices.type();

        try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky stars", colorView, OptionalInt.empty(), depthView, OptionalDouble.empty())) {
            pass.setPipeline(RenderPipelines.STARS);
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", slice);
            pass.setVertexBuffer(0, stars);
            pass.setIndexBuffer(starBuffer, type);
            pass.drawIndexed(0, 0, starIndexCount, 1);
        }

        matrix.popMatrix();
    }
}