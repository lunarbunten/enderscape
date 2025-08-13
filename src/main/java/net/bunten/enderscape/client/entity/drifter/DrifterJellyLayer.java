package net.bunten.enderscape.client.entity.drifter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class DrifterJellyLayer extends RenderLayer<DrifterRenderState, DrifterModel> {

    public DrifterJellyLayer(RenderLayerParent<DrifterRenderState, DrifterModel> parent) {
        super(parent);
    }

    public RenderType renderType() {
        return RenderType.entityCutoutNoCull(Enderscape.id("textures/entity/drifter/jelly.png"));
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int i, DrifterRenderState state, float f, float g) {
        if (!state.leakingJelly) return;

        collector.submitModel(
                getParentModel(), state, pose, renderType(), i, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, 1
        );
    }
}