package net.enderscape.entity.motu;

import net.enderscape.Enderscape;
import net.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MotuRenderer extends MobEntityRenderer<MotuEntity, MotuModel> {
    public MotuRenderer(Context context) {
        super(context, new MotuModel(context.getPart(EnderscapeClient.MOTU)), 0.4F);
        addFeature(new EyeRenderer(this));
    }

    @Override
    public Identifier getTexture(MotuEntity mob) {
        return Enderscape.id("textures/entity/motu.png");
    }

    class EyeRenderer extends EyesFeatureRenderer<MotuEntity, MotuModel> {
        public EyeRenderer(FeatureRendererContext<MotuEntity, MotuModel> context) {
            super(context);
        }

        public RenderLayer getEyesTexture() {
            return RenderLayer.getEyes(Enderscape.id("textures/entity/motu_glow.png"));
        }
    }
}