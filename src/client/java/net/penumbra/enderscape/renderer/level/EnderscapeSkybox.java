package net.penumbra.enderscape.renderer.level;

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.registry.renderer.EnderscapeRenderPipelines;
import net.penumbra.enderscape.renderer.value.EndFlashParameters;
import org.joml.*;

import java.lang.Math;
import java.util.Optional;
import java.util.OptionalDouble;

import static net.minecraft.world.attribute.EnvironmentAttributes.STAR_BRIGHTNESS;
import static net.penumbra.enderscape.registry.level.EnderscapeEnvironmentAttributes.*;

/**
 * Based on BetterEnd and Eden Ring skybox renderers
 * Credits to paulevs!
 */
@Environment(EnvType.CLIENT)
public class EnderscapeSkybox {

    public EnderscapeSkybox() {
        buffers = Buffers.create();

        TextureManager manager = Minecraft.getInstance().getTextureManager();
        skyTexture = getTexture(manager, Enderscape.id("textures/environment/sky.png"));
        nebulaTexture1 = getTexture(manager, Enderscape.id("textures/environment/nebula1.png"));
        nebulaTexture2 = getTexture(manager, Enderscape.id("textures/environment/nebula2.png"));
    }

    public static final Axis SKY_ROTATION_AXIS = Axis.YP;

    private final Buffers buffers;

    private final AbstractTexture skyTexture;
    private final AbstractTexture nebulaTexture1;
    private final AbstractTexture nebulaTexture2;

    public void render(PoseStack pose, ClientLevel level, Camera camera, DeltaTracker tracker) {
        float partialTicks = tracker.getGameTimeDeltaPartialTick(false);
        float gameTime = (level.getGameTime() + tracker.getGameTimeDeltaTicks()) % 360000;
        float baseSpeed = gameTime * 0.00003F;

        renderSkybox(pose, computeSkyColor(level, camera, partialTicks), baseSpeed);
        renderNebulae(pose, nightVisionAffectedColor(level, camera, partialTicks, NEBULA_COLOR, NEBULA_BRIGHTNESS), baseSpeed * 5);
        renderStars(pose, nightVisionAffectedColor(level, camera, partialTicks, STAR_COLOR, STAR_BRIGHTNESS), baseSpeed * 10);
    }

    private Vector4f computeSkyColor(ClientLevel level, Camera camera, float partialTicks) {
        Vector3f skyColor = ARGB.vector3fFromRGB24(camera.attributeProbe().getValue(EnvironmentAttributes.SKY_COLOR, partialTicks));
        float alpha = 1.0F;

        // Skybox alpha decreases with Night Vision intensity, as the noise background looks pretty awful when bright.

        if (camera.entity() instanceof LivingEntity mob && mob.hasEffect(MobEffects.NIGHT_VISION) && !mob.hasEffect(MobEffects.DARKNESS)) {
            alpha -= GameRenderer.nightVisionScale(mob, partialTicks);
        }

        skyColor = scaleWithoutOverflow(skyColor, gammaFactor());
        skyColor = scaleWithoutOverflow(skyColor, EndFlashParameters.skyboxBrightness());
        skyColor = applyVoidDarkness(level, camera, skyColor);

        return new Vector4f(skyColor.x(), skyColor.y(), skyColor.z(), Math.clamp(alpha, 0.0F, 1.0F));
    }

    private Vector4f nightVisionAffectedColor(ClientLevel level, Camera camera, float partialTicks, EnvironmentAttribute<Integer> colorAttribute, EnvironmentAttribute<Float> alphaAttribute) {
        Vector3f color = ARGB.vector3fFromRGB24(camera.attributeProbe().getValue(colorAttribute, partialTicks));
        Vector3f scaledColor = scaleToMaximum(color, getNightVisionScale(camera, partialTicks));
        scaledColor = applyVoidDarkness(level, camera, scaledColor);

        float alpha = camera.attributeProbe().getValue(alphaAttribute, partialTicks);

        return new Vector4f(scaledColor, alpha);
    }

    public static Vector3f scaleWithoutOverflow(Vector3f color, float factor) {
        float x = color.x() * factor;
        float y = color.y() * factor;
        float z = color.z() * factor;

        float maximum = Math.max(x, Math.max(y, z));

        if (maximum > 1.0F) {
            float scale = 1.0F / maximum;

            x *= scale;
            y *= scale;
            z *= scale;
        }

        return new Vector3f(x, y, z);
    }

    public static float gammaFactor() {
        float factor = EnderscapeConfig.getInstance().skyboxBrightnessScaleFactor;

        if (factor <= 0.0F) {
            return 1.0F;
        } else {
            float gamma = Minecraft.getInstance().options.gamma().get().floatValue();
            float scale = factor / 100.0F;

            return 1.0F + (gamma * scale);
        }
    }

    private static Vector3f applyVoidDarkness(ClientLevel level, Camera camera, Vector3f color) {
        float voidDarknessOnsetRange = level.getLevelData().voidDarknessOnsetRange();

        if (voidDarknessOnsetRange <= 0.0F) {
            return color;
        } else {
            float darkness = Mth.clamp((voidDarknessOnsetRange + level.getMinY() - (float) camera.position().y) / voidDarknessOnsetRange, 0.0F, 1.0F);
            color = color.mul(1.0F - darkness);
            return color;
        }
    }

    public static Vector3f scaleToMaximum(Vector3f color, float factor) {
        float maximum = Math.max(color.x(), Math.max(color.y(), color.z()));
        if (maximum <= 0.0F) return color;

        float scale = 1.0F + factor * ((1.0F / maximum) - 1.0F);
        return color.mul(scale);
    }

    private static float getNightVisionScale(Camera camera, float partialTicks) {
        if (camera.entity() instanceof LivingEntity mob && mob.hasEffect(MobEffects.NIGHT_VISION) && !mob.hasEffect(MobEffects.DARKNESS)) {
            return GameRenderer.nightVisionScale(mob, partialTicks);
        }
        return 0.0F;
    }

    private static BufferData createSkyBuffer() {
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
                return new BufferData(buffer, mesh.drawState().indexCount());
            }
        }
    }

    private void renderSkybox(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        RenderSystem.AutoStorageIndexBuffer buffer = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);

        GpuTextureView colorView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();
        GpuTextureView textureView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTextureView();
        GpuBufferSlice slice = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewStack(), color, new Vector3f(), new Matrix4f());

        GpuBuffer skyBuffer = buffer.getBuffer(buffers.sky().indexCount());
        IndexType type = buffer.type();

        try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky box", colorView, Optional.empty(),textureView, OptionalDouble.empty())) {
            pass.setPipeline(RenderPipelines.END_SKY);
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", slice);
            pass.bindTexture("Sampler0", skyTexture.getTextureView(), skyTexture.getSampler());
            pass.setVertexBuffer(0, buffers.sky().buffer().slice());
            pass.setIndexBuffer(skyBuffer, type);
            pass.drawIndexed(buffers.sky().indexCount(), 1, 0, 0, 0);
        }

        matrix.popMatrix();
    }

    private static BufferData createNebulaeBuffer(double minSize, double maxSize, int count, long seed) {
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
                return new BufferData(buffer, mesh.drawState().indexCount());
            }
        }
    }

    private void renderNebulae(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        drawIndividualNebulae(buffers.nebula1(), color, nebulaTexture1);
        drawIndividualNebulae(buffers.nebula2(), color, nebulaTexture2);

        matrix.popMatrix();
    }

    private void drawIndividualNebulae(BufferData data, Vector4f color, AbstractTexture texture) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        GpuTextureView colorView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();
        GpuTextureView depthView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTextureView();

        GpuBufferSlice slice = RenderSystem.getDynamicUniforms().writeTransform(matrix, color, new Vector3f(), new Matrix4f());

        RenderSystem.AutoStorageIndexBuffer buffer = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
        GpuBuffer nebulaeBuffer = buffer.getBuffer(data.indexCount());
        IndexType type = buffer.type();

        try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky nebulae", colorView, Optional.empty(), depthView, OptionalDouble.empty())) {
            pass.setPipeline(EnderscapeRenderPipelines.NEBULAE_PIPELINE);
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", slice);
            pass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());
            pass.setVertexBuffer(0, data.buffer().slice());
            pass.setIndexBuffer(nebulaeBuffer, type);
            pass.drawIndexed(data.indexCount(), 1, 0, 0, 0);
        }
    }

    private static BufferData createStarsBuffer(int count, float minSize, float maxSize) {
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
                buffer = RenderSystem.getDevice().createBuffer(() -> "Stars vertex buffer", 40, mesh.vertexBuffer());
                return new BufferData(buffer, mesh.drawState().indexCount());
            }
        }
    }

    private void renderStars(PoseStack pose, Vector4f color, float angle) {
        Matrix4fStack matrix = RenderSystem.getModelViewStack();

        matrix.pushMatrix();
        matrix.mul(pose.last().pose());
        matrix.mul(new Matrix4f().rotation(SKY_ROTATION_AXIS.rotation(angle)));

        GpuTextureView colorView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();
        GpuTextureView depthView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTextureView();
        GpuBufferSlice slice = RenderSystem.getDynamicUniforms().writeTransform(matrix, color, new Vector3f(), new Matrix4f());

        RenderSystem.AutoStorageIndexBuffer buffer = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
        GpuBuffer starBuffer = buffer.getBuffer(buffers.stars().indexCount());
        IndexType type = buffer.type();

        try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky stars", colorView, Optional.empty(), depthView, OptionalDouble.empty())) {
            pass.setPipeline(RenderPipelines.STARS);
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", slice);
            pass.setVertexBuffer(0, buffers.stars().buffer().slice());
            pass.setIndexBuffer(starBuffer, type);
            pass.drawIndexed(buffers.stars().indexCount(), 1, 0, 0, 0);
        }

        matrix.popMatrix();
    }

    private AbstractTexture getTexture(TextureManager manager, Identifier location) {
        return manager.getTexture(location);
    }

    public void close() {
        buffers.close();
    }

    private record BufferData(GpuBuffer buffer, int indexCount) {
    }

    private record Buffers(BufferData sky, BufferData nebula1, BufferData nebula2, BufferData stars) {
        private static Buffers create() {
            return new Buffers(
                    createSkyBuffer(),
                    createNebulaeBuffer(16, 64, 60, 2),
                    createNebulaeBuffer(16, 64, 60, 3),
                    createStarsBuffer(1500, 0.05F, 0.25F)
            );
        }

        public void close() {
            sky.buffer().close();
            nebula1.buffer().close();
            nebula2.buffer().close();
            stars.buffer().close();
        }
    }
}