package net.bunten.enderscape.entity.driftlet;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class DriftletRenderer extends MobRenderer<Driftlet, DriftletModel> {
    public DriftletRenderer(Context context) {
        super(context, new DriftletModel(EnderscapeClient.DRIFTLET.bakeLayer(context)), 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(Driftlet mob) {
        return Enderscape.id("textures/entity/drifter/driftlet.png");
    }

    @Override
    protected RenderType getRenderType(Driftlet mob, boolean showBody, boolean translucent, boolean showOutline) {
        return showBody ? RenderType.entityTranslucent(getTextureLocation(mob)) : super.getRenderType(mob, showBody, translucent, showOutline);
    }
}