package net.penumbra.enderscape.registry.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.penumbra.enderscape.Enderscape;

import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED_SNIPPET;
import static net.minecraft.client.renderer.RenderPipelines.MATRICES_PROJECTION_SNIPPET;

public class EnderscapeRenderPipelines {

    public static final RenderPipeline IMPROVED_LIGHTMAP = RenderPipelines.register(
            RenderPipeline.builder()
                    .withLocation(Enderscape.id("pipeline/improved_lightmap"))
                    .withVertexShader("core/screenquad")
                    .withFragmentShader(Enderscape.id("enderscape/improved_lightmap"))
                    .withUniform("LightmapInfo", UniformType.UNIFORM_BUFFER)
                    .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
                    .build()
    );

    public static final RenderPipeline MIDNIGHT_LIGHTMAP = RenderPipelines.register(
            RenderPipeline.builder()
                    .withLocation(Enderscape.id("pipeline/midnight_lightmap"))
                    .withVertexShader("core/screenquad")
                    .withFragmentShader(Enderscape.id("enderscape/midnight_lightmap"))
                    .withUniform("LightmapInfo", UniformType.UNIFORM_BUFFER)
                    .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
                    .build()
    );

    public static final RenderPipeline ALPHA_BASED_OVERLAY = RenderPipelines.register(
            RenderPipeline.builder(GUI_TEXTURED_SNIPPET)
                    .withLocation(Enderscape.id("pipeline/alpha_based_overlay"))
                    .withColorTargetState(new ColorTargetState(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_CONSTANT_ALPHA)))
                    .build()
    );
    public static final RenderPipeline NEBULAE_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(MATRICES_PROJECTION_SNIPPET)
                    .withLocation(Enderscape.id("pipeline/nebulae"))
                    .withVertexShader("core/position_tex")
                    .withFragmentShader("core/position_tex")
                    .withSampler("Sampler0")
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
                    .build()
    );
}
