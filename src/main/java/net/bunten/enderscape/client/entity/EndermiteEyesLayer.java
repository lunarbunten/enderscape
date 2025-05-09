package net.bunten.enderscape.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EndermiteModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.monster.Endermite;

@Environment(EnvType.CLIENT)
public class EndermiteEyesLayer extends RenderLayer<Endermite, EndermiteModel<Endermite>> {

    public EndermiteEyesLayer(RenderLayerParent<Endermite, EndermiteModel<Endermite>> renderLayerParent) {
        super(renderLayerParent);
    }

    public RenderType renderType() {
        return RenderType.eyes(Enderscape.id("textures/entity/endermite/eyes.png"));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource source, int i, Endermite mob, float f, float g, float h, float j, float k, float l) {
        getParentModel().renderToBuffer(pose, source.getBuffer(renderType()), i, OverlayTexture.NO_OVERLAY);
    }
}