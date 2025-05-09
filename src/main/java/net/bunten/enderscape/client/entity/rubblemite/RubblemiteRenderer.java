package net.bunten.enderscape.client.entity.rubblemite;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeEntityRenderData;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.bunten.enderscape.entity.rubblemite.RubblemiteVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class RubblemiteRenderer extends MobRenderer<Rubblemite, RubblemiteModel> {
    public RubblemiteRenderer(Context context) {
        super(context, new RubblemiteModel(EnderscapeEntityRenderData.RUBBLEMITE.bakeLayer(context)), 0.4F);
        addLayer(new RubblemiteEyesLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Rubblemite mob) {
        return Enderscape.id("textures/entity/rubblemite/" + RubblemiteVariant.get(mob).getName() + ".png");
    }
}