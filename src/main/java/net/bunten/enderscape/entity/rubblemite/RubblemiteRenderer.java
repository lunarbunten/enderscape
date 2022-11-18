package net.bunten.enderscape.entity.rubblemite;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class RubblemiteRenderer extends MobRenderer<Rubblemite, RubblemiteModel> {
    public RubblemiteRenderer(Context context) {
        super(context, new RubblemiteModel(EnderscapeClient.RUBBLEMITE.bakeLayer(context)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Rubblemite mob) {
        return Enderscape.id("textures/entity/rubblemite/" + mob.getVariant().getName() + ".png");
    }
}