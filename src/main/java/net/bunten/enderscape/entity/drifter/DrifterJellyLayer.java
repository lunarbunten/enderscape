package net.bunten.enderscape.entity.drifter;

import com.mojang.blaze3d.vertex.PoseStack;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class DrifterJellyLayer extends RenderLayer<Drifter, DrifterModel> {
    private final DrifterModel model;
    
    public DrifterJellyLayer(RenderLayerParent<Drifter, DrifterModel> parent, EntityModelSet set) {
        super(parent);
        this.model = new DrifterModel(set.bakeLayer(EnderscapeClient.DRIFTER.getModelLayerLocation()));
    }

    @Override
    public void render(PoseStack matrix, MultiBufferSource source, int i, Drifter mob, float f, float g, float h, float j, float k, float l) {
        if (mob.isDrippingJelly()) {
            coloredCutoutModelCopyLayerRender(getParentModel(), model, getTexture(mob), matrix, source, i, mob, f, g, j, k, l, h, 1, 1, 1);
        }
    }

    private ResourceLocation getTexture(Drifter mob) {
        return Enderscape.id("textures/entity/drifter/jelly.png");
    }
}