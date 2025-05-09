package net.bunten.enderscape.client.entity.drifter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class DrifterJellyLayer extends RenderLayer<Drifter, DrifterModel> {
    public DrifterJellyLayer(RenderLayerParent<Drifter, DrifterModel> parent) {
        super(parent);
    }

    private final ResourceLocation JELLY_LAYER_TEXTURE = Enderscape.id("textures/entity/drifter/jelly.png");

    @Override
    public void render(PoseStack pose, MultiBufferSource source, int i, Drifter mob, float f, float g, float h, float j, float k, float l) {
        if (mob.isDrippingJelly()) {
            VertexConsumer vertex = source.getBuffer(RenderType.entityCutoutNoCull(JELLY_LAYER_TEXTURE));
            getParentModel().renderToBuffer(pose, vertex, i, OverlayTexture.NO_OVERLAY);
        }
    }
}