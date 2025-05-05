package net.bunten.enderscape.client.entity.rubblemite;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

@Environment(EnvType.CLIENT)
public class RubblemiteEyesLayer extends RenderLayer<RubblemiteRenderState, RubblemiteModel> {

    public RubblemiteEyesLayer(RenderLayerParent<RubblemiteRenderState, RubblemiteModel> renderLayerParent) {
        super(renderLayerParent);
    }

    public RenderType renderType() {
        return RenderType.eyes(Enderscape.id("textures/entity/rubblemite/eyes.png"));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource source, int i, RubblemiteRenderState state, float f, float g) {
        getParentModel().renderToBuffer(pose, source.getBuffer(renderType()), i, OverlayTexture.NO_OVERLAY);
    }
}