package net.penumbra.enderscape.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class EndHavenCoreSpecialRenderer implements NoDataSpecialModelRenderer {

    @Override
    public void submit(final PoseStack pose, final SubmitNodeCollector collector, final int lightCoords, final int overlayCoords, final boolean hasFoil, final int outlineColor) {
        EndHavenCoreRenderer.submitSpecial(RenderTypes.endGateway(), pose, collector);
    }

    @Override
    public void getExtents(final Consumer<Vector3fc> output) {
        EndHavenCoreRenderer.getExtents(output);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<Void> bake(final SpecialModelRenderer.BakingContext context) {
            return new EndHavenCoreSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
