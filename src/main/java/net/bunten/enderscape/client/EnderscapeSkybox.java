package net.bunten.enderscape.client;

import org.betterx.bclib.util.BackgroundInfo;
import org.betterx.bclib.util.MHelper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.world.EnderscapeBiomes;
import net.bunten.enderscape.world.biomes.EnderscapeBiome;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.SkyRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.phys.Vec3;

/**
 *  Based on BetterEnd and Eden Ring skybox renderers
 *  Credits to paulevs!
 */
@Environment(EnvType.CLIENT)
public class EnderscapeSkybox implements SkyRenderer {

    private boolean init = false;

    private static BufferBuilder builder;

    private VertexBuffer nebula1;
    private VertexBuffer nebula2;
    private VertexBuffer stars;
    private VertexBuffer sky;

    private void init() {
        builder = Tesselator.getInstance().getBuilder();

        sky = buildSkybox(sky);
        nebula1 = buildNebula(nebula1, 16, 64, 60, 2);
        nebula2 = buildNebula(nebula2, 16, 64, 60, 3);
        stars = buildStars(stars, 0.1, 0.3, 1000, 1);
        init = true;
    }

    private ResourceLocation getSkyTexture(boolean override) {
        return override ? Enderscape.id("textures/environment/sky.png") : new ResourceLocation("textures/environment/end_sky.png");
    }

    private Vec3 getSkyColor(WorldRenderContext context) {
        Vec3 vec3 = context.camera().getPosition().subtract(2, 2, 2).scale(0.25);
        return CubicSampler.gaussianSampleVec3(vec3, (i, j, k) -> {
            Holder<Biome> holder = context.world().getBiomeManager().getNoiseBiomeAtQuart(i, j, k);
            int color = holder.is(EnderscapeBiomes.ALL_BIOMES) ? holder.value().getSkyColor() : EnderscapeBiome.DEFAULT_SKY_COLOR;
            return Vec3.fromRGB24(color);
        });
    }

    @Override
    public void render(WorldRenderContext context) {
        if (context.world() == null || context.matrixStack() == null) return;
        if (!init) init();

        float alpha = 1 - BackgroundInfo.blindness;

        if (Config.CLIENT.modifySkybox()) {
            float time = ((context.world().getDayTime() + context.tickDelta()) % 360000) * 0.00004F;
            Vec3 color = getSkyColor(context).scale(alpha);
            
            renderSkybox(context, new Vector3f(color), Config.CLIENT.overrideVanillaSkyTexture());
            renderNebula(time, alpha, context);
            renderStars(time, alpha, context);
            
        } else {
            Vec3 color = new Vec3(0.15F, 0.15F, 0.15F).scale(alpha);
            renderSkybox(context, new Vector3f(color), false);
        }
    }

    private void renderSkybox(WorldRenderContext context, Vector3f color, boolean override) {
        PoseStack pose = context.matrixStack();
        Matrix4f project = context.projectionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

        for (int i = 0; i < 6; i++) {
            pose.pushPose();

            switch(i) {
                case 1:
                    pose.mulPose(Vector3f.XP.rotationDegrees(90));
                    break;
                case 2:
                    pose.mulPose(Vector3f.XP.rotationDegrees(-90));
                    break;
                case 3:
                    pose.mulPose(Vector3f.XP.rotationDegrees(180));
                    break;
                case 4:
                    pose.mulPose(Vector3f.ZP.rotationDegrees(90));
                    break;
                case 5:
                    pose.mulPose(Vector3f.ZP.rotationDegrees(-90));
                    break;
            }

            RenderSystem.setShaderTexture(0, getSkyTexture(override));
            RenderSystem.setShaderColor(color.x(), color.y(), color.z(), 1);
            sky.bind();
            sky.drawWithShader(pose.last().pose(), project, GameRenderer.getPositionTexShader());
            VertexBuffer.unbind();
            pose.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private void renderNebula(float time, float alpha, WorldRenderContext context) {
        PoseStack pose = context.matrixStack();
        Matrix4f project = context.projectionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

        if (alpha > 0) {
            pose.pushPose();
            pose.mulPose(new Quaternion(time, time, time, false));
            RenderSystem.setShaderTexture(0, Enderscape.id("textures/environment/nebula1.png"));
            RenderSystem.setShaderColor(SkyInfo.nebula[0], SkyInfo.nebula[1], SkyInfo.nebula[2], alpha * SkyInfo.nebula[3]);
            nebula1.bind();
            nebula1.drawWithShader(pose.last().pose(), project, GameRenderer.getPositionTexShader());
            VertexBuffer.unbind();
            pose.popPose();

            pose.pushPose();
            pose.mulPose(new Quaternion(time, time, time, false));
            RenderSystem.setShaderTexture(0, Enderscape.id("textures/environment/nebula2.png"));
            RenderSystem.setShaderColor(SkyInfo.nebula[0], SkyInfo.nebula[1], SkyInfo.nebula[2], alpha * SkyInfo.nebula[3]);
            nebula1.bind();
            nebula1.drawWithShader(pose.last().pose(), project, GameRenderer.getPositionTexShader());
            VertexBuffer.unbind();
            pose.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private void renderStars(float time, float alpha, WorldRenderContext context) {
        PoseStack pose = context.matrixStack();
        Matrix4f project = context.projectionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        FogRenderer.setupNoFog();

        if (alpha > 0) {
            pose.pushPose();
            pose.mulPose(new Quaternion(time, time, time, false));
            RenderSystem.setShaderColor(SkyInfo.stars[0], SkyInfo.stars[1], SkyInfo.stars[2], alpha * SkyInfo.stars[3]);
            stars.bind();
            stars.drawWithShader(pose.last().pose(), project, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
            pose.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private VertexBuffer buildSkybox(VertexBuffer buffer) {
        if (buffer != null) {
            buffer.close();
        }

        buffer = new VertexBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        builder.vertex(-100, 100, -100).uv(0, 0).endVertex();
        builder.vertex(100, 100, -100).uv(24, 0).endVertex();
        builder.vertex(100, 100, 100).uv(24, 24).endVertex();
        builder.vertex(-100, 100, 100).uv(0, 24).endVertex();

        RenderedBuffer renderedBuffer = builder.end();
        buffer.bind();
        buffer.upload(renderedBuffer);

        return buffer;
    }

    private VertexBuffer buildNebula(VertexBuffer buffer, double minSize, double maxSize, int count, long seed) {
        if (buffer != null) {
            buffer.close();
        }

        buffer = new VertexBuffer();
        RandomSource random = new LegacyRandomSource(seed);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        for (int i = 0; i < count; ++i) {
            double posX = random.nextDouble() * 2.0 - 1.0;
            double posY = random.nextDouble() - 0.5;
            double posZ = random.nextDouble() * 2.0 - 1.0;
            double size = MHelper.randRange(minSize, maxSize, random);
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
                    builder.vertex(px + dx, py + dy, pz + dz).uv(texU, texV).endVertex();
                }
            }
        }

        RenderedBuffer renderedBuffer = builder.end();
        buffer.bind();
        buffer.upload(renderedBuffer);

        return buffer;
    }

    private VertexBuffer buildStars(VertexBuffer buffer, double minSize, double maxSize, int count, long seed) {
        if (buffer != null) {
            buffer.close();
        }

        buffer = new VertexBuffer();
        RandomSource random = new LegacyRandomSource(seed);
        RenderSystem.setShader(GameRenderer::getPositionShader);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for (int i = 0; i < count; ++i) {
            double posX = random.nextDouble() * 2.0 - 1.0;
            double posY = random.nextDouble() * 2.0 - 1.0;
            double posZ = random.nextDouble() * 2.0 - 1.0;
            double size = MHelper.randRange(minSize, maxSize, random);
            double length = posX * posX + posY * posY + posZ * posZ;

            if (length < 1.0 && length > 0.001) {
                length = 1.0 / Math.sqrt(length);
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
                    builder.vertex(px + dx, py + dy, pz + dz).endVertex();
                }
            }
        }

        RenderedBuffer renderedBuffer = builder.end();
        buffer.bind();
        buffer.upload(renderedBuffer);

        return buffer;
    }
}