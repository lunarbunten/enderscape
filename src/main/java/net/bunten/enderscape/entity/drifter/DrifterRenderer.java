package net.bunten.enderscape.entity.drifter;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class DrifterRenderer extends MobRenderer<Drifter, DrifterModel> {
    public DrifterRenderer(Context context) {
        super(context, new DrifterModel(EnderscapeClient.DRIFTER.bakeLayer(context)), 1);
        addLayer(new DrifterJellyLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Drifter mob) {
        return Enderscape.id("textures/entity/drifter/drifter.png");
    }
}