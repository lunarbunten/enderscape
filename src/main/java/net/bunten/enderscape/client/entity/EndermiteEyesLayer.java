package net.bunten.enderscape.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EndermiteModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

@Environment(EnvType.CLIENT)
public class EndermiteEyesLayer extends RenderLayer<LivingEntityRenderState, EndermiteModel> {
    private static final RenderType ENDERMITE_EYES = RenderType.eyes(Enderscape.id("textures/entity/endermite/eyes.png"));

    public EndermiteEyesLayer(RenderLayerParent<LivingEntityRenderState, EndermiteModel> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int i, LivingEntityRenderState state, float f, float g) {
        collector.submitModel(getParentModel(), state, pose, ENDERMITE_EYES, i, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }
}