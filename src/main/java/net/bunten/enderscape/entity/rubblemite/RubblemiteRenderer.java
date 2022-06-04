package net.bunten.enderscape.entity.rubblemite;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.EnderscapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RubblemiteRenderer extends MobEntityRenderer<RubblemiteEntity, RubblemiteModel> {
    public RubblemiteRenderer(Context context) {
        super(context, new RubblemiteModel(context.getPart(EnderscapeClient.RUBBLEMITE)), 0.4F);
    }

    @Override
    public Identifier getTexture(RubblemiteEntity mob) {
        return Enderscape.id("textures/entity/rubblemite.png");
    }
}