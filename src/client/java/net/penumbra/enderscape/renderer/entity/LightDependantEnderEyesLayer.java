package net.penumbra.enderscape.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.util.ClientsideLightUtil;

public class LightDependantEnderEyesLayer<S extends EntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {

    private final RenderType darkEyesType;
    private final RenderType brightEyesType;

    public LightDependantEnderEyesLayer(RenderLayerParent<S, M> renderer, Identifier darkScleraTexturePath, Identifier brightTexturePath) {
        super(renderer);

        this.darkEyesType = RenderTypes.eyes(darkScleraTexturePath);
        this.brightEyesType = RenderTypes.eyes(brightTexturePath);
    }

    public static <S extends EntityRenderState, M extends EntityModel<S>> LightDependantEnderEyesLayer<S, M> enderman(RenderLayerParent<S, M> renderer) {
        return new LightDependantEnderEyesLayer<>(
                renderer,
                Enderscape.id("textures/entity/enderman/enderman_eyes_dark.png"),
                Enderscape.id("textures/entity/enderman/enderman_eyes_bright.png")
        );
    }

    @Override
    public void submit(final PoseStack pose, final SubmitNodeCollector collector, final int lightCoords, final S state, final float yRot, final float xRot) {
        float brightness = 1.0F;

        if (state instanceof StoresSkylightFactor stored) {
            brightness = ClientsideLightUtil.lightBrightness(lightCoords, stored.skyLightFactor());
        }

        int bright = ARGB.white(brightness);
        int dark = ARGB.white(1.0F - brightness);

        collector.order(1).submitModel(getParentModel(), state, pose, darkEyesType, lightCoords, OverlayTexture.NO_OVERLAY, dark, null, state.outlineColor, null);
        collector.order(2).submitModel(getParentModel(), state, pose, brightEyesType, lightCoords, OverlayTexture.NO_OVERLAY, bright, null, state.outlineColor, null);
    }
}