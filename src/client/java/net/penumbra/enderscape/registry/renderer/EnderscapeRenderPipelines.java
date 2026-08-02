package net.penumbra.enderscape.registry.renderer;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.penumbra.enderscape.Enderscape;

import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED_SNIPPET;

public class EnderscapeRenderPipelines {

    public static final RenderPipeline IMPROVED_LIGHTMAP = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
                    .withLocation(Enderscape.id("pipeline/improved_lightmap"))
                    .withVertexShader("core/screenquad")
                    .withFragmentShader(Enderscape.id("enderscape/improved_lightmap"))
                    .withBindGroupLayout(BindGroupLayouts.LIGHTMAP_INFO)
                    .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
                    .build()
    );

    public static final RenderPipeline MIDNIGHT_LIGHTMAP = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
                    .withLocation(Enderscape.id("pipeline/midnight_lightmap"))
                    .withVertexShader("core/screenquad")
                    .withFragmentShader(Enderscape.id("enderscape/midnight_lightmap"))
                    .withBindGroupLayout(BindGroupLayouts.LIGHTMAP_INFO)
                    .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
                    .build()
    );

    public static final RenderPipeline ALPHA_BASED_OVERLAY = RenderPipelines.register(
            RenderPipeline.builder(GUI_TEXTURED_SNIPPET)
                    .withLocation(Enderscape.id("pipeline/alpha_based_overlay"))
                    .withColorTargetState(new ColorTargetState(new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_CONSTANT_ALPHA)))
                    .build()
    );
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
}
