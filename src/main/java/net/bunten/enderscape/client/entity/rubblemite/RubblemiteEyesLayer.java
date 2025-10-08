package net.bunten.enderscape.client.entity.rubblemite;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

@Environment(EnvType.CLIENT)
public class RubblemiteEyesLayer extends RenderLayer<RubblemiteRenderState, RubblemiteModel> {
    private static final RenderType RUBBLEMITE_EYES = RenderType.eyes(Enderscape.id("textures/entity/rubblemite/eyes.png"));

    public RubblemiteEyesLayer(RenderLayerParent<RubblemiteRenderState, RubblemiteModel> parent) {
        super(parent);
    }


    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int i, RubblemiteRenderState state, float f, float g) {
        collector.submitModel(getParentModel(), state, pose, RUBBLEMITE_EYES, i, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }
}