package net.bunten.enderscape.client.renderer;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.bunten.enderscape.Enderscape;
import net.minecraft.client.renderer.RenderPipelines;

public class EnderscapeRenderPipelines {

    public static final RenderPipeline IMPROVED_LIGHTMAP = RenderPipelines.register(
            RenderPipeline.builder()
                    .withLocation(Enderscape.id("pipeline/improved_lightmap"))
                    .withVertexShader("core/screenquad")
                    .withFragmentShader(Enderscape.id("enderscape/improved_lightmap"))
                    .withUniform("LightmapInfo", UniformType.UNIFORM_BUFFER)
                    .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
                    .withDepthWrite(false)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .build()
    );

    public static final RenderPipeline MIDNIGHT_LIGHTMAP = RenderPipelines.register(
            RenderPipeline.builder()
                    .withLocation(Enderscape.id("pipeline/midnight_lightmap"))
                    .withVertexShader("core/screenquad")
                    .withFragmentShader(Enderscape.id("enderscape/midnight_lightmap"))
                    .withUniform("LightmapInfo", UniformType.UNIFORM_BUFFER)
                    .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
                    .withDepthWrite(false)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .build()
    );
}
