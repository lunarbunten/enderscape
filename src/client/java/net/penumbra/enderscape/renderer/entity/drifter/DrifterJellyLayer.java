package net.penumbra.enderscape.renderer.entity.drifter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.penumbra.enderscape.Enderscape;

public class DrifterJellyLayer extends RenderLayer<DrifterRenderState, DrifterModel> {
    private static final RenderType JELLY_OVERLAY = RenderTypes.entityCutout(Enderscape.id("textures/entity/drifter/drifter_jelly.png"));

    public DrifterJellyLayer(RenderLayerParent<DrifterRenderState, DrifterModel> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int i, DrifterRenderState state, float f, float g) {
        if (!state.leakingJelly) return;
        collector.submitModel(getParentModel(), state, pose, JELLY_OVERLAY, i, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }
}