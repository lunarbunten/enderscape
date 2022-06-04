package net.bunten.enderscape.entity.driftlet;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DriftletRenderer extends MobEntityRenderer<DriftletEntity, DriftletModel> {
    public DriftletRenderer(Context context) {
        super(context, new DriftletModel(context.getPart(EnderscapeClient.DRIFTLET)), 0.7F);
    }

    @Override
    public Identifier getTexture(DriftletEntity mob) {
        return Enderscape.id("textures/entity/drifter/driftlet.png");
    }

    @Override
    protected RenderLayer getRenderLayer(DriftletEntity mob, boolean showBody, boolean translucent, boolean showOutline) {
        return RenderLayer.getEntityTranslucent(getTexture(mob));
    }
}