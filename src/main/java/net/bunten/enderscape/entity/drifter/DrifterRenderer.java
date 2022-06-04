package net.bunten.enderscape.entity.drifter;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DrifterRenderer extends MobEntityRenderer<DrifterEntity, DrifterModel> {
    public DrifterRenderer(Context context) {
        super(context, new DrifterModel(context.getPart(EnderscapeClient.DRIFTER)), 1);
    }

    @Override
    public Identifier getTexture(DrifterEntity mob) {
        String suffix = mob.isDrippingJelly() ? "_jelly.png" : ".png";
        return Enderscape.id("textures/entity/drifter/drifter" + suffix);
    }
}