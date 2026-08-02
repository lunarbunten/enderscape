package net.penumbra.enderscape.renderer.entity.enderman;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class ImprovedEndermanEyesLayer extends RenderLayer<EndermanRenderState, ImprovedEndermanModel<EndermanRenderState>> {
    private static final RenderType ENDERMAN_EYES = RenderTypes.eyes(Identifier.withDefaultNamespace("textures/entity/enderman/enderman_eyes.png"));

    public ImprovedEndermanEyesLayer(RenderLayerParent<EndermanRenderState, ImprovedEndermanModel<EndermanRenderState>> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int i, EndermanRenderState state, float f, float g) {
        collector.submitModel(getParentModel(), state, pose, ENDERMAN_EYES, i, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }
}