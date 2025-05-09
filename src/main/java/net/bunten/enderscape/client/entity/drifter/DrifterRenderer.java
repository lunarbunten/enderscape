package net.bunten.enderscape.client.entity.drifter;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeEntityRenderData;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class DrifterRenderer extends MobRenderer<Drifter, DrifterModel> {

    public DrifterRenderer(Context context) {
        super(context, new DrifterModel(EnderscapeEntityRenderData.DRIFTER.bakeLayer(context)), 1);
        addLayer(new DrifterJellyLayer(this));
    }

    @Override
    protected int getBlockLightLevel(Drifter mob, BlockPos pos) {
        return Math.max(3, super.getBlockLightLevel(mob, pos));
    }

    @Override
    public ResourceLocation getTextureLocation(Drifter state) {
        return Enderscape.id("textures/entity/drifter/drifter.png");
    }
}