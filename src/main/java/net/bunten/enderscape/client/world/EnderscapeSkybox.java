package net.bunten.enderscape.client.world;

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
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
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.lang.Math;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import static net.bunten.enderscape.registry.EnderscapeEnvironmentAttributes.*;

/**
 * Based on BetterEnd and Eden Ring skybox renderers
 * Credits to paulevs!
 */
@Environment(EnvType.CLIENT)
public class EnderscapeSkybox {

    public static final Axis SKY_ROTATION_AXIS = Axis.YP;

    private record NebulaData(GpuBufferSlice buffer, int indexCount) {
    }

    public static final RenderPipeline NEBULAE_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder()
                    .withLocation(Enderscape.id("pipeline/nebulae"))
                    .withVertexShader("core/position_tex")
                    .withFragmentShader("core/position_tex")
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
                    .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
                    .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
                    .withPrimitiveTopology(PrimitiveTopology.QUADS)
                    .build()
    );

    private static final RenderSystem.AutoStorageIndexBuffer starIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
    private static int starIndexCount;

    private static final NebulaData sky = createSkyBuffer();
    private static final NebulaData nebula1 = createNebulaeBuffer(16, 64, 60, 2);
    private static final NebulaData nebula2 = createNebulaeBuffer(16, 64, 60, 3);
    private static final GpuBufferSlice stars = createStarsBuffer(1500, 0.05F, 0.25F);

    private static AbstractTexture skyTexture;
    private static AbstractTexture nebulaTexture1;
    private static AbstractTexture nebulaTexture2;

    public static float gammaFactor() {
        if (!EnderscapeConfig.getInstance().skyboxScalesBrightnessWithGamma) return 1.0F;

        float gamma = Minecraft.getInstance().options.gamma().get().floatValue();
        float scale = ((float) EnderscapeConfig.getInstance().skyboxBrightnessScaleFactor) / 100.0F;

        return 1.0F + (gamma * scale);
    }

    private static Vector4f computeSkyColor(Camera camera, float partial) {
        Vector3f skyColor = ARGB.vector3fFromRGB24(camera.attributeProbe().getValue(EnvironmentAttributes.SKY_COLOR, partial));
        skyColor = skyColor.mul(gammaFactor()).mul(EndFlashParameters.skyboxBrightness());

        return new Vector4f(skyColor.x(), skyColor.y(), skyColor.z(), 1.0F);
    }

    public static void render(PoseStack pose, ClientLevel level, Camera camera, DeltaTracker tracker) {
        if (skyTexture == null) {
            skyTexture = getTexture(Minecraft.getInstance().getTextureManager(), Enderscape.id("textures/environment/sky.png"));
            nebulaTexture1 = getTexture(Minecraft.getInstance().getTextureManager(), Enderscape.id("textures/environment/nebula1.png"));
            nebulaTexture2 = getTexture(Minecraft.getInstance().getTextureManager(), Enderscape.id("textures/environment/nebula2.png"));
        }

        float partial = tracker.getGameTimeDeltaPartialTick(false);
        float gameTime = (level.getGameTime() + tracker.getGameTimeDeltaTicks()) % 360000;
        float baseSpeed = gameTime * 0.00003F;

        renderSkybox(pose, computeSkyColor(camera, partial), baseSpeed);
        renderNebulae(pose, createVector4fColor(camera, partial, NEBULA_COLOR, NEBULA_ALPHA), baseSpeed * 5);
        renderStars(pose, createVector4fColor(camera, partial, STAR_COLOR, STAR_ALPHA), baseSpeed * 10);
    }

    @NotNull
    private static Vector4f createVector4fColor(Camera camera, float partial, EnvironmentAttribute<Integer> main, EnvironmentAttribute<Float> alpha) {
        return new Vector4f(ARGB.vector3fFromRGB24(camera.attributeProbe().getValue(main, partial).intValue()), camera.attributeProbe().getValue(alpha, partial).floatValue());
    }

    private static NebulaData createSkyBuffer() {
        GpuBuffer buffer;
        try (ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(24 * DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

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
                return new NebulaData(buffer.slice(), mesh.drawState().indexCount());
            }
        }
    }

    private static void renderSkybox(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        RenderSystem.AutoStorageIndexBuffer buffer = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);

        GpuTextureView colorView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();
        GpuTextureView textureView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTextureView();
        GpuBufferSlice slice = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy(), color, new Vector3f(), new Matrix4f());

        GpuBuffer skyBuffer = buffer.getBuffer(sky.indexCount());
        IndexType type = buffer.type();

        try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky box", colorView, Optional.empty(), textureView, OptionalDouble.empty())) {
            pass.setPipeline(RenderPipelines.END_SKY);
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", slice);
            pass.bindTexture("Sampler0", skyTexture.getTextureView(), skyTexture.getSampler());
            pass.setVertexBuffer(0, sky.buffer());
            pass.setIndexBuffer(skyBuffer, type);
            pass.drawIndexed(sky.indexCount(), 1, 0, 0, 0);
        }

        matrix.popMatrix();
    }

    private static NebulaData createNebulaeBuffer(double minSize, double maxSize, int count, long seed) {
        RandomSource random = new LegacyRandomSource(seed);

        GpuBuffer buffer;
        try (ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(DefaultVertexFormat.POSITION_TEX.getVertexSize() * count * 4)) {
            BufferBuilder builder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX);

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
                return new NebulaData(buffer.slice(), mesh.drawState().indexCount());
            }
        }
    }

    private static void renderNebulae(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        drawIndividualNebulae(nebula1, color, nebulaTexture1);
        drawIndividualNebulae(nebula2, color, nebulaTexture2);

        matrix.popMatrix();
    }

    private static void drawIndividualNebulae(NebulaData data, Vector4f color, AbstractTexture texture) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        GpuTextureView colorView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();
        GpuTextureView depthView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTextureView();

        GpuBufferSlice slice = RenderSystem.getDynamicUniforms().writeTransform(matrix, color, new Vector3f(), new Matrix4f());

        RenderSystem.AutoStorageIndexBuffer buffer = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
        GpuBuffer nebulaeBuffer = buffer.getBuffer(data.indexCount());
        IndexType type = buffer.type();

        try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky nebulae", colorView, Optional.empty(), depthView, OptionalDouble.empty())) {
            pass.setPipeline(NEBULAE_PIPELINE);
            pass.setUniform("DynamicTransforms", slice);
            pass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());
            pass.setVertexBuffer(0, data.buffer());
            pass.setIndexBuffer(nebulaeBuffer, type);
            pass.drawIndexed(data.indexCount(), 1, 0, 0, 0);
        }
    }

    private static GpuBufferSlice createStarsBuffer(int count, float minSize, float maxSize) {
        RandomSource random = RandomSource.create(10842L);
        float distance = 100.0F;

        GpuBuffer buffer;
        try (ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * count * 4)) {
            BufferBuilder buf = new BufferBuilder(builder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);

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

        return buffer.slice();
    }

    private static void renderStars(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        GpuTextureView colorView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();
        GpuTextureView depthView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTextureView();
        GpuBufferSlice slice = RenderSystem.getDynamicUniforms().writeTransform(matrix, color, new Vector3f(), new Matrix4f());

        GpuBuffer starBuffer = starIndices.getBuffer(starIndexCount);
        IndexType type = starIndices.type();

        try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky stars", colorView, Optional.empty(), depthView, OptionalDouble.empty())) {
            pass.setPipeline(RenderPipelines.STARS);
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", slice);
            pass.setVertexBuffer(0, stars);
            pass.setIndexBuffer(starBuffer, type);
            pass.drawIndexed(starIndexCount, 1, 0, 0, 0);
        }

        matrix.popMatrix();
    }

    private static AbstractTexture getTexture(TextureManager manager, Identifier location) {
        return manager.getTexture(location);
    }
}